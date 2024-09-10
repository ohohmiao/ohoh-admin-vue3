package com.ohohmiao.framework.security.enums;

import lombok.Getter;

/**
 * 登录相关缓存key枚举
 *
 * @author ohohmiao
 * @date 2023/4/6 22:29
 */
@Getter
public enum AuthCacheKeyEnum {

    /**
     * 账号登录失败次数
     */
    USER_LOGIN_FAILTIMES("auth:loginfail", 900L);

    /**
     * 缓存key
     */
    String key;

    /**
     * 缓存存活秒数
     */
    long ttl;

    AuthCacheKeyEnum(String key, long ttl){
        this.key = key;
        this.ttl = ttl;
    }

}
