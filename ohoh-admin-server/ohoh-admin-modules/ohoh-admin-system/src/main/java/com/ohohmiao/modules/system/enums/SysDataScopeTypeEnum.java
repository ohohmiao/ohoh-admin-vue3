package com.ohohmiao.modules.system.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 数据范围类别枚举
 *
 * @author ohohmiao
 * @date 2023-04-06 16:57
 */
@Getter
public enum SysDataScopeTypeEnum {

    /**
     * 仅本人数据权限
     */
    SELF(0),

    /**
     * 全部机构数据权限
     */
    ALL(1),

    /**
     * 角色机构数据权限
     */
    ROLEORG(2),

    /**
     * 角色机构及下属数据权限
     */
    ROLEORGANDCHILD(3),

    /**
     * 自定义机构数据权限
     */
    CUSTOM(4);

    int type;

    SysDataScopeTypeEnum(int type){
        this.type = type;
    }

    public static SysDataScopeTypeEnum assemble(int type){
        return Arrays.stream(SysDataScopeTypeEnum.values()).filter(
                e -> e.getType() == type).findFirst().orElse(null);
    }

}
