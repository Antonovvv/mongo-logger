package com.qww.mongologger.core.annotation;

import com.qww.mongologger.core.entity.BaseLog;
import com.qww.mongologger.core.entity.ExecLog;
import com.qww.mongologger.core.entity.TestLog;
import com.qww.mongologger.core.entity.WebLog;
import com.qww.mongologger.core.utils.exceptions.LogTypeNotFoundException;

public enum LogType {
    /**
     * 基础日志
     */
    BASE(BaseLog.class),
    /**
     * 方法执行日志
     */
    EXEC(ExecLog.class),
    /**
     * 请求级日志
     */
    WEB(WebLog.class),
    /**
     * 测试日志
     */
    TEST(TestLog.class),
    /**
     * 自定义日志类
     */
    CUSTOM();

    private Class<?> logClass;

    LogType() {}

    LogType(Class<?> logClass) {
        this.logClass = logClass;
    }

    public Class<?> getLogClass() {
        return logClass;
    }

    public static LogType getTypeByClass(Class<?> logClass) throws LogTypeNotFoundException {
        for (LogType logType : LogType.values()) {
            if (logType.getLogClass().equals(logClass)) {
                return logType;
            }
        }
        throw new LogTypeNotFoundException();
    }

    public Boolean isAbove(LogType target) {
        return this.ordinal() >= target.ordinal();
    }

    public Boolean isBelow(LogType target) {
        return this.ordinal() <= target.ordinal();
    }
}
