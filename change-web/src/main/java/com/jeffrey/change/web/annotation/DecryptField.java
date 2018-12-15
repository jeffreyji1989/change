package com.jeffrey.change.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author jijunhui
 * @Date 2018/11/30 16:35
 * @Version 1.0.0
 * @Description 加密过滤字段注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//最高优先级
//@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface DecryptField {
    /**
     * 需要返回的字段
     * 
     * @return
     */
    String[] includes() default {};

    /**
     * 需要去除的字段
     * 
     * @return
     */
    String[] excludes() default {};

    /**
     * 数据是否需要加密
     * 
     * @return
     */
    boolean encode() default true;
}
