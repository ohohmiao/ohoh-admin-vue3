package com.ohohmiao.modules.system.enums;

import lombok.Getter;

/**
 * 关联资源类别枚举
 *
 * @author ohohmiao
 * @date 2025-06-03 16:56
 */
@Getter
public enum SysReferResTypeEnum {

    /**
     * 用户
     */
    USER("user"),

    /**
     * 部门
     */
    ORG("org"),

    /**
     * 岗位
     */
    POSITION("position"),

    /**
     * 联系组
     */
    CONTACT("contact");

    String code;

    SysReferResTypeEnum(String code){
        this.code = code;
    }

}
