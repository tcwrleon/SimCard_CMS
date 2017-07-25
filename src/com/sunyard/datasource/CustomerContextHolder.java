package com.sunyard.datasource;


import org.apache.commons.lang3.StringUtils;

public class CustomerContextHolder {
    public static final String DATA_SOURCE_MAIN = "dataSourceMain";
    public static final String DATA_SOURCE_CATPOOLCMCC = "dataSourceCatPoolCMCC";
    public static final String DATA_SOURCE_CATPOOLCTCC = "dataSourceCatPoolCTCC";
    public static final String DATA_SOURCE_CATPOOLCUCC = "dataSourceCatPoolCUCC";
    //用ThreadLocal来设置当前线程使用哪个dataSource
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static void setCustomerType(String customerType) {
        contextHolder.set(customerType);
    }
    public static String getCustomerType() {
        String dataSource = contextHolder.get();
        if (StringUtils.isEmpty(dataSource)) {
            return DATA_SOURCE_MAIN;
        }else if(dataSource.equals(DATA_SOURCE_CATPOOLCMCC)) {
            return DATA_SOURCE_CATPOOLCMCC;
        }else if(dataSource.equals(DATA_SOURCE_CATPOOLCTCC)){
            return DATA_SOURCE_CATPOOLCTCC;
        }else if(dataSource.equals(DATA_SOURCE_CATPOOLCUCC)){
            return DATA_SOURCE_CATPOOLCUCC;
        }else{
            return DATA_SOURCE_MAIN;
        }
    }
    public static void clearCustomerType() {
        contextHolder.remove();
    }
}