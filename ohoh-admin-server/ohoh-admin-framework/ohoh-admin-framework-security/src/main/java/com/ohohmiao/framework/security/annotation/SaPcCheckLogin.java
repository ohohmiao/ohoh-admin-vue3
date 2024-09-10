package com.ohohmiao.framework.security.annotation;

import cn.dev33.satoken.annotation.SaCheckLogin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录认证(PC版)：只有登录之后才能进入该方法
 *
 * @author ohohmiao
 * @date 2023-04-06 10:43
 */
@SaCheckLogin(type = "PC")
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE})
public @interface SaPcCheckLogin {
}
