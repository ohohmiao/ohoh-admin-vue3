package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.workflow.enums.*;
import com.ohohmiao.modules.workflow.mapper.ProcessTaskMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowNextHandlerDTO;
import com.ohohmiao.modules.workflow.model.entity.ProcessTask;
import com.ohohmiao.modules.workflow.model.pojo.FlowProcessForm;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;
import com.ohohmiao.modules.workflow.service.FlowNodeService;
import com.ohohmiao.modules.workflow.service.ProcessTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程任务Service实现
 *
 * @author ohohmiao
 * @date 2026-02-14 15:22
 */
@Service("processTaskService")
public class ProcessTaskServiceImpl extends CommonServiceImpl<ProcessTaskMapper, ProcessTask> implements ProcessTaskService {

    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Resource
    private FlowNodeService flowNodeService;

    @Override
    public void assignTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, List<FlowNextHandlerDTO> nextHandlerList){
        // TODO 开始环节，则保存开始环节信息；否则，处理当前环节任务
        boolean isNotEnded = false;
        if(flowInfoVO.getStartFlowFlag()){
            isNotEnded = this.saveStartNodeTask(flowInfoVO, processForm);
        }else{

        }
        if(isNotEnded){
            // TODO 非跳转和非转办情形，多人任务环节，计算决策结果
            if(flowInfoVO.getActType() != FlowActTypeEnum.TRANSFER.ordinal() &&
                    flowInfoVO.getActType() != FlowActTypeEnum.JUMP.ordinal()){

            }
            // 遍历处理下一步环节流程任务
            for(FlowNextHandlerDTO nextHandler: nextHandlerList){
                if(nextHandler.getNodeType().equals(FlowNodeTypeEnum.TASK.getCode())){
                    // TODO 判断是否跳出并行环节

                    // 根据下一环节任务指派类别，分别处理
                    FlowNodeVO nextNodeInfo = flowNodeService.get(flowInfoVO.getDefCode(),
                            flowInfoVO.getDefVersion(), nextHandler.getNodeId());
                    if(nextNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.SINGLE.ordinal()){
                        this.assignSingleTask(flowInfoVO, processForm, nextHandler);
                    }else if(nextNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.MULTI.ordinal()){
                        this.assignMultiTask(flowInfoVO, processForm, nextHandler);
                    }else if(nextNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.FREE.ordinal()){
                        this.assignFreeTask(flowInfoVO, processForm, nextHandler);
                    }
                }
            }
        }
    }

    @Override
    public boolean saveStartNodeTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm){
        ProcessTask startTask = new ProcessTask();
        startTask.setProcessId(flowInfoVO.getProcessId());
        startTask.setTaskState(FlowTaskStateEnum.HANDLED.ordinal());
        FlowNodeVO curNodeInfo = flowInfoVO.getCurNodeInfo();
        startTask.setTaskNodeid(curNodeInfo.getNodeId());
        startTask.setTaskNodename(curNodeInfo.getNodeName());
        startTask.setAssignHandlerids(flowInfoVO.getCreatorId());
        startTask.setAssignHandlernames(flowInfoVO.getCreatorName());
        if(flowInfoVO.getCreatorType() == ProcessCreatorTypeEnum.SYSUSER.ordinal()){
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            startTask.setHandlerId(loginUser.getUserId());
            startTask.setHandlerName(loginUser.getUserName());
            StpLoginUser.UserOrg loginUserOrg = loginUser.getSwitchOrg();
            startTask.setHandlerOrgid(loginUserOrg.getOrgId());
            startTask.setHandlerOrgname(loginUserOrg.getOrgName());
        }
        // TODO 非系统用户发起情形
        LocalDateTime curDateTime = LocalDateTime.now();
        startTask.setTaskStarttime(curDateTime);
        startTask.setApprovalResult(processForm.getAppovalResult());
        startTask.setHandleOpinion(processForm.getHandleOpinion());
        startTask.setTaskEndtime(curDateTime);
        startTask.setConsumeSeconds(0L);
        startTask.setOvertimeFlag(CommonWhetherEnum.NO.getCode());
        startTask.setExceedSeconds(0L);
        this.save(startTask);
        return true;
    }

    private void assignSingleTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, FlowNextHandlerDTO nextHandlerDTO){
        LocalDateTime curDateTime = LocalDateTime.now();
        FlowNodeVO curNodeInfo = flowInfoVO.getCurNodeInfo();
        Integer actType = flowInfoVO.getActType();
        List<FlowTaskHandler> nextHandlers = nextHandlerDTO.getHandlers();
        String nextHandlerIds = nextHandlers.stream().map(
                FlowTaskHandler::getHandlerId).collect(Collectors.joining(","));
        String nextHandlerNames = nextHandlers.stream().map(
                FlowTaskHandler::getHandlerName).collect(Collectors.joining(","));

        ProcessTask parentTask = new ProcessTask();
        parentTask.setProcessId(flowInfoVO.getProcessId());
        parentTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
        parentTask.setTaskNodeid(nextHandlerDTO.getNodeId());
        parentTask.setTaskNodename(nextHandlerDTO.getNodeName());
        if(actType == FlowActTypeEnum.TRANSFER.ordinal() || actType == FlowActTypeEnum.JUMP.ordinal()){
            // TODO 转办/跳转
        }else if(actType == FlowActTypeEnum.RETURN.ordinal()){
            // TODO 退回
        }else{
            parentTask.setIncomingNodeid(curNodeInfo.getNodeId());
            parentTask.setIncomingNodename(curNodeInfo.getNodeName());
        }
        // TODO parentTask.setOutgoingTaskids();
        parentTask.setAssignHandlerids(nextHandlerIds);
        parentTask.setAssignHandlernames(nextHandlerNames);
        parentTask.setTaskStarttime(curDateTime);
        parentTask.setTaskDeadline(processForm.getHandleDeadline());
        this.save(parentTask);

        for(FlowTaskHandler nextHandler: nextHandlers){
            ProcessTask subTask = BeanUtil.copyProperties(parentTask, ProcessTask.class);
            subTask.setTaskId(null);
            subTask.setAssignHandlerids(null);
            subTask.setAssignHandlernames(null);
            subTask.setParentTaskid(parentTask.getTaskId());
            subTask.setHandlerId(nextHandler.getHandlerId());
            subTask.setHandlerName(nextHandler.getHandlerName());
            // TODO subTask.setHandlerOrgid();subTask.setHandlerOrgname();
            this.save(subTask);
        }
    }

    private void assignMultiTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, FlowNextHandlerDTO nextHandlerDTO){
        LocalDateTime curDateTime = LocalDateTime.now();
        FlowNodeVO curNodeInfo = flowInfoVO.getCurNodeInfo();
        String curNodeId = curNodeInfo.getNodeId();
        String curNodeName = curNodeInfo.getNodeName();
        Integer actType = flowInfoVO.getActType();
        if(actType == FlowActTypeEnum.TRANSFER.ordinal() || actType == FlowActTypeEnum.JUMP.ordinal()){
            // TODO 转办/跳转 根据flowInfoVO curTaskId 查询任务表，回填当前任务节点id和当前任务节点名称
        }
        List<FlowTaskHandler> nextHandlers = nextHandlerDTO.getHandlers();
        String taskGroupid = UUID.randomUUID().toString(true);

        for(int i = 0; i < nextHandlers.size(); i++){
            FlowTaskHandler nextHandler = nextHandlers.get(i);
            ProcessTask subTask = new ProcessTask();
            subTask.setProcessId(flowInfoVO.getProcessId());
            if(nextHandlerDTO.getMultiHandletype() == FlowTaskMultiHandleTypeEnum.PARALLEL.ordinal()){
                subTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
            }else{
                if(i == 0){
                    subTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
                }else{
                    subTask.setTaskState(FlowTaskStateEnum.WAITING.ordinal());
                }
            }
            subTask.setTaskNodeid(nextHandlerDTO.getNodeId());
            subTask.setTaskNodename(nextHandlerDTO.getNodeName());
            subTask.setIncomingNodeid(curNodeId);
            subTask.setIncomingNodename(curNodeName);
            // TODO subTask.setOutgoingTaskids();
            subTask.setMultiHandletype(nextHandlerDTO.getMultiHandletype());
            subTask.setTaskGroupid(taskGroupid);
            subTask.setAssignHandlerids(nextHandler.getHandlerId());
            subTask.setAssignHandlernames(nextHandler.getHandlerName());
            subTask.setHandlerId(nextHandler.getHandlerId());
            subTask.setHandlerName(nextHandler.getHandlerName());

        }
    }

    private void assignFreeTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, FlowNextHandlerDTO nextHandlerDTO){

    }

}
