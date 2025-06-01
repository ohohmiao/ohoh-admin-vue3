package com.ohohmiao.modules.workflow.enums;

/**
 * 流程任务多人决策方式枚举
 *
 * @author ohohmiao
 * @date 2025-05-30 17:33
 */
public enum FlowTaskMultiAssignRuleEnum {

    /**
     * 少数服从多数
     */
    MAJORITY,

    /**
     * 以最后一个结果为准
     */
    LAST,

    /**
     * 按比例
     */
    RATIO,

    /**
     * 按权重
     */
    WEIGHT;

}
