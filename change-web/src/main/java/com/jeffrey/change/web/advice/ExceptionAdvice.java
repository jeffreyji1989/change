package com.jeffrey.change.web.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:jeffrey(jeffreyji@aliyun.com)
 * @date: 2018/12/16 0:05
 * @version:1.0.0
 * @description:统一异常拦截
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.jeffrey.change.web.controller")
public class ExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String defaultException(HttpServletRequest request, Exception e){
        log.info("异常信息：{}",e.getMessage());
        return "出错了！";
    }
}


