package com.elend.gate.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * xml封装类的注解
 * 
 * @author mgt
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeAnnotation {
    /** 根点名称 */
    public String name() default "";

    /** 节点类型 */
    public NodeType type() default NodeType.SIMPLE_FIELD;
}
