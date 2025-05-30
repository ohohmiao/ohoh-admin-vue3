package com.ohohmiao.modules.workflow.enums;

import lombok.Getter;

/**
 * 任务指派类别枚举
 *
 * @author ohohmiao
 * @date 2025-05-30 17:27
 */
@Getter
public enum FlowTaskAssignTypeEnum {

    /**
     * 单人任务
     */
    SINGLE,

    /**
     * 多人任务
     */
    MULTI,

    /**
     * 自由任务
     */
    FREE;

}
