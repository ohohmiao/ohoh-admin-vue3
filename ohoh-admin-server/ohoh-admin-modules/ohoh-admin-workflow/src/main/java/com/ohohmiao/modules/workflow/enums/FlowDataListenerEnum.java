package com.ohohmiao.modules.workflow.enums;

import lombok.Getter;

/**
 * 流程模块数据变更监听器类名称枚举
 *
 * @author ohohmiao
 * @date 2024-12-02 10:34
 */
@Getter
public enum FlowDataListenerEnum {

    DEFTYPE("FlowDefTypeChangeListener");

    /**
     * 类名称
     */
    String name;

    FlowDataListenerEnum(String name){
        this.name = name;
    }

}
