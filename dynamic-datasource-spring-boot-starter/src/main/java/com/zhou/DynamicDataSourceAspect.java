package com.zhou;

import com.zhou.annotation.DataSourceType;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author zhou22
 * @desc 数据源选择切面
 * @Date 2025-02-19 10:25:15
 */
@Aspect
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(com.zhou.annotation.DataSourceType)")
    public void pointCut() {

    }

    @Around("pointCut() && @annotation(dataSourceType)")
    public Object selectDataSource(ProceedingJoinPoint joinPoint, DataSourceType dataSourceType) throws Throwable {
        if (!StringUtils.isBlank(dataSourceType.dataSourceName())) {
            // 将要切换的数据源名称存入上下文中
            DataSourceContextHolder.setDatasource(dataSourceType.dataSourceName());
        }
        try {
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDatasource();
        }
    }

}