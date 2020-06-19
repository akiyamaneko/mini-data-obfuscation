package com.github.echohlne.dataobfuscation.utils;



import com.github.echohlne.dataobfuscation.exception.CopyException;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("All")
public final class CloneUtils {

    private CloneUtils() {

    }

    public static <T> T deepClone(T original) {
        Set<T> deepCopyObjectSet = new HashSet<>();
        Supplier<Stream<T>> streamSupplier = () -> (Stream<T>) deepCopyObjectSet.stream();
        boolean copyObject = false;

        if (ClassesUtils.isNull(original)) {
            throw new CopyException("Original object must be not null for Deep Copy");
        }

        if (isObjectPrimitiveOrString(getObjectClass(original.getClass().getName()))) {
            return original;
        }

        if (!(deepCopyObjectSet.isEmpty())) {
            copyObject = streamSupplier.get().anyMatch(original::equals);
        }

        if (copyObject) {
            return streamSupplier.get().filter(original::equals).findAny().get();
        } else {
            if (isObjectArrayOrCollection(getObjectClass(original.getClass().getName()))) {
                return getCloneObjectArrayOrCollectionType(original);
            }
        }

        Field[] originalObjectFields = original.getClass().getDeclaredFields();
        T deepCopyObjectWithFields = createNewObject(original);

        Arrays.stream(originalObjectFields).forEach(field -> {
            field.setAccessible(true);
            if (!isNull(deepCopyObjectWithFields)) {
                if (isNull(getFieldValue(field, original))) {
                    insertValue(deepCopyObjectWithFields, field, null);
                } else if (isObjectPrimitiveOrString(getObjectClass(field.getType().getName()))
                        || field.getType().isEnum()) {
                    insertValue(deepCopyObjectWithFields, field, getFieldValue(field, original));
                } else if (field.getType().isArray()) {
                    insertValue(deepCopyObjectWithFields, field,
                            getCloneObjectOfArrayType(getFieldValue(field, original)));
                } else {
                    insertValue(deepCopyObjectWithFields, field, deepClone(getFieldValue(field, original)));
                }
            }
        });

        deepCopyObjectSet.add(deepCopyObjectWithFields);
        return deepCopyObjectWithFields;
    }

    private static <T> T getCloneObjectArrayOrCollectionType(T original) {
        if (original.getClass().isArray()) {
            return getCloneObjectOfArrayType(original);
        } else if (Collection.class.isAssignableFrom(original.getClass())) {
            return (T) getCloneObjectOfCollectionType(original);
        } else if (Map.class.isAssignableFrom(original.getClass())) {
            return (T) getCloneObjectOfMapType(original);
        }
        throw new CopyException("Error! Collection " + original.getClass() + " can not copy");
    }

    private static <V, K, T> Map<K, V> getCloneObjectOfMapType(T original) {
        Map<K, V> originalMap = (Map<K, V>) original;
        Map<K, V> copyMap = createNewObject((Map<K, V>) original);

        originalMap.values().forEach(
                mapValue -> {
                    assert !isNull(copyMap);
                    copyMap.put(getCloneOfMapKey(Objects.requireNonNull(getKeyFromValue(originalMap,
                            mapValue))), getCloneOfMapValue(mapValue));
                }
        );

        return copyMap;
    }

    private static <T> T getCloneObjectOfArrayType(T original) {
        int arrayLength = Array.getLength(original);
        String theClassName = original.getClass().getComponentType().getTypeName();
        Class<?> arrayClass = getObjectClass(theClassName);

        T copyObject = (T) Array.newInstance(arrayClass, arrayLength);

        if (isObjectPrimitiveOrString(original.getClass())) {
            System.arraycopy(original, 0, copyObject, 0, arrayLength);
        } else {
            for (int index = 0; index < arrayLength; index++) {
                Array.set(copyObject, index, deepClone(Array.get(original, index)));
            }
        }

        return copyObject;
    }

    private static <T> Collection<T> getCloneObjectOfCollectionType(T original) {
        Collection<T> subObject = (Collection<T>) original;
        Collection<T> copyCollection = createNewObject(subObject);

        if (isObjectPrimitiveOrString(original.getClass())) {
            assert !isNull(copyCollection);
            copyCollection.addAll(subObject);
        } else {
            for (T itemValue : subObject) {
                copyCollection.add(deepClone(itemValue));
            }
        }
        return copyCollection;
    }

    private static <K> K getCloneOfMapKey(K mapKey) {
        K copyMapKey;

        if (isObjectPrimitiveOrString(mapKey.getClass())) {
            copyMapKey = mapKey;
        } else if (isObjectArrayOrCollection(mapKey.getClass())) {
            copyMapKey = getCloneObjectArrayOrCollectionType(mapKey);
        } else {
            copyMapKey = deepClone(mapKey);
        }

        return copyMapKey;
    }

    private static <V> V getCloneOfMapValue(V mapValue) {
        V copyMapValue;

        if (isObjectPrimitiveOrString(mapValue.getClass())) {
            copyMapValue = mapValue;
        } else if (isObjectArrayOrCollection(mapValue.getClass())) {
            copyMapValue = getCloneObjectArrayOrCollectionType(mapValue);
        } else {
            copyMapValue = deepClone(mapValue);
        }

        return copyMapValue;
    }

    private static <K, V> K getKeyFromValue(Map<K, V> originalMap, V value) {
        for (K mapKey : originalMap.keySet()) {
            if (originalMap.get(mapKey).equals(value)) {
                return mapKey;
            }
        }
        throw new CopyException("Error while Map get key from value for copy");
    }

    private static <T> T createNewObject(T original) {
        Constructor<?>[] declaredConstructors = original.getClass().getDeclaredConstructors();
        try {
            if (hasDefaultConstructor(declaredConstructors)) {
                return createNewObjectWithDefaultConstructor(original);
            } else {
                return createNewObjectWithOutDefaultConstructor(original, declaredConstructors[0]);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException
                | NoSuchMethodException e) {
            throw new CopyException("Error while create new instance for copy", e);
        }
    }

    private static <T> T createNewObjectWithDefaultConstructor(T original) throws IllegalAccessException,
            InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<T> declaredConstructor = (Constructor<T>) original.getClass().getDeclaredConstructor();
        declaredConstructor.setAccessible(true);

        return declaredConstructor.newInstance();
    }

    private static <T> T createNewObjectWithOutDefaultConstructor(T original, Constructor<?> objectConstructor)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] objectFields = original.getClass().getDeclaredFields();
        List<Object> fieldsValue = new ArrayList<>();

        for (Field field : objectFields) {
            field.setAccessible(true);
            if (field.getType().isPrimitive()) {
                fieldsValue.add(0);
            } else {
                fieldsValue.add(null);
            }
        }

        return (T) objectConstructor.newInstance(fieldsValue.toArray());
    }

    private static void insertValue(Object clone, Field cloningField, Object copySubObject) {
        try {
            Field deepCopyObjectField = clone.getClass().getDeclaredField(cloningField.getName());
            deepCopyObjectField.setAccessible(true);

            deepCopyObjectField.set(clone, copySubObject);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CopyException("Error while insert the copy value to field for copy", e);
        }
    }

    private static Object getFieldValue(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new CopyException("Error while getting the value of field for copy", e);
        }
    }

    private static Class<?> getObjectClass(String className) {
        try {
            if ("byte".equals(className) || "java.lang.Byte".equals(className)) {
                return byte.class;
            } else if ("short".equals(className) || "java.lang.Short".equals(className)) {
                return short.class;
            } else if ("int".equals(className) || "java.lang.Integer".equals(className)) {
                return int.class;
            } else if ("long".equals(className) || "java.lang.Long".equals(className)) {
                return long.class;
            } else if ("double".equals(className) || "java.lang.Double".equals(className)) {
                return double.class;
            } else if ("float".equals(className) || "java.lang.Float".equals(className)) {
                return float.class;
            } else if ("char".equals(className) || "java.lang.Character".equals(className)) {
                return char.class;
            } else {
                return Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            throw new CopyException("Error while getting the instance class name for copy", e);
        }
    }

    private static boolean isNull(Object object) {
        return object == null;
    }

    private static boolean isObjectPrimitiveOrString(Class<?> originalObjectClass) {
        if (!originalObjectClass.isArray()) {
            return originalObjectClass.isPrimitive() || "String".equals(originalObjectClass.getSimpleName());
        }

        return false;
    }

    private static boolean isObjectArrayOrCollection(Class<?> originalObjectClass) {
        return originalObjectClass.isArray() || Map.class.isAssignableFrom(originalObjectClass) ||
                Collection.class.isAssignableFrom(originalObjectClass);
    }

    private static boolean hasDefaultConstructor(Constructor[] constructors) {
        for (Constructor constructor : constructors) {
            if (constructor.getGenericParameterTypes().length == 0) {
                return true;
            }
        }

        return false;
    }
}