package com.qww.mongologger.core.annotation;

import com.qww.mongologger.core.config.MongoLoggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MongoLoggerAutoConfiguration.class})
public @interface EnableMongoLogger {
}
