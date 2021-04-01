package com.kemp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于列表activity
 * 用于描述列表activity的功能
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Description {
    String value() default "";
}
