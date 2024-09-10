package com.ohohmiao.modules.system.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 系统资源类型枚举
 *
 * @author ohohmiao
 * @date 2023-04-06 16:41
 */
@Getter
public enum SysResTypeEnum {

    /**
     * 菜单
     */
    MENU(0),

    /**
     * 按钮
     */
    BUTTON(1);

    int type;

    SysResTypeEnum(int type){
        this.type = type;
    }

    public static SysResTypeEnum assemble(int type){
        return Arrays.stream(SysResTypeEnum.values()).filter(
                e -> e.getType() == type).findFirst().orElse(null);
    }

}
