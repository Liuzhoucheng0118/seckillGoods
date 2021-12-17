package com.lzc.seckill.config;

import javax.xml.bind.Element;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author liuzhoucheng
 * @Date 2021/12/17 14:20
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {//访问次数限制
    int second();
    int maxCount();
    boolean needLogin() default true;
}
