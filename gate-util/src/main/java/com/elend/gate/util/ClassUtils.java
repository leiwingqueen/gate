package com.elend.gate.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class操作相关的工具
 * @author mgt
 *
 */
public class ClassUtils {
    /**
     * 获取field的泛型的class 如果非带泛型，返回空，多个泛型，只返回第一个
     * @param field
     * @return
     */
    public static Class getParameterizedType(Field field) {
        // 获得Field实例的泛型类型
        Type gType = field.getGenericType();
        // 如果gType类型是ParameterizedType对象
        if (gType instanceof ParameterizedType) {
            // 强制类型转换
            ParameterizedType pType = (ParameterizedType) gType;
            // 取得泛型类型的泛型参数
            Type[] tArgs = pType.getActualTypeArguments();
            return (Class) tArgs[0];
        } else {
            return null;
        }
    }
}
