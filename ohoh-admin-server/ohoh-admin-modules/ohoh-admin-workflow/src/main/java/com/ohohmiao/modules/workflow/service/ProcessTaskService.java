package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowNextHandlerDTO;
import com.ohohmiao.modules.workflow.model.entity.ProcessTask;
import com.ohohmiao.modules.workflow.model.pojo.FlowProcessForm;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.model.vo.FlowTaskNodeVO;

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
     * 查询串审环节的下一等待办理人
     * @param processId
     * @param taskId
     * @return
     */
    FlowTaskNodeVO getMultiHandleNodeNextWaitingHandler(String processId, String taskId);

    /**
     * 列出未办理的流程任务
     * @param processId
     * @return
     */
    List<ProcessTask> listCurRunningProcessTasks(String processId);

    /**
     * 级联更新任务状态
     * @param taskId
     * @param taskState
     */
    void updateTaskStateCascade(String taskId, Integer taskState);

    /**
     * 判断流程任务是否存在
     * @param taskId
     * @return
     */
    boolean isExist(String taskId);

}
