package com.itheima.anno;

import com.itheima.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented//元注解
@Constraint(validatedBy = {StateValidation.class})//指定提供校验规则的类
@Target(FIELD)//元注解，指定这个注解的作用对象，这里是作用在属性上
@Retention(RUNTIME)//元注解

public @interface State {
    //提供校验失败后的提示信息
    String message() default "state的参数只能是已发布或者草稿";
    //指定分组
    Class<?>[] groups() default {};
    //负载 获取到State注解的附加信息
    Class<? extends Payload>[] payload() default {};
}
