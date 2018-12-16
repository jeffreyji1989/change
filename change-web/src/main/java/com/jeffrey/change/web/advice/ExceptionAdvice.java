package com.jeffrey.change.web.advice;

import com.jeffrey.change.web.vo.BaseResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public BaseResultVo defaultException(HttpServletRequest request, Exception e){
        // 拦截参数验证没通过的情况
        if (e instanceof MethodArgumentNotValidException){
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            if (bindingResult.hasErrors()){
                BaseResultVo baseResultVo = new BaseResultVo();
                for (ObjectError err :bindingResult.getAllErrors()){
                    log.error("--->>>>>注解名称:{},field:{},errMsg:{}",err.getCode(),((FieldError) err).getField(),err.getDefaultMessage());
                    baseResultVo.setCode("0");
                    baseResultVo.setMessage(err.getDefaultMessage());
                    break;
                }
                return baseResultVo;
            }
        }
        return null;
    }
}


