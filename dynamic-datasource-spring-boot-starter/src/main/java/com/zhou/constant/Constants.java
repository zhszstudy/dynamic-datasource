package com.zhou.constant;

/**
 * @author zhou22
 * @desc 常量接口
 * @Date 2025-02-19 11:52:49
 */
public interface Constants {

    String COMMA = ",";


    // 动态数据源常量
    interface DynamicDataSourceConstants {
        // 动态数据源配置前缀
        String PREFIX = "dynamic.jdbc.datasource.";

        // 数据源列表
        String DATA_SOURCE_LIST = "list";

        // 默认数据源
        String DEFAULT_DATA_SOURCE = "default";

        // 连接池属性
        String POOL_KEY = "pool";

        // 数据库连接相关配置
        String USERNAME = "username";

        String URL = "url";

        String PASSWORD = "password";

        String DRIVER_CLASS_NAME = "driver-class-name";

        String TYPE_CLASS_NAME = "type-class-name";
    }

    // druid监控常量
    interface DruidMonitorConstants {

        String WEB_PREFIX = "druid.monitor.web-stat-filter";

        String STAT_PREFIX = "druid.monitor.stat-view-servlet";

        String LOGIN_USERNAME = "loginUsername";

        String LOGIN_PASSWORD = "loginPassword";

        String RESET_ENABLE = "resetEnable";

        String EXCLUSIONS = "exclusions";
    }

    // mybatis扫描常量
    interface MybatisScannerConstants {
        String PREFIX = "mybatis.scanner";
    }
}
