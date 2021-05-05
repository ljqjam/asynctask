package com.ljq.asynctask.annotation;

import java.lang.annotation.*;

/**
 * @author linjunqiang
 * @Date 2021/5/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AsyncEnable {
}
