package com.ohohmiao.modules.workflow.enums;

/**
 * 流程事件类别枚举
 *
 * @author ohohmiao
 * @date 2025-01-15 20:32
 */
public enum FlowEventTypeEnum {

    /**
     * 存储
     */
    SAVE,

    /**
     * 前置
     */
    PRE,

    /**
     * 后置
     */
    AFT,

    /**
     * 判断
     */
    DECIDE,

    /**
     * 读取
     */
    READ;

}
