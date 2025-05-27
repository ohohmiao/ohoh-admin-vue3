package com.ohohmiao.modules.workflow.enums;

import lombok.Getter;

/**
 * 流程发起范围枚举
 *
 * @author ohohmiao
 * @date 2025-05-27 12:05
 */
@Getter
public enum FlowInitiatorScopeEnum {

    /**
     * 全体人员
     */
    ALL,

    /**
     * 指定人员
     */
    TARGET;

}
