package com.ohohmiao.framework.log.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author ohohmiao
 * @date 2023-08-04 11:12
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommonLog {

    /**
     * 日志名称
     */
    String value() default "未命名";

    /**
     * 是否保存请求参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存响应结果
     */
    boolean isSaveResponseData() default true;

    /**
     * 需排除的请求参数名称
     */
    String[] excludeParamNames() default {};

}
