package com.ohohmiao.modules.workflow.enums;

/**
 * 办理时限限制类别枚举
 *
 * @author ohohmiao
 * @date 2025-05-27 11:55
 */
public enum ProcessLimitTypeEnum {

    /**
     * 不限制
     */
    NO,

    /**
     * 按工作日
     */
    WORKDAY,

    /**
     * 按自然日
     */
    DAY,

    /**
     * 按小时
     */
    HOUR;

}
