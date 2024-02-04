package com.kemp.demo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectUtil {
    private static final Map<String, Method> sMethods = new ConcurrentHashMap<String, Method>();
    private static final Map<String, Field> sFields = new ConcurrentHashMap<String, Field>();

    public static Method getMethod(Class clazz, String methodName) throws NoSuchMethodException {
        return doGetMethod(clazz.getName(), clazz, methodName, null);
    }

    public static Method getMethod(Class clazz, String methodName, Class... cls) throws NoSuchMethodException {
        return doGetMethod(clazz.getName(), clazz, methodName, cls);
    }

    private static Method doGetMethod(final String clazzName, Class<?> clazz, String methodName,
                                      Class[] cls) throws NoSuchMethodException {
        String key = clazz.getName().concat(".").concat(methodName);
        if (null != cls && cls.length > 0) {
            key = key.concat("(");
            for (Class c : cls) {
                key = key.concat(c.getName()).concat(",");
            }
            key = key.substring(0, key.length() - 1).concat(")");
        }
        Method method = null;
        try {
            if (sMethods.containsKey(key)) {
                method = sMethods.get(key);
            }
        } catch (Throwable e) {//NOPMD
        }
        if (null == method) {
            try {
                if (null == cls) {
                    method = clazz.getDeclaredMethod(methodName);
                } else {
                    method = clazz.getDeclaredMethod(methodName, cls);
                }
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
                if (null != clazz) {
                    return doGetMethod(clazzName, clazz, methodName, cls);
                } else {
                    throw new NoSuchMethodException(clazzName + "." + methodName);
                }
            }
            method.setAccessible(true);
            sMethods.put(key, method);
        }
        return method;
    }

    public static Object invokeMethod(Object object, String methodName) throws Exception {
        Class clazz = object.getClass();
        return invokeMethod(clazz, object, methodName, null, null);
    }

    public static Object invokeMethod(Object object, String methodName, Class[] cls,
                                      Object[] args) throws Exception {
        Class clazz = object.getClass();
        return invokeMethod(clazz, object, methodName, cls, args);
    }

    public static Object invokeMethod(String className, String methodName) throws Exception {
        Class clazz = Class.forName(className);
        return invokeMethod(clazz, null, methodName, null, null);
    }

    public static Object invokeMethod(String className, String methodName, Class[] cls,
                                      Object[] args) throws Exception {
        Class clazz = Class.forName(className);
        return invokeMethod(clazz, null, methodName, cls, args);
    }

    public static Object invokeMethod(Class clazz, String methodName) throws Exception {
        return invokeMethod(clazz, null, methodName, null, null);
    }

    public static Object invokeMethod(Class clazz, String methodName, Class[] cls,
                                      Object[] args) throws Exception {
        return invokeMethod(clazz, null, methodName, cls, args);
    }

    public static Object invokeMethod(Class clazz, Object object, String methodName, Class[] cls,
                                      Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return doInvokeMethod(clazz.getName(), clazz, object, methodName, cls, args);
    }

    private static Object doInvokeMethod(final String clazzName, Class clazz, Object object, String methodName, Class[] cls,
                                         Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = doGetMethod(clazzName, clazz, methodName, cls);
        if (null == args) {
            return method.invoke(object);
        } else {
            return method.invoke(object, args);
        }
    }

    /**
     * Have better using setFieldValue(Class<?> clazz, Object object, String fieldName, Object value)
     *
     * @param object    Object
     * @param fieldName Filed Name
     * @param value     Value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        setFieldValue(object.getClass(), object, fieldName, value);
    }

    public static void setFieldValue(Class<?> clazz, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        setFieldValue(clazz, null, fieldName, value);
    }

    public static void setFieldValue(Class<?> clazz, Object object, String fieldName,
                                     Object value) throws NoSuchFieldException, IllegalAccessException {
        doGetField(clazz.getName(), clazz, fieldName).set(object, value);
    }

    /**
     * Have better using getFieldValue(Class<?> clazz, Object object, String fieldName)
     *
     * @param object    // Object
     * @param fieldName // Filed Name
     * @return // Field Object
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        return getFieldValue(object.getClass(), object, fieldName);
    }

    public static Object getFieldValue(Class<?> clazz, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        return getFieldValue(clazz, null, fieldName);
    }

    public static Object getFieldValue(Class<?> clazz, Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        return doGetField(clazz.getName(), clazz, fieldName).get(object);
    }

    public static Field getFiled(Object object, String fieldName) throws NoSuchFieldException {
        return getFiled(object.getClass(), fieldName);
    }

    public static Field getFiled(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return doGetField(clazz.getName(), clazz, fieldName);
    }

    private static Field doGetField(final String clazzName, Class<?> clazz, String fieldName)
            throws NoSuchFieldException {
        String key = clazz.getName().concat(".").concat(fieldName);
        Field field = null;
        try {
            if (sFields.containsKey(key)) {
                field = sFields.get(key);
            }
        } catch (Throwable e) {//NOPMD
        }
        if (null == field) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
                if (null != clazz) {
                    return doGetField(clazzName, clazz, fieldName);
                } else {
                    throw new NoSuchFieldException(clazzName + "." + fieldName);
                }
            }
            field.setAccessible(true);
            sFields.put(key, field);
        }
        return field;
    }
}
