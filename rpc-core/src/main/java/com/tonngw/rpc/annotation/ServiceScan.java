package com.tonngw.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识服务的扫描的包的范围,即扫描范围的根包
 * 
 * @author tonngw
 * @date 2022-01-26 22:33
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScan {

    public String value() default "";

}
