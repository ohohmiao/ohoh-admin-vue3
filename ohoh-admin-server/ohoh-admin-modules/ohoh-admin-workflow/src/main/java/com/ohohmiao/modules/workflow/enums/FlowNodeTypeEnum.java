package com.ohohmiao.modules.workflow.enums;

import lombok.Getter;

/**
 * 流程环节节点类别枚举
 *
 * @author ohohmiao
 * @date 2025-06-10 11:13
 */
@Getter
public enum FlowNodeTypeEnum {

    /**
     * 任务节点
     */
    TASK("task"),

    /**
     * 办结节点
     */
    END("end"),

    /**
     * 分支节点
     */
    DECISION("exclusiveGateway"),

    /**
     * 并行节点
     */
    PARALLEL("parallelGateway"),

    /**
     * 合并节点
     */
    INCLUSIVE("inclusiveGateway");

    String code;

    FlowNodeTypeEnum(String code){
        this.code = code;
    }

}
