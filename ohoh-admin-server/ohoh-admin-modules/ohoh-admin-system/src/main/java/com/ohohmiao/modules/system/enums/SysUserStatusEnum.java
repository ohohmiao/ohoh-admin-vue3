package com.ohohmiao.modules.system.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 系统用户状态枚举
 *
 * @author ohohmiao
 * @date 2023/4/5 17:17
 */
@Getter
public enum SysUserStatusEnum {

    /**
     * 正常
     */
    ENABLE(1),

    /**
     * 停用
     */
    DISABLED(0);

    int status;

    SysUserStatusEnum(int status) {
        this.status = status;
    }

    public static SysUserStatusEnum assemble(int status){
        return Arrays.stream(SysUserStatusEnum.values()).filter(
                e -> e.getStatus() == status).findFirst().orElse(null);
    }

}
