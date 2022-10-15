package com.webserver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标注处于处理业务的类
 */
@Target(ElementType.TYPE)//在类上使用
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
