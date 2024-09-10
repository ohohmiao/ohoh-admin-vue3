package com.ohohmiao.framework.security.enums;

import lombok.Getter;

/**
 * 登录相关常量枚举
 *
 * @author ohohmiao
 * @date 2023/4/5 18:32
 */
@Getter
public enum AuthConstEnum {

    /**
     * 登录用户session key
     */
    LOGINUSER_SESSIONKEY("curLoginUser"),

    /**
     * 超级管理员账号
     */
    SUPERADMIN_ACCOUNT("coder"),

    /**
     * 账号默认密码
     */
    DEFAULT_PASSWORD("ohoh@123");

    String value;

    AuthConstEnum(String value){
        this.value = value;
    }

}
