package com.zhou.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zhou.DynamicDataSourceAspect;
import com.zhou.constant.Constants;
import com.zhou.dynamic.DynamicDataSource;
import com.zhou.utils.MapKeyConvertUtils;
import com.zhou.utils.PropertyUtil;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.zhou.constant.Constants.*;

/**
 * @author zhou22
 * @desc 动态数据源切换配置
 * @Date 2025-02-19 10:37:08
 */
@Configuration
public class DynamicDataSourceAutoConfig implements EnvironmentAware {

    // 数据源分组
    private final Map<String, Map<String, Object>> dataSourceMap = new HashMap<>();

    // 默认数据源名称
    private String defaultDataSourceName;

    private Environment environment;
    
    /**
     * 读取配置文件
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        // 获取默认数据源名称
        this.defaultDataSourceName = PropertyUtil.convertToTarget(environment, Constants.DynamicDataSourceConstants.PREFIX + Constants.DynamicDataSourceConstants.DEFAULT_DATA_SOURCE, String.class);
        // 获取数据源名称列表
        String dataSources = PropertyUtil.convertToTarget(environment, Constants.DynamicDataSourceConstants.PREFIX + Constants.DynamicDataSourceConstants.DATA_SOURCE_LIST, String.class);
        for (String dataSource : dataSources.split(COMMA)) {
            // 挨个获取数据源配置
            Map<String, Object> dataSourceProperties = PropertyUtil.convertToTarget(environment, DynamicDataSourceConstants.PREFIX + dataSource, Map.class);
            dataSourceMap.put(dataSource, dataSourceProperties);
        }
    }

		// 配置数据源切面
    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

		// 配置自定义数据源：动态数据源核心实现
    @Bean("dynamicDataSource")
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        // 将读取的数据源配置信息，依次转为真实的数据源对象
        for (Map.Entry<String, Map<String, Object>> entry : dataSourceMap.entrySet()) {
            DataSource dataSource = createDataSource(entry.getValue());
            targetDataSources.put(entry.getKey(), dataSource);
        }
        // 设置配置的所有数据源，后续会根据这个map来实现数据源切换
        dynamicDataSource.setTargetDataSources(targetDataSources);
        // 设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get(defaultDataSourceName));
        return dynamicDataSource;
    }

    /**
     * 创建数据源
     *
     * @param dataSourcePropertyMap
     * @return
     */
    private DataSource createDataSource(Map<String, Object> dataSourcePropertyMap) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUrl(dataSourcePropertyMap.get(Constants.DynamicDataSourceConstants.URL).toString());
        dataSourceProperties.setUsername(dataSourcePropertyMap.get(Constants.DynamicDataSourceConstants.USERNAME).toString());
        dataSourceProperties.setPassword(dataSourcePropertyMap.get(Constants.DynamicDataSourceConstants.PASSWORD).toString());
        dataSourceProperties.setDriverClassName(dataSourcePropertyMap.get(Constants.DynamicDataSourceConstants.DRIVER_CLASS_NAME).toString());
        String typeClassName = dataSourcePropertyMap.get(Constants.DynamicDataSourceConstants.TYPE_CLASS_NAME).toString();

        try {
            // 创建数据源
            DataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type((Class<DataSource>) Class.forName(typeClassName)).build();
            // 获取连接池配置，支持多种连接池
            Map<String, Object> poolProperties = (Map<String, Object>) (dataSourcePropertyMap.containsKey(Constants.DynamicDataSourceConstants.POOL_KEY) ? dataSourcePropertyMap.get(DynamicDataSourceConstants.POOL_KEY) : Collections.emptyMap());
            // 反射设置连接池配置信息
            MetaObject metaObject = SystemMetaObject.forObject(dataSource);
            for (Map.Entry<String, Object> poolProperty : poolProperties.entrySet()) {
                String key = MapKeyConvertUtils.middleLineToCamelHump(poolProperty.getKey());
                if (metaObject.hasSetter(key)) {
                    metaObject.setValue(key, poolProperty.getValue());
                }
            }
            return dataSource;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("数据源连接池配置失效，无法找到类：" + typeClassName);
        }
    }

    /**
     * SqlSession工厂配置
     *
     * @param dynamicDataSource
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dynamicDataSource) throws Exception {
        // 将配置映射到配置类
        MybatisScannerProperties mybatisScannerProperties = PropertyUtil.convertToTarget(environment, Constants.MybatisScannerConstants.PREFIX, MybatisScannerProperties.class);
        // 配置mybatis-plus扫描
        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        // 指定数据源为配置的动态数据源
        factory.setDataSource(dynamicDataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mybatisScannerProperties.getMapperLocations()));
        factory.setTypeAliasesPackage(mybatisScannerProperties.getTypeAliasesPackage());
        MybatisConfiguration configuration = new MybatisConfiguration();
        // 是否开启数据库字段下划线命名到Java属性驼峰命名的自动映射
        configuration.setMapUnderscoreToCamelCase(mybatisScannerProperties.isMapUnderscoreToCamelCase());
        // 日志输出类配置
        configuration.setLogImpl((Class<? extends Log>) Class.forName(mybatisScannerProperties.getLogImpl()));
        factory.setConfiguration(configuration);
        return factory.getObject();
    }

		
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dynamicDataSource) {
        // 指定数据源为配置的动态数据源
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    /**
     * spring事务管理配置
     *
     * @param transactionManager
     * @return
     */
    @Bean
    public TransactionTemplate transactionTemplate(DataSourceTransactionManager transactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        transactionTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        return  transactionTemplate;
    }

    /**
     * druid监控页面配置-帐号密码配置
     *
     * @return servlet registration bean
     */
    @ConditionalOnProperty(prefix = DruidMonitorConstants.STAT_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        // 将配置映射到配置类
        DruidMonitorProperties.StatViewServlet statViewServlet = PropertyUtil.convertToTarget(environment, DruidMonitorConstants.STAT_PREFIX, DruidMonitorProperties.StatViewServlet.class);
        // druid监控帐号密码配置
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), statViewServlet.getUrlPattern());
        servletRegistrationBean.addInitParameter(DruidMonitorConstants.LOGIN_USERNAME, statViewServlet.getLoginUsername());
        servletRegistrationBean.addInitParameter(DruidMonitorConstants.LOGIN_PASSWORD, statViewServlet.getLoginPassword());
        servletRegistrationBean.addInitParameter(DruidMonitorConstants.RESET_ENABLE, String.valueOf(statViewServlet.isResetEnable()));
        return servletRegistrationBean;
    }

    /**
     * druid监控页面配置-允许页面正常浏览
     *
     * @return filter registration bean
     */
    @ConditionalOnProperty(prefix = DruidMonitorConstants.WEB_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    public FilterRegistrationBean druidWebStataFilter() {
        DruidMonitorProperties.WebStatFilter webStatFilter = PropertyUtil.convertToTarget(environment, DruidMonitorConstants.WEB_PREFIX, DruidMonitorProperties.WebStatFilter.class);
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则.
        filterRegistrationBean.addUrlPatterns(webStatFilter.getUrlPattern());
        // 排除不需要统计的URL请求
        filterRegistrationBean.addInitParameter(DruidMonitorConstants.EXCLUSIONS, webStatFilter.getExclusions());
        return filterRegistrationBean;
    }

}
