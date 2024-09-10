package com.ohohmiao.framework.common.enums;

import lombok.Getter;

/**
 * 通用模块缓存key枚举
 *
 * @author ohohmiao
 * @date 2024-09-04 19:07
 */
@Getter
public enum CommonCacheKeyEnum {

    /**
     * 图像验证码
     */
    KAPTCHA_CODE("kaptcha_codes:", 180L);

    /**
     * 缓存key
     */
    String key;

    /**
     * 缓存存活秒数
     */
    long ttl;

    CommonCacheKeyEnum(String key, long ttl){
        this.key = key;
        this.ttl = ttl;
    }

}
