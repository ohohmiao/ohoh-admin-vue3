package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowNextHandlerDTO;
import com.ohohmiao.modules.workflow.model.entity.ProcessTask;
import com.ohohmiao.modules.workflow.model.pojo.FlowProcessForm;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;

import java.util.List;

/**
 * 流程任务Service
 *
 * @author ohohmiao
 * @date 2026-02-14 15:21
 */
public interface ProcessTaskService extends CommonService<ProcessTask> {

    /**
     * 派发流程任务
     * @param flowInfoVO
     * @param processForm
     * @param nextHandlerList
     */
    void assignTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, List<FlowNextHandlerDTO> nextHandlerList);

    /**
     * 保存开始环节流程任务
     * @param flowInfoVO
     * @param processForm
     * @return
     */
    boolean saveStartNodeTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm);

}
