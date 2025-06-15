package com.ohohmiao.modules.workflow.enums;

import com.ohohmiao.framework.common.util.PlatRedisUtil;
import lombok.Getter;

/**
 * 流程模块缓存key枚举
 *
 * @author ohohmiao
 * @date 2024-12-02 09:27
 */
@Getter
public enum FlowCacheKeyEnum {

    /**
     * 流程定义类别
     */
    DEFTYPE_ALL("workflow_deftype:all", PlatRedisUtil.TMPKEY_TTL),

    /**
     * 流程实例流水号
     */
    WORKFLOW_SERIAL("workflow:serial:%s:%s", PlatRedisUtil.TMPKEY_TTL);

    /**
     * 缓存key
     */
    String key;

    /**
     * 缓存存活秒数
     */
    long ttl;

    FlowCacheKeyEnum(String key, long ttl) {
        this.key = key;
        this.ttl = ttl;
    }

}
