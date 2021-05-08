package com.kemp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于列表activity
 * 用于描述列表activity的功能
 *
 * 4个元注解
 * @Target 表示该注解用于什么地方
 * @Retention 表示在什么级别保存该注解信息
 * @Documented 将此注解包含在 javadoc 中 ，它代表着此注解会被javadoc工具提取成文档
 * @Inherited 允许子类继承父类中的注解
 *
 * 3个标准注解
 * @Override 表示当前的方法定义将覆盖超类中的方法
 * @Deprecated 废除，如果使用了注解为它的元素，那么编译器会发出警告信息
 * @SuppressWarnings 关闭不当的编译器警告信息
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Description {
    String value() default "";
}
