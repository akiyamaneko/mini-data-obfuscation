package com.github.echohlne.dataobfuscation.api;


import com.github.echohlne.dataobfuscation.exception.AnnotationIncompleteException;
import com.github.echohlne.dataobfuscation.meta.annotation.Rule;
import com.github.echohlne.dataobfuscation.meta.annotation.Strategy;
import com.github.echohlne.dataobfuscation.meta.interfaces.IRule;
import com.github.echohlne.dataobfuscation.meta.interfaces.IStrategy;
import com.github.echohlne.dataobfuscation.utils.ClassesUtils;
import com.github.echohlne.dataobfuscation.utils.CloneUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public final class DataObfuscation {

    private DataObfuscation() {
    }

    public static <T> T obfuscateData(T originalObject) {
        T deepCopyObject = CloneUtils.deepClone(originalObject);

        obfuscateByClassType(deepCopyObject);

        return deepCopyObject;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void obfuscateByClassType(Object dst) {
        Class<?> dstClass = dst.getClass();

        if (!ClassesUtils.isPrimitiveType(dstClass)) {
            if (ClassesUtils.isJavaBean(dstClass)) {
                List<Field> fieldList = ClassesUtils.getFieldList(dstClass);
                for (Field field : fieldList) {
                    dealWithCustomAnnotation(field, dst, null, null);
                }
            } else if (ClassesUtils.isCollection(dstClass)) {
                final Collection<Object> entryCollection = (Collection<Object>) dst;
                if (ClassesUtils.isCollectionNotEmpty(entryCollection)) {
                    Object firstCollectionEntry = entryCollection.iterator().next();
                    Class collectionEntryClass = firstCollectionEntry.getClass();

                    if (ClassesUtils.isClassTypeShouldProcessAgain(collectionEntryClass)) {
                        for (Object collectionEntry : entryCollection) {
                            obfuscateByClassType(collectionEntry);
                        }
                    }
                }
            } else if (ClassesUtils.isArray(dst)) {
                Object[] arrays = (Object[]) dst;
                if (ClassesUtils.isArrayNotEmpty(arrays)) {
                    Object firstArrayEntry = arrays[0];
                    final Class entryFieldClass = firstArrayEntry.getClass();

                    if (ClassesUtils.isClassTypeShouldProcessAgain(entryFieldClass)) {
                        for (Object arrayEntry : arrays) {
                            obfuscateByClassType(arrayEntry);
                        }
                    }
                }
            }
        }


    }

    private static void obfuscateByClassType(Object dst, Rule parentRule, Strategy parentMask) {
        List<Field> fieldList = ClassesUtils.getFieldList(dst.getClass());
        for (Field field : fieldList) {
            dealWithCustomAnnotation(field, dst, parentRule, parentMask);
        }
    }

    private static Rule getCurrentRule(Field field, Rule parentRule) {
        Rule currentRule = field.getAnnotation(Rule.class);
        return ClassesUtils.isNull(currentRule) ? parentRule : currentRule;
    }

    private static Strategy getCurrentMask(Field field, Strategy parentMask) {
        Strategy currentMask = field.getAnnotation(Strategy.class);
        return ClassesUtils.isNull(currentMask) ? parentMask : currentMask;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void dealWithCustomAnnotation(Field field, Object dst, Rule parentRule, Strategy parentMask) {
        try {
            Rule currentRule = getCurrentRule(field, parentRule);
            Strategy currentStrategy = getCurrentMask(field, parentMask);

            if (ClassesUtils.isIllegalRuleAndStrategy(currentStrategy, currentRule)) {
                throw new AnnotationIncompleteException("Policy Annotation requires Desensitize Annotation.");
            }

            Object fieldValue = field.get(dst);
            if (String.class.isAssignableFrom(field.getType())) {
                if (ClassesUtils.nonNull(currentStrategy)) {
                    Class<? extends IStrategy> strategy = currentStrategy.type();
                    if (ClassesUtils.nonNull(currentRule)) {
                        Class<? extends IRule> rule = currentRule.accept();
                        if (rule.newInstance().accept(fieldValue)) {
                            field.set(dst, strategy.newInstance().mask(fieldValue));
                        }
                    } else {
                        field.set(dst, strategy.newInstance().mask(fieldValue));
                    }
                }

            } else if (ClassesUtils.isJavaBean(field.getType())) {
                final Object fieldNewObject = field.get(dst);
                obfuscateByClassType(fieldNewObject, currentRule, currentStrategy);
            } else if (fieldValue instanceof Collection) {
                final Collection<Object> entryCollection = (Collection<Object>) field.get(dst);
                if (!entryCollection.isEmpty()) {
                    Object firstCollectionEntry = entryCollection.iterator().next();
                    Class collectionEntryClass = firstCollectionEntry.getClass();

                    if (ClassesUtils.isClassTypeShouldProcessAgain(collectionEntryClass)) {
                        for (Object collectionEntry : entryCollection) {
                            obfuscateByClassType(collectionEntry, currentRule, currentStrategy);
                        }
                    } else {
                        List<Object> newResultList = new ArrayList<>(entryCollection.size());
                        for (Object entry : entryCollection) {
                            Object result = getDataAfterObfuscate(entry, field, currentRule, currentStrategy);
                            newResultList.add(result);
                        }
                        field.set(dst, newResultList);
                    }
                }

            } else if (fieldValue instanceof Object[]) {
                if (ClassesUtils.nonNull(currentStrategy)) {
                    Object[] arrays = (Object[]) field.get(dst);
                    if (ClassesUtils.isArrayNotEmpty(arrays)) {
                        Object firstArrayEntry = arrays[0];
                        final Class entryFieldClass = firstArrayEntry.getClass();

                        if (ClassesUtils.isClassTypeShouldProcessAgain(entryFieldClass)) {
                            for (Object arrayEntry : arrays) {
                                obfuscateByClassType(arrayEntry, currentRule, currentStrategy);
                            }
                        } else {
                            final int arrayLength = arrays.length;
                            Object newArray = Array.newInstance(entryFieldClass, arrayLength);
                            for (int i = 0; i < arrayLength; i++) {
                                Object entry = arrays[i];
                                Object result = getDataAfterObfuscate(entry, field, currentRule, currentStrategy);
                                Array.set(newArray, i, result);
                            }
                            field.set(dst, newArray);
                        }
                    }
                }

            } else {
                updateSingleField(dst, field);
            }

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static Object getDataAfterObfuscate(final Object entry, final Field field, Rule parentRule, Strategy parentMask) {
        Rule currentRule = getCurrentRule(field, parentRule);
        Strategy currentStrategy = getCurrentMask(field, parentMask);

        if (ClassesUtils.isIllegalRuleAndStrategy(currentStrategy, currentRule)) {
            throw new AnnotationIncompleteException("Policy Annotation requires Desensitize Annotation.");
        }

        try {
            if (ClassesUtils.nonNull(currentStrategy)) {
                Class<? extends IStrategy> strategy = currentStrategy.type();
                if (ClassesUtils.nonNull(currentRule)) {
                    Class<? extends IRule> rule = currentRule.accept();
                    if (rule.newInstance().accept(entry)) {
                        return strategy.newInstance().mask(entry);
                    }
                } else {
                    return strategy.newInstance().mask(entry);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AnnotationIncompleteException(e);
        }
        return entry;
    }


    private static void updateSingleField(final Object dst, final Field field) {
        if (ClassesUtils.isPrimitiveType(field)) {
            return;
        }
        Rule currentRule = field.getAnnotation(Rule.class);
        Strategy currentStrategy = field.getAnnotation(Strategy.class);

        if (ClassesUtils.isIllegalRuleAndStrategy(currentStrategy, currentRule)) {
            throw new AnnotationIncompleteException("Policy Annotation requires Desensitize Annotation.");
        }

        try {
            if (ClassesUtils.nonNull(currentStrategy)) {
                Object fieldValue = field.get(dst);
                Class<? extends IStrategy> strategy = currentStrategy.type();
                if (ClassesUtils.nonNull(currentRule)) {
                    Class<? extends IRule> rule = currentRule.accept();
                    if (rule.newInstance().accept(fieldValue)) {
                        field.set(dst, strategy.newInstance().mask(fieldValue));
                    }
                } else {
                    field.set(dst, strategy.newInstance().mask(fieldValue));
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AnnotationIncompleteException(e);
        }

    }
}
