package com.ohohmiao.framework.security.annotation;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证(PC版)
 *
 * @author ohohmiao
 * @date 2023-04-06 10:55
 */
@SaCheckPermission(type = "PC")
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
public @interface SaPcCheckPermission {

    /**
     * 需要校验的权限码
     * @return 需要校验的权限码
     */
    @AliasFor(annotation = SaCheckPermission.class)
    String [] value() default {};

    /**
     * 验证模式：AND | OR，默认OR
     * @return 验证模式
     */
    @AliasFor(annotation = SaCheckPermission.class)
    SaMode mode() default SaMode.OR;

    /**
     * 角色+权限结合判断，默认超管角色
     * @return
     */
    @AliasFor(annotation = SaCheckPermission.class)
    String[] orRole() default "coder";

}
