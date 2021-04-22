package com.qww.mongologger.core.aspect;

import com.qww.mongologger.core.MongoLoggableComponent;
import org.aspectj.lang.annotation.DeclareParents;

public class MongoLogAspect {
    // @DeclareParents(value = "")
    public MongoLoggableComponent mongoLoggableComponent;
}
