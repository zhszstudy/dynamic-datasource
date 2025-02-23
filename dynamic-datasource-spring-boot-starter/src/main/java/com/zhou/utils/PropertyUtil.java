package com.zhou.utils;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.NoSuchElementException;

/**
 * @author zhou22
 * @desc 属性操作工具类
 * @Date 2025-02-19 10:38:14
 */
public class PropertyUtil {

    /**
     * 如果没有配置的信息，则抛出异常
     *
     * @param environment
     * @param name
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T convertToTarget(Environment environment, String name, Class<T> clz) {
        try {
            return Binder.get(environment).bind(name, clz).get();
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 如果没有配置信息，返回一个空的对象
     *
     * @param environment
     * @param name
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T convertToTargetIfAbsent(Environment environment, String name, Class<T> clz) {
        return Binder.get(environment).bindOrCreate(name, clz);
    }
}