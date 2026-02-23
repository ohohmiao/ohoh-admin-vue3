package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowMyApprovalPageDTO;
import com.ohohmiao.modules.workflow.model.entity.ProcessInstance;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.model.vo.ProcessInstanceVO;

import java.util.List;

/**
 * 流程实例Service
 *
 * @author ohohmiao
 * @date 2025-06-11 10:22
 */
public interface ProcessInstanceService extends CommonService<ProcessInstance> {

    /**
     * 获取流程发起人
     * @param flowInfoVO
     * @return
     */
    List<FlowTaskHandler> getCreator(FlowInfoVO flowInfoVO);

    /**
     * 保存或更新流程实例
     * @param flowInfoVO
     * @param isTempSave
     */
    void saveOrUpdate(FlowInfoVO flowInfoVO, boolean isTempSave);

    /**
     * 更新当前运行信息
     * @param processId
     * @param curRunningNodeIds
     * @param curRunningNodeNames
     * @param curHandlerIds
     * @param curHandlerNames
     */
    void updateCurRunningInfo(String processId, String curRunningNodeIds,
                              String curRunningNodeNames, String curHandlerIds, String curHandlerNames);

    /**
     * 更新办结信息
     * @param processId
     * @param processState
     * @param finalOpinion
     */
    void updateHandleEndedInfo(String processId, Integer processState, String finalOpinion);

    /**
     * 获取待我审批流程列表
     * @param pageDTO
     * @return
     */
    Page<ProcessInstanceVO> listMyApprovalPage(FlowMyApprovalPageDTO pageDTO);

}
