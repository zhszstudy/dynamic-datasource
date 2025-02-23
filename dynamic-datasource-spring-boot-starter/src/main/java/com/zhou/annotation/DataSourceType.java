package com.zhou.annotation;

import java.lang.annotation.*;

/**
 * @author zhou22
 * @desc 数据源选择标记
 * @Date 2025-02-19 10:20:27
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceType {

    String dataSourceName() default "";

}