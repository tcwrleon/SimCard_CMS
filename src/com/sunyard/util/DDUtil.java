package com.sunyard.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sunyard.entity.system.DictEntity;
import com.sunyard.service.system.DictService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author mumu
 * 数据字典缓存类
 */
@Service
public class DDUtil {

    private static Logger logger = LoggerFactory.getLogger(DDUtil.class);

    @Resource
    private DictService dictService;
    private static DictService staticDictService;
   
    private static Cache cache;
    
    /**
     * 缓存数据字典数据
     * 缓存数据有三种形式：  1 主键加上值为key,中文说明为value  如：[key="K_USERTYPE-1",value="授权用户"]
     * 				 2   主键加上中文说明为key,值为value   如：[key="K_USERTYPE-授权用户",value="1"]
     * 				 3   主键为key,DictEntity的集合为value   如：[key="K_USERTYPE",value="[DictEntity1,DictEntity2]"]
     * @throws Exception
     */
    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
	public void putAllInCache() throws Exception {
    	staticDictService = dictService;
        List<DictEntity> list = staticDictService.queryAll(new DictEntity());
        cache = CacheUtil.getCacheManager().getCache("ddCache");
        cache.removeAll();

        if(list != null){
        	String sun_key = "";
        	List<DictEntity> valuesByKey = null;
        	
        	for(int i = 0; i < list.size(); i++){
            	DictEntity dict = list.get(i);
            	
            	cache.put(new Element(dict.getSun_key().concat("-").concat(dict.getVal()), dict.getPrompt()));//缓存数据形式1
            	cache.put(new Element(dict.getSun_key().concat("-").concat(dict.getPrompt()), dict.getVal()));//缓存数据形式2
            	
            	//缓存数据形式3
            	if(sun_key.equals(dict.getSun_key())){
            		valuesByKey.add(dict);
            		
            		if(i==list.size()-1){
            			cache.put(new Element(sun_key, valuesByKey));
            			logger.info("key="+ sun_key + "值为" + (List<DictEntity>)cache.get(sun_key).getObjectValue());
            			valuesByKey = Collections.synchronizedList(new ArrayList<DictEntity>());
            		}
            	}else{
            		if(i==0){
            			valuesByKey = Collections.synchronizedList(new ArrayList<DictEntity>());
            			sun_key = dict.getSun_key();
            			valuesByKey.add(dict);
            		}else{
            			cache.put(new Element(sun_key, valuesByKey));
            			logger.info("key="+ sun_key + "值为" + (List<DictEntity>)cache.get(sun_key).getObjectValue());
            			valuesByKey = Collections.synchronizedList(new ArrayList<DictEntity>());
                		sun_key = dict.getSun_key();
                		valuesByKey.add(dict);
            		}
            	}
            }
        }
        
    }
    
    /**
     * 根据主键和值取中文说明
     * 从缓存中取值；不存在，从数据库查询，并缓存之
     * @param sun_key
     * @param val
     * @return
     */
    public static final String getContent(String sun_key,String val){
        String result = "";
        if(StringUtil.isEmpty(val)){
        	return result;
        }
        Element element = cache.get(sun_key + "-" + val);
        if(element != null && element.getObjectValue() != null){//从缓存中取值（根据key值）
            result = String.valueOf(element.getObjectValue());
            if(result != null){
                return result;
            }
        }else{
            //从数据库查询，并缓存之
            String allDdContentSpec = staticDictService.getAllDdContentSpec(sun_key, val);
            if (allDdContentSpec ==null){
            	result = val;
            }else {
                result = allDdContentSpec;
            }
        }
        return result;
    }
    /**
     * 根据主键和中文取值说明
     * 从缓存中取值；不存在，从数据库查询，并缓存之
     * @param sun_key
     * @param prompt
     * @return
     */
    public static final String getValue(String sun_key,String prompt){
        String result = "";
        if(StringUtil.isEmpty(prompt)){
        	return result;
        }
        Element element = cache.get(sun_key + "-" + prompt);
        if(element != null && element.getObjectValue() != null){//从缓存中取值（根据key值）
            result = String.valueOf(element.getObjectValue());
            if(result != null){
                return result;
            }
        }
        return result;
    }

    /**
     * 根据主键查询所有数据字典
     * 从缓存中取值；不存在，从数据库查询，并缓存之
     * @param sun_key
     * @return
     */
    @SuppressWarnings("unchecked")
	public static final List<DictEntity> getDDContentsByKey(String sun_key){
        Element element = cache.get(sun_key);
        List<DictEntity> result = null;
        if(element != null && element.getObjectValue() != null){//从缓存中取值（根据key值）
            result = (List<DictEntity>)element.getObjectValue();
            if(result!=null){
                return result;
            }
        }else{
            //从数据库查询，并缓存之
            List<DictEntity> allDdContentSpecList = staticDictService.getAllDdContentByKey(sun_key);
            if (allDdContentSpecList ==null){

            }else {
                result = allDdContentSpecList;
            }
        }
        return result;
    }
  
    //删除某一个key对应的值
    public static void removeFromCache(String sun_key,String val){
        String key=sun_key + "-" + val;
        cache.remove(key);
    }
    
    public static void removeFromCache(String sun_key){
    	cache.remove(sun_key);
    }
     //删除所有
    public static void removeFromCache(){
        cache.removeAll();
    }
    
    
}
