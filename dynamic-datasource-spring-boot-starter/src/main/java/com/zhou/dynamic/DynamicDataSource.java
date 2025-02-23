package com.zhou.dynamic;

import com.zhou.DataSourceContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author zhou22
 * @desc 动态数据源获取
 * @Date 2025-02-19 10:33:19
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Value("${dynamic.jdbc.datasource.default}")
    private String defaultDataSource;

    @Override
    protected Object determineCurrentLookupKey() {
        // 从数据源上下文获取要切换的数据源
        String datasource = DataSourceContextHolder.getDatasource();
        // 如果没有配置注解，则选择默认数据源
        return datasource != null ? datasource : defaultDataSource;
    }
}