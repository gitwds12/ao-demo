package com.example.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.OperateLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面处理类，操作日志异常日志记录处理
 * @author wu
 * */
@Aspect
@Component
public class OperateLogAspect {

    private final Logger logger = LoggerFactory.getLogger(OperateLogAspect.class);

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     * */
    @Pointcut("@annotation(com.example.demo.annotation.OperateLog)")
    public void operateLogPointCut() {
    }

    /**
     * 设置操作异常切入点记录异常日志 扫描所有controller包下操作
     * */
     @Pointcut("execution(* com.example.demo.controller..*.*(..))")
     public void operateExceptionLogPointCut() {
     }


    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     * @param joinPoint 切入点
     * @param keys      返回结果
     * */
    @AfterReturning(value = "operateLogPointCut()", returning = "keys")
    public void loggerOperateLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        OperateLog opLog = method.getAnnotation(OperateLog.class);
        if (opLog != null) {
            String operateModule = opLog.operateModule();
            String operateType = opLog.operateType();
            String operateDesc = opLog.operateDesc();
            logger.info("操作模块：" + operateModule);
            logger.info("操作类型：" + operateType);
            logger.info("操作描述：" + operateDesc);
        }

        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();

        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;

        logger.info("请求方法：" + methodName);

        // 请求的参数
        Map<String, String> rtnMap = convertMap(request.getParameterMap());
        String params = JSON.toJSONString(rtnMap);

        logger.info("请求参数：" + params);

        logger.info("返回结果：" + JSON.toJSONString(keys));

    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     * @param joinPoint 切入点
     * @param e         异常信息
     * */
    @AfterThrowing(pointcut = "operateExceptionLogPointCut()", throwing = "e")
    public void loggerExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();

        logger.info("省略一些。。。");
        logger.info("异常名称："+e.getClass().getName()); // 异常名称
    }


    /**
     * 转换request 请求参数
     * @param paramMap request获取的参数数组
     * */
     public Map<String, String> convertMap(Map<String, String[]> paramMap) {
         Map<String, String> rtnMap = new HashMap<String, String>();
         for (String key : paramMap.keySet()) {
                 rtnMap.put(key, paramMap.get(key)[0]);
             }
         return rtnMap;
     }

}
