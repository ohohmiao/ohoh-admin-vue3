package com.ohohmiao.modules.system.enums;

import lombok.Getter;

/**
 * 系统角色属性枚举
 *
 * @author ohohmiao
 * @date 2023-04-06 9:41
 */
@Getter
public enum SysRolePropEnum {

    /**
     * 角色授予的用户
     */
    SYSUSER("SYS_USER"),

    /**
     * 角色授予的系统资源
     */
    SYSRES("SYS_RES"),

    /**
     * 角色自定义数据范围
     */
    CUSTOM_DATASCOPE("CUSTOM_DATASCOPE");

    String value;

    SysRolePropEnum(String value){
        this.value = value;
    }

}
