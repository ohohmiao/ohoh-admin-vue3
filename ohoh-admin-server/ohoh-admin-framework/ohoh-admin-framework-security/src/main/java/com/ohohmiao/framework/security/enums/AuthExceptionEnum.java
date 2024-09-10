package com.ohohmiao.framework.security.enums;

import lombok.Getter;

/**
 * 登录异常枚举
 *
 * @author ohohmiao
 * @date 2023/4/5 16:41
 */
@Getter
public enum AuthExceptionEnum {

    CAPTCHA_EMPTY("验证码不能为空"),

    CAPTCHA_ERROR("验证码输入有误"),

    ACCOUNTORPASS_ERROR("账号或密码错误"),

    ACCOUNT_DISBALED("账号已被禁用");

    String msg;

    AuthExceptionEnum(String msg){
        this.msg = msg;
    }

}
