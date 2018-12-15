package com.jeffrey.change.web.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


/**
 * @author:jeffrey(jeffreyji@aliyun.com)
 * @date: 2018/12/15 21:17
 * @version:1.0.0
 * @description:日志统一拦截 todo execution这种方式在springboot2中没有调试通 within的这种可以
 */
@Slf4j
@Aspect
@Configuration
public class LogAspect {
    /**
     * jdk1.8 线程安全的时间format
     */
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //    @Pointcut("execution(public * com.jeffrey.change.web.*.*(..))")
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        String className = joinPoint.getSignature().getName();
        // 获取httpservletrequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("=================================统一入参日志拦截start================================================");
        log.info("--->>>>>请求时间:{}", dateTimeFormatter.format(LocalDateTime.now()));
        log.info("--->>>>>请求IP:{}", getIpAddress(request));
        log.info("--->>>>>请求url:{}", request.getRequestURL().toString());
        log.info("--->>>>>请求的类:{}", packageName);
        log.info("--->>>>>请求的方法:{}()", className);
        log.info("--->>>>>请求的参数:{}", Arrays.toString(joinPoint.getArgs()));
        log.info("=================================统一入参日志拦截end================================================");
    }

//    /**
//     * 处理完请求返回内容
//     * @param ret
//     * @throws Throwable
//     */
//    @AfterReturning(returning = "ret", pointcut = "log()")
//    public void doAfterReturning(Object ret) throws Throwable {
//        // 处理完请求，返回内容
//        System.out.println("方法的返回值 : " + ret);
//    }

    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     * @param joinPoint
     */
    @After("log()")
    public void doAfter(JoinPoint joinPoint) {
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        String className = joinPoint.getSignature().getName();
        log.info("=================================统一返回值日志拦截start================================================");
        log.info("--->>>>>返回时间:{}", dateTimeFormatter.format(LocalDateTime.now()));
        log.info("--->>>>>请求的类:{}", packageName);
        log.info("--->>>>>请求的方法:{}()", className);
        log.info("--->>>>>返回值:{}", Arrays.toString(joinPoint.getArgs()));
        log.info("=================================统一返回值日志拦截end================================================");
    }

//    /**
//     * 环绕通知,环绕增强，相当于MethodInterceptor
//     * @param pjp
//     * @return
//     */
//    @Around("log()")
//    public Object doArround(ProceedingJoinPoint pjp) {
//        try {
//            log.info("--->>>>>start{}",pjp.getSignature().getDeclaringTypeName());
//            Object o =  pjp.proceed();
//            log.info("--->>>>>end{}",o);
//            return o;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    /**
//     * 异常
//     * @param joinPoint
//     */
//    @AfterThrowing("log()")
//    public void afterThrowing(JoinPoint joinPoint) {
//        log.info("--------------{}",joinPoint.getTarget().toString());
//    }


    /**
     * 获取ip地址
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
