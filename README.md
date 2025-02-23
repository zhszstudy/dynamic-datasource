# dynamic-datasource

#### 介绍
SpringBoot+Mybatis-Plus实现动态数据源



#### 使用说明

1. 引入依赖：

   ```xml
   <dependency>
               <groupId>com.zhou</groupId>
               <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
               <version>1.0</version>
           </dependency>
   ```

2.  排除自动装配数据源配置：

    ```java
    @SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
    ```

3.  yml文件配置：

    ```yml
    server:
      port: 8348
    
    dynamic:
      jdbc:
        datasource:
          default: master
          list: master,slave
          master:
            url: jdbc:mysql://localhost:3306/datasource_test1?useSSL=false&serverTimezone=UTC
            username: root
            password: your_password
            driver-class-name: com.mysql.jdbc.Driver
            type-class-name: com.alibaba.druid.pool.DruidDataSource
            pool:
              max-active: 10
              initial-size: 1
              max-wait: 30000
              min-idle: 1
              time-between-eviction-runs-millis: 30000
              min-evictable-idle-time-millis: 150000
              validation-query: select 'x'
              test-while-idle: true
              test-on-borrow: false
              test-on-return: false
              pool-prepared-statements: true
              max-open-prepared-statements: 20
              filters: stat, wall
              testConnectionOnCheckout: false
              testConnectionOnCheckin: true
              idleConnectionTestPeriod: 3600
          slave:
            url: jdbc:mysql://localhost:3306/datasource_test2?useSSL=false&serverTimezone=UTC
            username: root
            password: your_password
            driver-class-name: com.mysql.jdbc.Driver
            type-class-name: com.alibaba.druid.pool.DruidDataSource
            pool:
              max-active: 20
              initial-size: 1
              max-wait: 3000
              min-idle: 1
              time-between-eviction-runs-millis: 6000
              min-evictable-idle-time-millis: 3000
              validation-query: select 'x'
              test-while-idle: true
              test-on-borrow: false
              test-on-return: false
              pool-prepared-statements: true
              max-open-prepared-statements: 20
              filters: stat, wall
              testConnectionOnCheckout: false
              testConnectionOnCheckin: true
              idleConnectionTestPeriod: 360
    druid:
      monitor:
        web-stat-filter:
          # 是否开启配置
          enabled: true
          url-pattern: /*
          exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
        stat-view-servlet:
          url-pattern: /druid/*
          reset-enable: false
          # 是否开启配置
          enabled: true
          login-username: admin
          login-password: admin
    
    # mybatis 配置
    mybatis:
      scanner:
        mapperLocations: classpath:mapper/*Mapper.xml
        # 实体类别名配置
        typeAliasesPackage: com.zhou.model
        mapUnderscoreToCamelCase: true
        logImpl: org.apache.ibatis.logging.stdout.StdOutImpl
    ```

4.  方法使用：

    ```java
    @DataSourceType(dataSourceName = "slave")
    @Override
    public List<Student> queryStudentFromSlave() {
        return baseMapper.selectList(null);
    }
    ```

    



