package com.ohohmiao.framework.common.enums;

import lombok.Getter;

/**
 * 执行结果枚举
 *
 * @author ohohmiao
 * @date 2023-08-04 15:59
 */
@Getter
public enum CommonExeResultEnum {

    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 失败
     */
    FAIL(0);

    int result;

    CommonExeResultEnum(int result){
        this.result = result;
    }

}
