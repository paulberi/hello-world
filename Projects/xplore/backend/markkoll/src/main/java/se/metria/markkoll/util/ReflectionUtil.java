package se.metria.markkoll.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionUtil {
    public static <A extends Annotation> List<FieldValuePair>
    getFieldValuesWithAnnotationRecursive(Object object, Class<A> annotationClass) throws IllegalAccessException {
        var addedNonPrimitiveFields = new HashSet<Field>();

        return getFieldValuesWithAnnotationRecursive(object, annotationClass, addedNonPrimitiveFields);
    }

    public static <A extends Annotation> List<Field>
    getFieldsWithAnnotationRecursive(Class<?> clazz, Class<A> annotationClass)
    {
        var addedNonPrimitiveFields = new HashSet<Field>();

        return getFieldsWithAnnotationRecursive(clazz, annotationClass, addedNonPrimitiveFields);
    }

    private static <A extends Annotation> List<Field>
    getFieldsWithAnnotationRecursive(Class<?> clazz, Class<A> annotationClass, Set<Field> addedNonPrimitiveFields)
    {
        var declaredFields = clazz.getDeclaredFields();
        var fieldsOut = new ArrayList<Field>();
        for (var df: declaredFields) {
            if (df.isAnnotationPresent(annotationClass)) {
                fieldsOut.add(df);
            }
            else if (!isPrimitive(df) && !addedNonPrimitiveFields.contains(df)) {
                addedNonPrimitiveFields.add(df);
                df.setAccessible(true);
                fieldsOut.addAll(getFieldsWithAnnotationRecursive(df.getType(), annotationClass, addedNonPrimitiveFields));
            }
        }

        return fieldsOut;
    }

    private static <A extends Annotation> List<FieldValuePair>
    getFieldValuesWithAnnotationRecursive(Object object, Class<A> annotationClass, Set<Field> addedNonPrimitiveFields)
        throws IllegalAccessException
    {
        var declaredFields = object.getClass().getDeclaredFields();
        var fvPair = new ArrayList<FieldValuePair>();
        for (var df: declaredFields) {
            if (df.isAnnotationPresent(annotationClass)) {
                fvPair.add(new FieldValuePair(df, object));
            }
            else if (!isPrimitive(df) && !addedNonPrimitiveFields.contains(df)) {
                addedNonPrimitiveFields.add(df);
                df.setAccessible(true);
                fvPair.addAll(getFieldValuesWithAnnotationRecursive(df.get(object), annotationClass, addedNonPrimitiveFields));
            }
        }
        return fvPair;
    }

    private static Boolean isPrimitive(Field field) {
        var clazz = field.getType();

        return ClassUtils.isPrimitiveOrWrapper(clazz) || String.class.isAssignableFrom(clazz);
    }

    @Data
    @AllArgsConstructor
    public static class FieldValuePair {
        private Field field;
        private Object value;
    }
}
