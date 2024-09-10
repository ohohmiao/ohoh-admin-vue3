package com.ohohmiao.framework.common.enums;

import lombok.Getter;

/**
 * 系统日志类别枚举
 *
 * @author ohohmiao
 * @date 2023-08-04 15:47
 */
@Getter
public enum CommonLogTypeEnum {

    /**
     * 操作日志
     */
    OPERATION(1),

    /**
     * 异常日志
     */
    EXCEPTION(2),

    /**
     * 登录日志
     */
    LOGIN(3),

    /**
     * 登出日志
     */
    LOGOUT(4);

    int type;

    CommonLogTypeEnum(int type){
        this.type = type;
    }

}
