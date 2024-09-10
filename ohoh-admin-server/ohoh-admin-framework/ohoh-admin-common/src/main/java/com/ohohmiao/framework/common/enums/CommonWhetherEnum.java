package com.ohohmiao.framework.common.enums;

import lombok.Getter;

/**
 * 是与否枚举
 *
 * @author ohohmiao
 * @date 2023-03-30 16:28
 */
@Getter
public enum CommonWhetherEnum {

    YES(1),

    NO(0);

    /**
     * 编码
     */
    int code;

    CommonWhetherEnum(int code){
        this.code = code;
    }

}
