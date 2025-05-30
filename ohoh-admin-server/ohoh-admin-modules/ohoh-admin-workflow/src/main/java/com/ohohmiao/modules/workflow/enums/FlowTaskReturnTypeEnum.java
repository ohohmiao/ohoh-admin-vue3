package com.ohohmiao.modules.workflow.enums;

import lombok.Getter;

/**
 * 任务退回执行方式枚举
 *
 * @author ohohmiao
 * @date 2025-05-30 17:29
 */
@Getter
public enum FlowTaskReturnTypeEnum {

    /**
     * 按流程图执行
     */
    DIAGRAM,

    /**
     * 直接返回
     */
    DIRECT;

}
