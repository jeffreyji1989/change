package com.jeffrey.change.web.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @Author jijunhui
 * @Date 2018/11/30 16:13
 * @Version 1.0.0
 * @Description TODO
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.jeffrey.change.web.controller")
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    /**
     * 是否支持 如果是false 则表示不会执行其他方法 true的时候才会执行
     *
     * @param methodParameter
     * @param type
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() throws IOException {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes.getRequest();
                log.info("--->>>>>这种方式可以取到request的信息{}",request.getRequestURL());
                InputStream inputStream = httpInputMessage.getBody();
                String result = IOUtils.toString(inputStream, "utf-8");
                JSONObject jsonObject = JSON.parseObject(result);
                jsonObject.entrySet().forEach(val -> {
                    String key = val.getKey();
                    Object value = val.getValue();
                    if (key.equals("userName")) {
                        val.setValue(new String(Base64.decodeBase64(value.toString())));
                    }
                });
                jsonObject.put("aaaaa", "aaaaaaa");
                return IOUtils.toInputStream(JSON.toJSONString(jsonObject), "utf-8");
            }

            @Override
            public HttpHeaders getHeaders() {
                return httpInputMessage.getHeaders();
            }
        };
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        log.info("--->>>>>返回的对象:{}", o);
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        log.info("--->>>>>========请求对象为空的时候执行此方法================");
        return o;
    }
}