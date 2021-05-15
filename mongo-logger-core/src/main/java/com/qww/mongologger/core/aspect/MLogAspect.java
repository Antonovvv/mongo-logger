package com.qww.mongologger.core.aspect;

import com.qww.mongologger.core.MongoLogger;
import com.qww.mongologger.core.MongoLoggerFactory;
import com.qww.mongologger.core.annotation.LogType;
import com.qww.mongologger.core.annotation.MLog;
import com.qww.mongologger.core.entity.BaseLog;
import com.qww.mongologger.core.entity.ExecLog;
import com.qww.mongologger.core.entity.WebLog;
import com.qww.mongologger.core.utils.ReflectUtil;
import com.qww.mongologger.core.utils.exceptions.MethodArgumentMismatchException;
import com.qww.mongologger.core.utils.exceptions.NullMLoggerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.mongodb.core.MongoTemplate;

@Aspect
@Component
public class MLogAspect {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MLogAspect.class);
    private ThreadLocal<BaseLog> logThread = new ThreadLocal<>();
    private ThreadLocal<MongoLogger> loggerThread = new ThreadLocal<>();
    private String collectionName;
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Pointcut("@annotation(com.qww.mongologger.core.annotation.MLog)")
    public void mLog() {}

    @Around("mLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MongoLogger mLogger;
        LogType logType;
        Object result;

        /*
          获取日志切点相关信息
         */
        Class<?> clazz = point.getTarget().getClass();
        String className = clazz.getName();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        String methodName = methodSignature.getName();
        Method method = methodSignature.getMethod();
        Object[] args = point.getArgs();

        Field loggerField = getFieldByType(clazz, MongoLogger.class);
        /*
          Component中定义了MLogger则获取对应实例
         */
        if (loggerField != null)  {
            loggerField.setAccessible(true);
            Object mLoggerVal = loggerField.get(point.getTarget());
            if (!(mLoggerVal instanceof MongoLogger)) throw new NullMLoggerException();
            else {
                mLogger = (MongoLogger) mLoggerVal;
                mLogger.setInMLogAnnotation(true);
                loggerThread.set(mLogger);
            }
        }

        /*
          确定日志类型及集合名称
         */
        // Mapping mappingAnnotation = method.getDeclaredAnnotation(Mapping.class);
        MLog annotation = method.getAnnotation(MLog.class);
        if (annotation.type().equals(LogType.BASE)) {
            logType = ReflectUtil.hasBeenAnnotated(method, Mapping.class) ? LogType.WEB : LogType.EXEC;
        } else {
            logType = annotation.type();
        }
        this.collectionName = annotation.collectionName();

        if (logType.isAbove(LogType.EXEC)) {
            if (logType.equals(LogType.WEB)) {
                /*
                  =====请求级日志行为=====
                 */
                WebLog webLog = (WebLog) getProperLogInstance(LogType.WEB);
                boolean hasRequestArg = false;

                /*
                  尝试从Controller入参中获取request实例
                 */
                for (Object arg : args) {
                    if (arg instanceof HttpServletRequest) {
                        hasRequestArg = true;
                        webLog.setDataFromRequest((HttpServletRequest) arg);
                        break;
                    }
                }
                /*
                  从SpringMVC的请求上下文容器中获取request实例
                 */
                if (!hasRequestArg) {
                    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                    if (requestAttributes == null) throw new NullPointerException();
                    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                    webLog.setDataFromRequest(request);
                }
                /*
                  END=====请求级日志行为=====
                 */
            } else if (logType.equals(LogType.TEST)) {
                log.info("TEST Log");
            }

            /*
              =====方法执行日志行为=====
             */
            ExecLog execLog = (ExecLog) getProperLogInstance(LogType.EXEC);
            execLog.setClassName(className);
            execLog.setMethodName(methodName);

            /*
              判断代理对象本身是否是连接点所在的目标对象，不是的话就要通过反射重新获取真实方法
             */
            if (point.getThis().getClass() != clazz) {
                method = clazz.getDeclaredMethod(methodName, method.getParameterTypes());
            }

            /*
              获取真实方法的参数列表
             */
            String[] argNames = parameterNameDiscoverer.getParameterNames(method);
            Class<?>[] argTypes = method.getParameterTypes();
            if (argNames == null) throw new NullPointerException();
            if (argNames.length != args.length || argTypes.length != args.length)
                throw new MethodArgumentMismatchException();

            for (int i = 0; i < args.length; ++i) {
                if (!argTypes[i].isInstance(args[i])) throw new MethodArgumentMismatchException();
                // response对象不打入日志，避免response.getWriter()错误
                if (args[i] instanceof HttpServletResponse) continue;
                // request对象不打入日志，避免field info冲突(MappingException)
                if (args[i] instanceof HttpServletRequest) continue;
                execLog.addMethodArg(argNames[i], argTypes[i], args[i]);
            }

            long beginTime = System.currentTimeMillis();
            result = point.proceed();
            long execTime = System.currentTimeMillis() - beginTime;
            execLog.setExecTime(execTime);
            /*
              END=====方法执行日志行为=====
             */
        } else {
            /*
              =====基础日志行为=====
             */
            BaseLog baseLog = getProperLogInstance(LogType.BASE);
            result = point.proceed();
        }
        return result;
    }

    @AfterReturning(value = "mLog()", returning = "rtv")
    public void after(JoinPoint point, Object rtv) {
        BaseLog baseLog = logThread.get();
        if (baseLog.getLogType().isAbove(LogType.EXEC)) {
            ExecLog execLog = (ExecLog) baseLog;
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            Class<?> type = methodSignature.getReturnType();
            execLog.setMethodReturn(type, rtv);
        }
        this.ending();
    }

    @AfterThrowing(value = "mLog()", throwing = "throwing")
    public void error(Throwable throwing) {
        BaseLog baseLog = logThread.get();
        if (baseLog.getLogType().isAbove(LogType.EXEC)) {
            ExecLog execLog = (ExecLog) baseLog;
            execLog.setMethodError(throwing);
        }
        this.ending();
    }

    private BaseLog getProperLogInstance(LogType logType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        BaseLog properLog = logThread.get();
        if (properLog == null) {
            // 这里使用Log对象的无参构造生成实例，需要注意
            properLog = (BaseLog) logType.getLogClass().getDeclaredConstructor().newInstance();
            logThread.set(properLog);
        }
        return properLog;
    }

    private void ending() {
        BaseLog baseLog = logThread.get();
//        mongoTemplate.save(baseLog);
        MongoLogger mLogger = loggerThread.get();
        if (mLogger == null) {
            mLogger = MongoLoggerFactory.getMongoLogger();
        }
        mLogger.commit(baseLog, collectionName);
        logThread.remove();
        loggerThread.remove();
    }

    public static Field getFieldByType(Class<?> clazz, Class<?> fieldClazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() == fieldClazz) {
                return f;
            }
        }
        return null;
    }
}
