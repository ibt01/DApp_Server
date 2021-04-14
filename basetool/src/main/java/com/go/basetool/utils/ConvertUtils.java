package com.go.basetool.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangcongzhen
 * @Description:
 * @date 2018/11/8
 */

public abstract class ConvertUtils {

    /**
     * 复制相同字段给另一个类
     *
     * @param entity
     * @param resultClassType
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R copySameProperty(T entity, Class<R> resultClassType) {
        try {
            return copySamePropertyThrows(entity, resultClassType);
        } catch (Exception e) {
            throw new RuntimeException("转换异常" + e.getMessage());
        }
    }


    /**
     * 复制相同字段给另一个类
     *
     * @param entity
     * @param resultClassType
     * @param <T>
     * @param <R>
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, R> R copySamePropertyThrows(T entity, Class<R> resultClassType) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (entity == null || resultClassType == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        //读取属性组
        BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor p : propertyDescriptors) {
            Method readMethod = p.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            Object value = readMethod.invoke(entity);
            if (value == null) {
                continue;
            }
            result.put(p.getName(), value);
        }
        R resultEntity = resultClassType.newInstance();
        //读取属性组
        BeanInfo resultBean = Introspector.getBeanInfo(resultClassType);
        PropertyDescriptor[] resultBeanPD = resultBean.getPropertyDescriptors();
        for (PropertyDescriptor p : resultBeanPD) {
            Object value = result.get(p.getName());
            if (value == null || p.getWriteMethod() == null) {
                continue;
            }
            p.getWriteMethod().invoke(resultEntity, value);
        }
        return resultEntity;
    }
}
