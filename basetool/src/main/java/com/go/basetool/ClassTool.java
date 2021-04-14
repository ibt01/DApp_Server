package com.go.basetool;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class ClassTool {
    public static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = o.getClass().getMethod(getter, new Class[]{});
        Object value = method.invoke(o, new Object[]{});
        return value;
    }
}
