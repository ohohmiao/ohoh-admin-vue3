package com.ohohmiao.modules.system.enums;

import com.ohohmiao.framework.common.util.PlatRedisUtil;
import lombok.Getter;

/**
 * 系统模块缓存key枚举
 *
 * @author ohohmiao
 * @date 2023/4/6 22:29
 */
@Getter
public enum SysCacheKeyEnum {

    /**
     * 全量组织机构
     */
    ORG_ALL("sys_org:all", PlatRedisUtil.TMPKEY_TTL),

    /**
     * 全量用户
     */
    USER_ALL("sys_user:all", PlatRedisUtil.TMPKEY_TTL),

    /**
     * 全量岗位
     */
    POSITION_ALL("sys_position:all", PlatRedisUtil.TMPKEY_TTL),

    /**
     * 全量系统字典类别
     */
    DICTYPE_ALL("sys_dictype:all", PlatRedisUtil.TMPKEY_TTL),

    /**
     * 全量系统字典
     */
    DIC_ALL("sys_dic:all", PlatRedisUtil.TMPKEY_TTL);

    /**
     * 缓存key
     */
    String key;

    /**
     * 缓存存活秒数
     */
    long ttl;

    SysCacheKeyEnum(String key, long ttl){
        this.key = key;
        this.ttl = ttl;
    }

}
