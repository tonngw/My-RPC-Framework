package com.tonngw.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标识一个服务提供类，注解放在接口的实现类上
 *
 * @author tonngw
 * @date 2022-01-26 22:31
 */
// 表示注解的作用目标为接口、类、枚举类型
@Target(ElementType.TYPE)
// 表示在运行时可以动态获取注解信息
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    public String name() default "";

}
