package com.qww.mongologger.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MLog {
    /**
     * 日志的类型{@link LogType}
     */
    LogType type() default LogType.BASE;

    /**
     * 日志存储的集合名称
     */
    String collectionName() default "";
}
