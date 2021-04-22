package com.qww.mongologger.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MLog {
    LogType type() default LogType.WEB;
}
