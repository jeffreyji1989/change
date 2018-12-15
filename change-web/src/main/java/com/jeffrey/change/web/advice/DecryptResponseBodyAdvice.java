package com.jeffrey.change.web.advice;

import com.jeffrey.change.web.annotation.DecryptField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author jijunhui
 * @Date 2018/11/30 16:40
 * @Version 1.0.0
 * @Description TODO
 */
@Slf4j
@Order(1)
//@ControllerAdvice(basePackages = "com.jeffrey.change.web.controller")
@RestControllerAdvice(basePackages = "com.jeffrey.change.web.controller")
public class DecryptResponseBodyAdvice implements ResponseBodyAdvice {

    // 包含项
    private String[] includes = {};
    // 排除项
    private String[] excludes = {};
    // 是否加密
    private boolean encode = true;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        log.info("-----------------1----------------");
        log.info("aclass:{}",aClass.getName());
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("-----------------2----------------");
        // 重新初始化为默认值
        includes = new String[]{};
        excludes = new String[]{};
        encode = true;

        // 判断返回的对象是单个对象，还是list，活着是map
        if (o == null) {
            return null;
        }
        if (methodParameter.getMethod().isAnnotationPresent(DecryptField.class)) {
            // 获取注解配置的包含和去除字段
            DecryptField serializedField = methodParameter.getMethodAnnotation(DecryptField.class);
            includes = serializedField.includes();
            excludes = serializedField.excludes();
            // 是否加密
            encode = serializedField.encode();
        }

        Object retObj = null;
        if (o instanceof List) {
            // List
            List list = (List) o;
            retObj = handleList(list);
        } else {
            // Single Object
            retObj = handleSingleObject(o);
        }
        return retObj;
    }

    /**
     * 处理返回值是单个enity对象
     *
     * @param o
     * @return
     */
    private Object handleSingleObject(Object o) {
        Map<String, Object> map = new HashMap<>();

        Field[] fields = o.getClass().getDeclaredFields();
        /**
         * 这里的判断逻辑是
         * 1 是否在includes里 如果在，则加密
         * 2 是否在exclueds里 如果在，则排除在外
         * 3 否则 不加密
         */
        for (Field field : fields) {
            // 1 包含的情况
            if (ArrayUtils.contains(includes, field.getName())) {
                String newVal = getNewVal(o, field);
                map.put(field.getName(), newVal);
            } else if (!ArrayUtils.contains(excludes, field.getName())) {
                field.setAccessible(true);
                Object val = null;
                try {
                    val = field.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                map.put(field.getName(), val);
            }
        }
        return map;
    }

    /**
     * 处理返回值是列表
     *
     * @param list
     * @return
     */
    private List handleList(List list) {
        List retList = new ArrayList();
        for (Object o : list) {
            Map map = (Map) handleSingleObject(o);
            retList.add(map);
        }
        return retList;
    }

    /**
     * 获取加密后的新值
     *
     * @param o
     * @param field
     * @return
     */
    private String getNewVal(Object o, Field field) {
        String newVal = "";
        try {
            field.setAccessible(true);
            Object val = field.get(o);

            if (val != null) {
                if (encode) {
                    newVal = new String(Base64.encodeBase64(val.toString().getBytes()));
                } else {
                    newVal = val.toString();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return newVal;
    }
}
