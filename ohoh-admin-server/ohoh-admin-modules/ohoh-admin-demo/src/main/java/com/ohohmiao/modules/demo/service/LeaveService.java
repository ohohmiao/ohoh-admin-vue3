package com.ohohmiao.modules.demo.service;

import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;

/**
 * @author ohohmiao
 * @date 2025-06-11 14:34
 */
public interface LeaveService {

    /**
     * 请假流程决策节点
     * @param flowInfoVO
     * @return
     */
    FlowInfoVO getFlowDecideResult(FlowInfoVO flowInfoVO);

}
