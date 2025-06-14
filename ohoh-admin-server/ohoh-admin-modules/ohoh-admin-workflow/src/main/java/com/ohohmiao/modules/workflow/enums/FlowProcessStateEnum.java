package com.ohohmiao.modules.workflow.enums;

/**
 * 流程实例状态枚举
 *
 * @author ohohmiao
 * @date 2025-06-14 13:29
 */
public enum FlowProcessStateEnum {

    /**
     * 草稿
     */
    DRAFT,

    /**
     * 正在办理
     */
    RUNNING,

    /**
     * 已办结(正常结束)
     */
    END,

    /**
     * 已办结(审核通过)
     */
    APPROVED,

    /**
     * 已办结(审核不通过)
     */
    NOTAPPROVED,

    /**
     * 已办结(强制终止)
     */
    TERMINATED;

}
