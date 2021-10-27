package com.kemp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * activity label
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Label {
    String value() default "";
}
