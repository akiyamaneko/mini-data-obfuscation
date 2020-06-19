package com.github.echohlne.dataobfuscation.utils;

import com.github.echohlne.dataobfuscation.meta.annotation.Rule;
import com.github.echohlne.dataobfuscation.meta.annotation.Strategy;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ClassesUtils {
    private ClassesUtils() {

    }

    public static boolean isJavaBean(Class<?> currentClass) {
        return null != currentClass
                && !currentClass.isInterface()
                && !Modifier.isAbstract(currentClass.getModifiers())
                && !currentClass.isEnum()
                && !ClassUtils.isAssignable(currentClass, Array.class)
                && !currentClass.isAnnotation()
                && !currentClass.isSynthetic()
                && !currentClass.isPrimitive()
                && ! ClassUtils.isAssignable(currentClass, Iterable.class)
                && ! ClassUtils.isAssignable(currentClass, Map.class);
    }

    @SuppressWarnings({"rawtypes"})
    private static boolean shouldHandleAgain(final Class fieldTypeClass) {
        if(ClassUtils.isPrimitiveOrWrapper(fieldTypeClass) || ClassUtils.isAssignable(fieldTypeClass, Map.class)) {
            return false;
        }
        return isJavaBean(fieldTypeClass) ||  ClassUtils.isAssignable(fieldTypeClass, Array.class) ||
                ClassUtils.isAssignable(fieldTypeClass, Collection.class);
    }
//
//    @SuppressWarnings({"rawtypes"})
    public static boolean isClassTypeShouldProcessAgain(final Class fieldTypeClass) {
        if(ClassUtils.isPrimitiveOrWrapper(fieldTypeClass) || ClassUtils.isAssignable(fieldTypeClass, Map.class) || ClassUtils.isAssignable(fieldTypeClass, String.class)) {
            return false;
        }
        return isJavaBean(fieldTypeClass) ||  ClassUtils.isAssignable(fieldTypeClass, Array.class) ||
                ClassUtils.isAssignable(fieldTypeClass, Collection.class);
    }

    /**
     * 常见的基础对象类型
     */
    private static final Class[] BASE_TYPE_CLASS = new Class[]{
            String.class, Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class, Object.class, Class.class
    };

    public static boolean isBase(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        for (Class baseClazz : BASE_TYPE_CLASS) {
            if (baseClazz.equals(clazz)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isMap(final Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }



    public static boolean isPrimitiveType(Field field) {
        return ClassUtils.isPrimitiveOrWrapper(field.getType());
    }

    public static boolean isPrimitiveType(Class<?> dstClass) {
        return ClassUtils.isPrimitiveOrWrapper(dstClass);
    }

    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static boolean nonNull(Object object) {
        return Objects.nonNull(object);
    }

    @SuppressWarnings({"rawtypes"})
    public static boolean isCollectionNotEmpty(Collection collection) {
        return !collection.isEmpty();
    }

    public static boolean isArrayNotEmpty(Object[] array) {
        return array.length > 0;
    }

    public static boolean isIllegalRuleAndStrategy(Strategy currentStrategy, Rule currentRule) {
        return isNull(currentStrategy) && nonNull(currentRule);
    }

    public static boolean isCollection(Class<?> releaseClass) {
        return ClassUtils.isAssignable(releaseClass, Collection.class);
    }

    public static boolean isArray(Object object) {
        return object instanceof Object[];
    }

    @SuppressWarnings({"rawtypes"})
    public static List<Field> getFieldList(Class className) {
        List<Field> fieldList = new ArrayList<>();
        Class curClass = className;
        while (curClass != null) {

            fieldList.addAll(Lists.newArrayList(curClass.getDeclaredFields()));
            curClass = curClass.getSuperclass();
        }

        for (Field field : fieldList) {
            field.setAccessible(true);
        }
        return fieldList;
    }


    public static String getObjectStr(Object src) {
        return src == null ? null : src.toString();
    }

}
