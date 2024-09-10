package com.ohohmiao.modules.system.enums;

import lombok.Getter;

/**
 * 系统用户属性枚举
 *
 * @author ohohmiao
 * @date 2023-04-06 9:41
 */
@Getter
public enum SysUserPropEnum {

    /**
     * 用户所在机构
     */
    SYSORG("SYS_ORG"),

    /**
     * 用户所在群组
     */
    USERGROUP("SYS_USERGROUP"),

    /**
     * 用户数据范围
     */
    DATASCOPE("DATASCOPE");

    String value;

    SysUserPropEnum(String value){
        this.value = value;
    }

}
