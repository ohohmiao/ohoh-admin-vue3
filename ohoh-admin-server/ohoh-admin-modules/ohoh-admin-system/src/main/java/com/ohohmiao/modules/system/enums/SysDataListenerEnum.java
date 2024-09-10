package com.ohohmiao.modules.system.enums;

import lombok.Getter;

/**
 * 系统模块数据变更监听器类名称枚举
 *
 * @author ohohmiao
 * @date 2023-04-14 10:47
 */
@Getter
public enum SysDataListenerEnum {

    ORG("SysOrgDataChangeListener"),

    ROLE("SysRoleDataChangeListener"),

    USER("SysUserDataChangeListener"),

    DICTYPE("SysDicTypeChangeListener"),

    DIC("SysDicChangeListener"),

    POSITION("SysPositionChangeListener");

    /**
     * 类名称
     */
    String name;

    SysDataListenerEnum(String name){
        this.name = name;
    }

}
