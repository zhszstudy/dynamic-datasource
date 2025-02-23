package com.zhou;

/**
 * @author zhou22
 * @desc 数据源上下文
 * @Date 2025-02-19 10:14:10
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> dataSourceName = new ThreadLocal<>();

    public static String getDatasource() {
        return dataSourceName.get();
    }

    public static void setDatasource(String datasource) {
        dataSourceName.set(datasource);
    }

    public static void clearDatasource() {
        dataSourceName.remove();
    }

}