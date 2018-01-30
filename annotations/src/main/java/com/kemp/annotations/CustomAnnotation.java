package com.kemp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangkp on 2017/12/19.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface CustomAnnotation {
    String value() default "value";
}
