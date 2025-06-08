package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;

/**
 * 流程核心Service
 *
 * @author ohohmiao
 * @date 2025-06-08 11:23
 */
public interface FlowService {

    /**
     * 获取流程核心信息
     * @param queryDTO
     * @return
     */
    FlowInfoVO getFlowInfo(FlowInfoQueryDTO queryDTO);

}
