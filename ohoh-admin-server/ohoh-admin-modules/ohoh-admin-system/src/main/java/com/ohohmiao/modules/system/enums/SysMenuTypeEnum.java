package com.ohohmiao.modules.system.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 菜单类型枚举
 *
 * @author ohohmiao
 * @date 2023-04-07 15:16
 */
@Getter
public enum SysMenuTypeEnum {

    /**
     * 目录
     */
    CATALOG(0),

    /**
     * 内部组件
     */
    MENU(1),

    /**
     * 外链
     */
    LINK(2);

    int type;

    SysMenuTypeEnum(int type){
        this.type = type;
    }

    public static SysMenuTypeEnum assemble(int type){
        return Arrays.stream(SysMenuTypeEnum.values()).filter(
                e -> e.getType() == type).findFirst().orElse(null);
    }

}
