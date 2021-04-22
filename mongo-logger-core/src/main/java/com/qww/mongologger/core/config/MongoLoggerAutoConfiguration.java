package com.qww.mongologger.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(MongoLoggerOptionProperties.class)
@Import({MongoLoggerDependentConfiguration.class})
@ComponentScan("com.qww.mongologger.core")
public class MongoLoggerAutoConfiguration {

}
