package com.ohohmiao.framework.security.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 登录设备枚举
 *
 * @author ohohmiao
 * @date 2023/4/5 16:15
 */
@Getter
public enum AuthDeviceEnum {

    PC("PC");

    String type;

    AuthDeviceEnum(String type){
        this.type = type;
    }

    public static AuthDeviceEnum assemble(String type){
        return Arrays.stream(AuthDeviceEnum.values()).filter(
                e -> e.getType().equals(type)).findFirst().orElse(null);
    }

}
