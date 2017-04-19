package com.elend.gate.annotation;

/**
 * Node注解的type类型
 * 
 * @author mgt
 */
public enum NodeType {
    /** 类 */
    CLASS,
    /** 一般属性，如String */
    SIMPLE_FIELD,
    /** 类属性 */
    CLASS_FIELD,
    /** 集合属性 */
    LIST_FIELD
}
