package com.ohohmiao.modules.workflow.enums;

/**
 * 流程动作类别枚举
 *
 * @author ohohmiao
 * @date 2025-06-10 10:02
 */
public enum FlowActTypeEnum {

    /**
     * 暂存
     */
    TEMPSAVE,

    /**
     * 提交
     */
    SUBMIT,

    /**
     * 退回
     */
    RETURN,

    /**
     * 挂起
     */
    SUSPEND,

    /**
     * 重启
     */
    RESTART,

    /**
     * 追回
     */
    RECOVER,

    /**
     * 转办
     */
    TRANSFER,

    /**
     * 跳转
     */
    JUMP;

}
