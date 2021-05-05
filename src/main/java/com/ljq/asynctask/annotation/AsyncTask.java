package com.ljq.asynctask.annotation;

import java.lang.annotation.*;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AsyncTask {

    String taskName();

    Class<?> param() default void.class;
}
