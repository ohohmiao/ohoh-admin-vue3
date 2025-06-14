package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNextNodeQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowSubmitDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.model.vo.FlowTaskNodeVO;

import java.util.List;

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
     * @param includeExtraInfo
     * @return
     */
    FlowInfoVO getFlowInfo(FlowInfoQueryDTO queryDTO, boolean includeExtraInfo);

    /**
     * 查询流程下一环节信息
     * @param queryDTO
     * @return
     */
    List<FlowTaskNodeVO> getNextNodeList(FlowNextNodeQueryDTO queryDTO);

    /**
     * 提交流程
     * @param submitDTO
     */
    void doSubmit(FlowSubmitDTO submitDTO);

}
