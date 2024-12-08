package com.ohohmiao.modules.workflow.listener;

import com.ohohmiao.framework.common.listener.CommonDataChangeListener;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 流程定义数据变更监听器
 *
 * @author ohohmiao
 * @date 2024-12-08 20:05
 */
@Component
public class FlowDefChangeListener implements CommonDataChangeListener<FlowDef> {

    @Override
    public void doAddWithDataList(List<FlowDef> dataList) {

    }

    @Override
    public void doEditWithDataList(List<FlowDef> dataList) {

    }

    @Override
    public void doDeleteWithDataIdList(List<String> dataIdList) {

    }

}
