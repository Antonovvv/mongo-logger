package com.qww.mongologger.core.utils;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

public class ReflectUtil {
    public static boolean hasBeenAnnotated(AnnotatedElement element, Class<? extends Annotation> annotationClass) {
        if (element.isAnnotationPresent(annotationClass)) {
            return true;
        } else {
            Annotation[] annotations = element.getDeclaredAnnotations();
            if (annotations.length == 0) return false;
            for (Annotation annotation : annotations) {
                if (isMetaAnnotation(annotation)) continue;
                if (hasBeenAnnotated(annotation.annotationType(), annotationClass)) return true;
            }
            return false;
        }
    }

    public static boolean isMetaAnnotation(Annotation annotation) {
        return annotation.annotationType().equals(Target.class)
                || annotation.annotationType().equals(Retention.class)
                || annotation.annotationType().equals(Documented.class)
                || annotation.annotationType().equals(Inherited.class)
                || annotation.annotationType().equals(Native.class)
                || annotation.annotationType().equals(Repeatable.class);
    }
}
