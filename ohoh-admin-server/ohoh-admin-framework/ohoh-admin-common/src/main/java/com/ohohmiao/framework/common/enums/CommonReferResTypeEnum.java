package com.ohohmiao.framework.common.enums;

import lombok.Getter;

/**
 * 关联资源类别枚举
 *
 * @author ohohmiao
 * @date 2025-05-27 15:40
 */
@Getter
public enum CommonReferResTypeEnum {

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

    CommonReferResTypeEnum(String code){
        this.code = code;
    }

}
