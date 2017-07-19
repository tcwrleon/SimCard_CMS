package com.sunyard.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

//容器启动时可以取bean对象
@Service
public class SpringBeanFactoryUtil implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public Object getBean(String beanName){
        return beanFactory.getBean(beanName);
    }
    
    public <T> T getBean(Class<T> cls){
    	return beanFactory.getBean(cls);
    }
}
