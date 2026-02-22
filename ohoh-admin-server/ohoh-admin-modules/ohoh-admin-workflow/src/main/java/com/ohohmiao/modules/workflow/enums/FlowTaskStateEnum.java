package com.ohohmiao.modules.workflow.enums;

/**
 * 流程任务状态枚举
 *
 * @author ohohmiao
 * @date 2025-06-15 17:32
 */
public enum FlowTaskStateEnum {

    /**
     * 正在办理
     */
    RUNNING,

    /**
     * 已办理
     */
    HANDLED,

    /**
     * 审核通过？
     */
    //APPROVED,

    /**
     * 审核不通过？
     */
    //NOTAPPROVED,

    /**
     * 已退回
     */
    RETURNED,

    /**
     * 等待中
     */
    WAITING,

    /**
     * 已转办
     */
    TRANSFERED,

    /**
     * 已办结
     */
    ENDED,

    /**
     * 已挂起
     */
    SUSPENDED,

    /**
     * 已重启
     */
    RESTARTED,

    /**
     * 被追回
     */
    REVOKED,

    /**
     * 被跳转
     */
    JUMPED;

}
