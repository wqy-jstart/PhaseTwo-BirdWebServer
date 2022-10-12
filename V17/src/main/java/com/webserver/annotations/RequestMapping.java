package com.webserver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标注某个Controller中处理某个请求的方法
 */
@Target(ElementType.METHOD)//用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();//需要传入处理表单的地址
}
