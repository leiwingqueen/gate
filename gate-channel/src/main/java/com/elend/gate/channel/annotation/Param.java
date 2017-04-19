package com.elend.gate.channel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数必填和加密顺序控制
 * @author liyongquan 2015年6月5日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Param {
    //是否必填
    boolean required() default false;
    //签名加密顺序
    int sequence() default -1;
}
