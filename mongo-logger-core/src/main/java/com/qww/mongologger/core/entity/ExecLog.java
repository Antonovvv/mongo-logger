package com.qww.mongologger.core.entity;

import com.qww.mongologger.core.entity.interfaces.ExecLogInterface;
import com.qww.mongologger.core.utils.JSONUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class ExecLog extends BaseLog implements ExecLogInterface {
    private String className;
    private String methodName;
    private List<MethodArg> methodArgs = new ArrayList<>();
    private MethodReturn methodReturn;
    private MethodError methodError;
    private long execTime;

    public ExecLog() {}

    public void addMethodArg(String name, Class<?> type, Object value) {
        MethodArg arg = new MethodArg(name, type.getName(), JSONUtil.stringify(value));
        this.methodArgs.add(arg);
    }

    public void setMethodReturn(Class<?> type, Object value) {
        this.methodReturn = new MethodReturn(type.getName(), JSONUtil.stringify(value));
    }

    public void setMethodError(Throwable throwing) {
        this.methodError = new MethodError(throwing.getClass().getName(), throwing.getMessage());
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public List<MethodArg> getMethodArgs() {
        return this.methodArgs;
    }

    public MethodReturn getMethodReturn() {
        return this.methodReturn;
    }

    public MethodError getMethodError() {
        return this.methodError;
    }

    public long getExecTime() {
        return this.execTime;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

//    @Override
//    public String toString() {
//        return "ExecLog{" +
//                "className='" + className + '\'' +
//                ", methodName='" + methodName + '\'' +
//                ", methodArgs=" + methodArgs +
//                ", methodReturn=" + methodReturn +
//                ", methodError=" + methodError +
//                ", execTime=" + execTime +
//                "} " + super.toString();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecLog)) return false;
        if (!super.equals(o)) return false;
        ExecLog execLog = (ExecLog) o;
        return getExecTime() == execLog.getExecTime() &&
                Objects.equals(getClassName(), execLog.getClassName()) &&
                Objects.equals(getMethodName(), execLog.getMethodName()) &&
                Objects.equals(getMethodArgs(), execLog.getMethodArgs()) &&
                Objects.equals(getMethodReturn(), execLog.getMethodReturn()) &&
                Objects.equals(getMethodError(), execLog.getMethodError());
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), getClassName(), getMethodName(), getMethodArgs(), getMethodReturn(), getMethodError(), getExecTime());
//    }
}

class MethodArg {
    private String argName;
    private String argType;
    private String argValue;

    public MethodArg(String argName, String argType, String argValue) {
        this.argName = argName;
        this.argType = argType;
        this.argValue = argValue;
    }

    public String getArgName() {
        return this.argName;
    }

    public String getArgType() {
        return this.argType;
    }

    public String getArgValue() {
        return this.argValue;
    }

    @Override
    public String toString() {
        return "MethodArg{" +
                "argName='" + argName + '\'' +
                ", argType='" + argType + '\'' +
                ", argValue='" + argValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodArg)) return false;
        MethodArg methodArg = (MethodArg) o;
        return Objects.equals(getArgName(), methodArg.getArgName()) &&
                Objects.equals(getArgType(), methodArg.getArgType()) &&
                Objects.equals(getArgValue(), methodArg.getArgValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArgName(), getArgType(), getArgValue());
    }
}

class MethodReturn {
    private String returnType;
    private String returnValue;

    public MethodReturn(String returnType, String returnValue) {
        this.returnType = returnType;
        this.returnValue = returnValue;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public String getReturnValue() {
        return this.returnValue;
    }

    @Override
    public String toString() {
        return "MethodReturn{" +
                "returnType='" + returnType + '\'' +
                ", returnValue='" + returnValue + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodReturn)) return false;
        MethodReturn that = (MethodReturn) o;
        return Objects.equals(getReturnType(), that.getReturnType()) &&
                Objects.equals(getReturnValue(), that.getReturnValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReturnType(), getReturnValue());
    }
}

class MethodError {
    private String errorType;
    private String message;

    public MethodError(String errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "MethodError{" +
                "errorType='" + errorType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodError)) return false;
        MethodError that = (MethodError) o;
        return Objects.equals(getErrorType(), that.getErrorType()) &&
                Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorType(), getMessage());
    }
}
