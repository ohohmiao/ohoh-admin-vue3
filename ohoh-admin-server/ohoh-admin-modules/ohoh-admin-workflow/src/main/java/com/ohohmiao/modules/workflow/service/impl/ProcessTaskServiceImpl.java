package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import com.ohohmiao.modules.workflow.model.vo.FlowTaskNodeVO;
import com.ohohmiao.modules.workflow.model.vo.ProcessTaskVO;
import com.ohohmiao.modules.workflow.service.FlowNodeService;
import com.ohohmiao.modules.workflow.service.ProcessTaskService;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private FlowNodeService flowNodeService;

    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Override
    public void assignTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, List<FlowNextHandlerDTO> nextHandlerList){
        // 开始环节，保存开始环节信息；否则，处理当前环节任务
        boolean isNeedAssign = false;
        if(flowInfoVO.getStartFlowFlag()){
            isNeedAssign = this.saveStartNodeTask(flowInfoVO, processForm);
        }else{
            isNeedAssign = this.handleCurTask(flowInfoVO, processForm);
        }
        if(isNeedAssign){
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
    public FlowTaskNodeVO getMultiHandleNodeNextWaitingHandler(String processId, String taskId){
        List<ProcessTaskVO> taskList = processTaskMapper.listMultiHandleNodeNextWaitingTasks(
                processId, taskId, FlowTaskStateEnum.WAITING.ordinal());
        if(CollUtil.isNotEmpty(taskList)){
            ProcessTaskVO task = taskList.get(0);
            FlowTaskNodeVO taskNode = new FlowTaskNodeVO();
            taskNode.setNodeId(task.getTaskNodeid());
            taskNode.setNodeName(task.getTaskNodename());
            taskNode.setNodeType(FlowNodeTypeEnum.TASK.getCode());
            List<FlowTaskHandler> handlers = new ArrayList<>();
            FlowTaskHandler handler = new FlowTaskHandler();
            handler.setHandlerId(task.getHandlerId());
            handler.setHandlerName(task.getHandlerName());
            handler.setHandlerOrgid(task.getHandlerOrgid());
            handler.setHandlerOrgname(task.getHandlerOrgname());
            handlers.add(handler);
            taskNode.setHandlers(handlers);
            taskNode.setMultiHandletype(FlowTaskMultiHandleTypeEnum.SERIAL.ordinal());
            taskNode.setReselectPermit(CommonWhetherEnum.NO.getCode());
            taskNode.setTaskId(task.getTaskId());
            return taskNode;
        }else{
            return null;
        }
    }

    @Override
    public List<ProcessTask> listCurRunningProcessTasks(String processId){
        LambdaQueryWrapper<ProcessTask> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.eq(ProcessTask::getProcessId, processId);
        listWrapper.in(ProcessTask::getTaskState, FlowTaskStateEnum.SUSPENDED.ordinal(),
                FlowTaskStateEnum.WAITING.ordinal(), FlowTaskStateEnum.RUNNING.ordinal());
        listWrapper.isNull(ProcessTask::getParentTaskid);
        return this.list(listWrapper);
    }

    @Override
    public void updateTaskStateCascade(String taskId, Integer taskState){
        ProcessTask task = this.getById(taskId);
        if(StrUtil.isNotEmpty(task.getParentTaskid())){
            LambdaUpdateWrapper<ProcessTask> cascadeUpdateWrapper = new LambdaUpdateWrapper();
            cascadeUpdateWrapper.eq(ProcessTask::getTaskId, task.getParentTaskid());
            cascadeUpdateWrapper.set(ProcessTask::getTaskState, taskState);
            this.update(cascadeUpdateWrapper);
        }
        LambdaUpdateWrapper<ProcessTask> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(ProcessTask::getTaskId, taskId);
        updateWrapper.set(ProcessTask::getTaskState, taskState);
        this.update(updateWrapper);
    }

    @Override
    public boolean isExist(String taskId){
        LambdaQueryWrapper<ProcessTask> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(ProcessTask::getTaskId, taskId);
        return this.count(countWrapper) > 0;
    }

    @Override
    public List<ProcessTask> listHandledTasks(String processId, String taskNodeId, Integer taskAssignType){
        LambdaQueryWrapper<ProcessTask> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.eq(ProcessTask::getProcessId, processId);
        listWrapper.eq(ProcessTask::getTaskNodeid, taskNodeId);
        listWrapper.eq(ProcessTask::getTaskState, FlowTaskStateEnum.HANDLED.ordinal());
        listWrapper.isNull(ProcessTask::getOutgoingTaskids);
        listWrapper.orderByDesc(ProcessTask::getTaskStarttime);
        List<ProcessTask> taskList = this.list(listWrapper);
        ProcessTask thizTask = taskList.get(0);
        if(taskAssignType == FlowTaskAssignTypeEnum.SINGLE.ordinal()){
            return CollUtil.newArrayList(thizTask);
        }else{
            LambdaQueryWrapper<ProcessTask> listGroupWrapper = new LambdaQueryWrapper<>();
            listGroupWrapper.eq(ProcessTask::getProcessId, processId);
            listGroupWrapper.eq(ProcessTask::getTaskNodeid, taskNodeId);
            listGroupWrapper.eq(ProcessTask::getTaskState, FlowTaskStateEnum.HANDLED.ordinal());
            listGroupWrapper.isNull(ProcessTask::getOutgoingTaskids);
            listGroupWrapper.eq(ProcessTask::getTaskGroupid, thizTask.getTaskGroupid());
            listGroupWrapper.orderByDesc(ProcessTask::getTaskStarttime);
            return this.list(listGroupWrapper);
        }
    }

    @Override
    public String getMultiHandleNodeOutgoingTaskids(String taskId){
        ProcessTask curTask = this.getById(taskId);
        LambdaQueryWrapper<ProcessTask> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.eq(ProcessTask::getProcessId, curTask.getProcessId());
        listWrapper.eq(ProcessTask::getTaskNodeid, curTask.getTaskNodeid());
        listWrapper.eq(ProcessTask::getTaskState, FlowTaskStateEnum.RETURNED.ordinal());
        listWrapper.eq(ProcessTask::getTaskGroupid, curTask.getTaskGroupid());
        listWrapper.orderByAsc(ProcessTask::getTaskId);
        List<ProcessTask> taskList = this.list(listWrapper);
        return taskList.stream().map(ProcessTask::getTaskId).collect(Collectors.joining(","));
    }

    @Override
    public List<ProcessTaskVO> listTasksByGroup(String[] taskIds){
        QueryWrapper<ProcessTask> listWrapper = new QueryWrapper<>();
        listWrapper.select("task_nodeid, task_nodename, " +
                "GROUP_CONCAT(handler_id) as handler_id, " +
                "GROUP_CONCAT(handler_name) as handler_name, " +
                "GROUP_CONCAT(handler_orgid) as handler_orgid, " +
                "GROUP_CONCAT(handler_orgname) as handler_orgname");
        listWrapper.in("task_id", taskIds);
        listWrapper.groupBy("task_nodeid, task_nodename");
        List<Map<String, Object>> list = processTaskMapper.selectMaps(listWrapper);
        return list.stream().map(m -> BeanUtil.toBean(m, ProcessTaskVO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProcessTask> listProcessTaskLogs(String processId){
        LambdaQueryWrapper<ProcessTask> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.isNull(ProcessTask::getParentTaskid);
        listWrapper.eq(ProcessTask::getProcessId, processId);
        listWrapper.orderByAsc(ProcessTask::getTaskId);
        return this.list(listWrapper);
    }

    private boolean saveStartNodeTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm){
        ProcessTask startTask = new ProcessTask();
        startTask.setProcessId(flowInfoVO.getProcessId());
        startTask.setTaskState(FlowTaskStateEnum.HANDLED.ordinal());
        FlowNodeVO curNodeInfo = flowInfoVO.getCurNodeInfo();
        startTask.setTaskNodeid(curNodeInfo.getNodeId());
        startTask.setTaskNodename(curNodeInfo.getNodeName());
        startTask.setAssignHandlerids(flowInfoVO.getCreatorId());
        startTask.setAssignHandlernames(flowInfoVO.getCreatorName());
        startTask.setAssignHandlerorgids(flowInfoVO.getCreatorOrgid());
        startTask.setAssignHandlerorgnames(flowInfoVO.getCreatorOrgname());
        startTask.setHandlerId(flowInfoVO.getCreatorId());
        startTask.setHandlerName(flowInfoVO.getCreatorName());
        startTask.setHandlerOrgid(flowInfoVO.getCreatorOrgid());
        startTask.setHandlerOrgname(flowInfoVO.getCreatorOrgname());
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

    private boolean handleCurTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm){
        LocalDateTime curDateTime = LocalDateTime.now();

        ProcessTask curTask = this.getById(flowInfoVO.getCurTaskId());
        curTask.setApprovalResult(processForm.getAppovalResult());
        curTask.setHandleOpinion(processForm.getHandleOpinion());
        curTask.setTaskEndtime(curDateTime);
        long consumeSeconds = Duration.between(curTask.getTaskStarttime(), curDateTime).getSeconds();
        curTask.setConsumeSeconds(consumeSeconds);
        if(flowInfoVO.getActType() != FlowActTypeEnum.RESTART.ordinal()){
            if(curTask.getTaskDeadline() != null){
                if(curDateTime.isAfter(curTask.getTaskDeadline())){
                    curTask.setOvertimeFlag(CommonWhetherEnum.YES.getCode());
                    long exceedSeconds = Duration.between(curTask.getTaskDeadline(), curDateTime).getSeconds();
                    curTask.setExceedSeconds(exceedSeconds);
                }else{
                    curTask.setOvertimeFlag(CommonWhetherEnum.NO.getCode());
                }
            }
        }

        FlowNodeVO curNodeInfo = flowNodeService.get(flowInfoVO.getDefCode(),
                flowInfoVO.getDefVersion(), flowInfoVO.getCurNodeInfo().getNodeId());
        if(curNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.SINGLE.ordinal()){
            return this.handleSingleTask(flowInfoVO, curTask);
        }else if(curNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.MULTI.ordinal()){
            return this.handleMultiTask(flowInfoVO, curTask);
        }else if(curNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.FREE.ordinal()){
            return this.handleFreeTask(flowInfoVO, curTask);
        }
        return false;
    }

    private boolean handleSingleTask(FlowInfoVO flowInfoVO, ProcessTask curTask){
        boolean isNeedAssign = true;
        LocalDateTime curDateTime = LocalDateTime.now();
        if(flowInfoVO.getActType() == FlowActTypeEnum.JUMP.ordinal()){
            String parentTaskId = curTask.getParentTaskid();
            if(StrUtil.isEmpty(parentTaskId)){
                parentTaskId = flowInfoVO.getCurTaskId();
            }
            flowInfoVO.setCurTaskId(parentTaskId);
            ProcessTask parentTask = this.getById(parentTaskId);
            // 删除其它待办任务
            LambdaUpdateWrapper<ProcessTask> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(ProcessTask::getParentTaskid, parentTaskId);
            deleteWrapper.ne(ProcessTask::getTaskId, parentTaskId); //TODO ???
            this.remove(deleteWrapper);
            // 处理当前任务
            parentTask.setTaskState(FlowTaskStateEnum.JUMPED.ordinal());
            parentTask.setTaskEndtime(curTask.getTaskEndtime());
            parentTask.setConsumeSeconds(curTask.getConsumeSeconds());
            parentTask.setOvertimeFlag(curTask.getOvertimeFlag());
            parentTask.setExceedSeconds(curTask.getExceedSeconds());
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            parentTask.setAssignHandlerids(loginUser.getUserId());
            parentTask.setAssignHandlernames(loginUser.getUserName());
            parentTask.setAssignHandlerorgids(loginUser.getSwitchOrg().getOrgId());
            parentTask.setAssignHandlerorgnames(loginUser.getSwitchOrg().getOrgName());
            this.updateById(parentTask);
        }else{
            // 删除其它待办任务
            LambdaUpdateWrapper<ProcessTask> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(ProcessTask::getParentTaskid, curTask.getParentTaskid());
            deleteWrapper.ne(ProcessTask::getTaskId, flowInfoVO.getCurTaskId());
            this.remove(deleteWrapper);
            // 处理当前任务
            Integer taskState = FlowTaskStateEnum.HANDLED.ordinal();
            if(flowInfoVO.getActType() == FlowActTypeEnum.SUBMIT.ordinal()){
                taskState = FlowTaskStateEnum.HANDLED.ordinal();
            }else if(flowInfoVO.getActType() == FlowActTypeEnum.RETURN.ordinal()){
                taskState = FlowTaskStateEnum.RETURNED.ordinal();
            }else if(flowInfoVO.getActType() == FlowActTypeEnum.RESTART.ordinal()){
                taskState = FlowTaskStateEnum.RESTARTED.ordinal();
                ProcessTask parentTask = this.getById(curTask.getParentTaskid());
                ProcessTask newParentTask = BeanUtil.copyProperties(parentTask, ProcessTask.class);
                newParentTask.setTaskId(null);
                newParentTask.setApprovalResult(null);
                newParentTask.setHandleOpinion(null);
                newParentTask.setTaskEndtime(null);
                newParentTask.setConsumeSeconds(null);
                newParentTask.setOvertimeFlag(null);
                newParentTask.setExceedSeconds(null);
                newParentTask.setTaskStarttime(curDateTime);
                newParentTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
                this.save(newParentTask);
                ProcessTask thizCurTask = this.getById(flowInfoVO.getCurTaskId());
                ProcessTask newTask = BeanUtil.copyProperties(thizCurTask, ProcessTask.class);
                newTask.setTaskId(null);
                newTask.setApprovalResult(null);
                newTask.setHandleOpinion(null);
                newTask.setTaskEndtime(null);
                newTask.setConsumeSeconds(null);
                newTask.setOvertimeFlag(null);
                newTask.setExceedSeconds(null);
                newTask.setParentTaskid(newParentTask.getTaskId());
                newTask.setTaskStarttime(curDateTime);
                newTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
                this.save(newTask);
                isNeedAssign = false;
            }else if(flowInfoVO.getActType() == FlowActTypeEnum.TRANSFER.ordinal()){
                taskState = FlowTaskStateEnum.TRANSFERED.ordinal();
            }
            curTask.setTaskState(taskState);
            this.updateById(curTask);
            if(StrUtil.isNotEmpty(curTask.getParentTaskid())){
                ProcessTask parentTask = this.getById(curTask.getParentTaskid());
                parentTask.setTaskState(taskState);
                parentTask.setAssignHandlerids(curTask.getAssignHandlerids());
                parentTask.setAssignHandlernames(curTask.getAssignHandlernames());
                parentTask.setAssignHandlerorgids(curTask.getAssignHandlerorgids());
                parentTask.setAssignHandlerorgnames(curTask.getAssignHandlerorgnames());
                parentTask.setApprovalResult(curTask.getApprovalResult());
                parentTask.setHandleOpinion(curTask.getHandleOpinion());
                parentTask.setTaskEndtime(curTask.getTaskEndtime());
                parentTask.setConsumeSeconds(curTask.getConsumeSeconds());
                parentTask.setOvertimeFlag(curTask.getOvertimeFlag());
                parentTask.setExceedSeconds(curTask.getExceedSeconds());
                this.updateById(parentTask);
            }
        }
        return isNeedAssign;
    }

    private boolean handleMultiTask(FlowInfoVO flowInfoVO, ProcessTask curTask){
        LocalDateTime curDateTime = LocalDateTime.now();
        // 处理当前任务
        Integer taskState = FlowTaskStateEnum.HANDLED.ordinal();
        if(flowInfoVO.getActType() == FlowActTypeEnum.SUBMIT.ordinal()){
            taskState = FlowTaskStateEnum.HANDLED.ordinal();
        }else if(flowInfoVO.getActType() == FlowActTypeEnum.RETURN.ordinal()){
            taskState = FlowTaskStateEnum.RETURNED.ordinal();
        }else if(flowInfoVO.getActType() == FlowActTypeEnum.RESTART.ordinal()){
            taskState = FlowTaskStateEnum.RESTARTED.ordinal();
            ProcessTask thizCurTask = this.getById(flowInfoVO.getCurTaskId());
            ProcessTask newTask = BeanUtil.copyProperties(thizCurTask, ProcessTask.class);
            newTask.setTaskId(null);
            newTask.setApprovalResult(null);
            newTask.setHandleOpinion(null);
            newTask.setTaskEndtime(null);
            newTask.setConsumeSeconds(null);
            newTask.setOvertimeFlag(null);
            newTask.setExceedSeconds(null);
            newTask.setTaskStarttime(curDateTime);
            newTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
            this.save(newTask);
        }else if(flowInfoVO.getActType() == FlowActTypeEnum.TRANSFER.ordinal()){
            taskState = FlowTaskStateEnum.TRANSFERED.ordinal();
        }else if(flowInfoVO.getActType() == FlowActTypeEnum.JUMP.ordinal()){
            taskState = FlowTaskStateEnum.JUMPED.ordinal();
            LambdaUpdateWrapper<ProcessTask> deleteWrapper = new LambdaUpdateWrapper<>();
            deleteWrapper.eq(ProcessTask::getTaskGroupid, curTask.getTaskGroupid());
            deleteWrapper.ne(ProcessTask::getTaskId, flowInfoVO.getCurTaskId());
            this.remove(deleteWrapper);
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            curTask.setAssignHandlerids(loginUser.getUserId());
            curTask.setAssignHandlernames(loginUser.getUserName());
            curTask.setAssignHandlerorgids(loginUser.getSwitchOrg().getOrgId());
            curTask.setAssignHandlerorgnames(loginUser.getSwitchOrg().getOrgName());
        }
        curTask.setTaskState(taskState);
        this.updateById(curTask);
        // 处理串审任务
        if(curTask.getMultiHandletype() == FlowTaskMultiHandleTypeEnum.SERIAL.ordinal() &&
            flowInfoVO.getActType() != FlowActTypeEnum.RESTART.ordinal() &&
            flowInfoVO.getActType() != FlowActTypeEnum.TRANSFER.ordinal()){
            FlowTaskNodeVO nextTaskNode = this.getMultiHandleNodeNextWaitingHandler(
                    flowInfoVO.getProcessId(), flowInfoVO.getCurTaskId());
            if(nextTaskNode != null){
                LambdaUpdateWrapper<ProcessTask> updateStateWrapper = new LambdaUpdateWrapper<>();
                updateStateWrapper.set(ProcessTask::getTaskState, FlowTaskStateEnum.RUNNING.ordinal());
                updateStateWrapper.eq(ProcessTask::getTaskId, nextTaskNode.getTaskId());
                this.update(updateStateWrapper);
            }
        }
        if(flowInfoVO.getActType() == FlowActTypeEnum.TRANSFER.ordinal()){
            return true;
        }
        // 判断是否存在未办结的任务
        LambdaQueryWrapper<ProcessTask> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(ProcessTask::getProcessId, flowInfoVO.getProcessId());
        countWrapper.eq(ProcessTask::getTaskNodeid, flowInfoVO.getCurNodeInfo().getNodeId());
        countWrapper.in(ProcessTask::getTaskState, FlowTaskStateEnum.SUSPENDED.ordinal(),
                FlowTaskStateEnum.WAITING.ordinal(), FlowTaskStateEnum.RUNNING.ordinal());
        countWrapper.ne(ProcessTask::getTaskId, flowInfoVO.getCurTaskId());
        return this.count(countWrapper) <= 0;
    }

    private boolean handleFreeTask(FlowInfoVO flowInfoVO, ProcessTask curTask) {
        LocalDateTime curDateTime = LocalDateTime.now();
        // 处理当前任务
        Integer taskState = FlowTaskStateEnum.HANDLED.ordinal();
        if (flowInfoVO.getActType() == FlowActTypeEnum.SUBMIT.ordinal()) {
            taskState = FlowTaskStateEnum.HANDLED.ordinal();
        } else if (flowInfoVO.getActType() == FlowActTypeEnum.RETURN.ordinal()) {
            taskState = FlowTaskStateEnum.RETURNED.ordinal();
        } else if (flowInfoVO.getActType() == FlowActTypeEnum.RESTART.ordinal()) {
            taskState = FlowTaskStateEnum.RESTARTED.ordinal();
            ProcessTask thizCurTask = this.getById(flowInfoVO.getCurTaskId());
            ProcessTask newTask = BeanUtil.copyProperties(thizCurTask, ProcessTask.class);
            newTask.setTaskId(null);
            newTask.setApprovalResult(null);
            newTask.setHandleOpinion(null);
            newTask.setTaskEndtime(null);
            newTask.setConsumeSeconds(null);
            newTask.setOvertimeFlag(null);
            newTask.setExceedSeconds(null);
            newTask.setTaskStarttime(curDateTime);
            newTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
            this.save(newTask);
        } else if (flowInfoVO.getActType() == FlowActTypeEnum.TRANSFER.ordinal()) {
            taskState = FlowTaskStateEnum.TRANSFERED.ordinal();
        } else if (flowInfoVO.getActType() == FlowActTypeEnum.JUMP.ordinal()) {
            taskState = FlowTaskStateEnum.JUMPED.ordinal();
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            curTask.setAssignHandlerids(loginUser.getUserId());
            curTask.setAssignHandlernames(loginUser.getUserName());
            curTask.setAssignHandlerorgids(loginUser.getSwitchOrg().getOrgId());
            curTask.setAssignHandlerorgnames(loginUser.getSwitchOrg().getOrgName());
        }
        curTask.setTaskState(taskState);
        this.updateById(curTask);
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
        String nextHandlerOrgids = nextHandlers.stream().map(
                FlowTaskHandler::getHandlerOrgid).collect(Collectors.joining(","));
        String nextHandlerOrgnames = nextHandlers.stream().map(
                FlowTaskHandler::getHandlerOrgname).collect(Collectors.joining(","));

        ProcessTask parentTask = new ProcessTask();
        parentTask.setProcessId(flowInfoVO.getProcessId());
        parentTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
        parentTask.setTaskNodeid(nextHandlerDTO.getNodeId());
        parentTask.setTaskNodename(nextHandlerDTO.getNodeName());
        if(actType == FlowActTypeEnum.TRANSFER.ordinal() || actType == FlowActTypeEnum.JUMP.ordinal()){
            // 转办/跳转
            ProcessTask curTask = this.getById(flowInfoVO.getCurTaskId());
            parentTask.setIncomingNodeid(curTask.getIncomingNodeid());
            parentTask.setIncomingNodename(curTask.getIncomingNodename());
        }else if(actType == FlowActTypeEnum.RETURN.ordinal()){
            // 退回
            List<ProcessTask> incomingTaskList = this.listReturnNodeIncomingTasks(flowInfoVO, nextHandlerDTO.getNodeId());
            if(CollUtil.isNotEmpty(incomingTaskList)){
                ProcessTask incomingTask = incomingTaskList.get(0);
                parentTask.setIncomingNodeid(incomingTask.getTaskNodeid());
                parentTask.setIncomingNodename(incomingTask.getTaskNodename());
            }
        }else{
            parentTask.setIncomingNodeid(curNodeInfo.getNodeId());
            parentTask.setIncomingNodename(curNodeInfo.getNodeName());
        }
        parentTask.setOutgoingTaskids(nextHandlerDTO.getOutgoingTaskids());
        parentTask.setMultiHandletype(nextHandlerDTO.getMultiHandletype());
        parentTask.setAssignHandlerids(nextHandlerIds);
        parentTask.setAssignHandlernames(nextHandlerNames);
        parentTask.setAssignHandlerorgids(nextHandlerOrgids);
        parentTask.setAssignHandlerorgnames(nextHandlerOrgnames);
        parentTask.setTaskStarttime(curDateTime);
        parentTask.setTaskDeadline(processForm.getHandleDeadline());
        this.save(parentTask);

        for(FlowTaskHandler nextHandler: nextHandlers){
            ProcessTask subTask = BeanUtil.copyProperties(parentTask, ProcessTask.class);
            subTask.setTaskId(null);
            subTask.setAssignHandlerids(null);
            subTask.setAssignHandlernames(null);
            subTask.setAssignHandlerorgids(null);
            subTask.setAssignHandlerorgnames(null);
            subTask.setParentTaskid(parentTask.getTaskId());
            subTask.setHandlerId(nextHandler.getHandlerId());
            subTask.setHandlerName(nextHandler.getHandlerName());
            subTask.setHandlerOrgid(nextHandler.getHandlerOrgid());
            subTask.setHandlerOrgname(nextHandler.getHandlerOrgname());
            this.save(subTask);
        }
    }

    /**
     * 获取目标退回节点的来源任务列表
     * @param flowInfoVO
     * @param nodeId
     * @return
     */
    private List<ProcessTask> listReturnNodeIncomingTasks(FlowInfoVO flowInfoVO, String nodeId){
        Set<String> incomingNodeIds = WorkflowUtil.getInComingNodeIds(flowInfoVO.getDefJson(), nodeId);
        LambdaQueryWrapper<ProcessTask> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.eq(ProcessTask::getProcessId, flowInfoVO.getProcessId());
        listWrapper.eq(ProcessTask::getTaskState, FlowTaskStateEnum.HANDLED.ordinal());
        listWrapper.isNull(ProcessTask::getParentTaskid);
        listWrapper.in(ProcessTask::getTaskNodeid, incomingNodeIds);
        listWrapper.orderByDesc(ProcessTask::getTaskId);
        return this.list(listWrapper);
    }

    private void assignMultiTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, FlowNextHandlerDTO nextHandlerDTO){
        LocalDateTime curDateTime = LocalDateTime.now();
        FlowNodeVO curNodeInfo = flowInfoVO.getCurNodeInfo();
        String curNodeId = curNodeInfo.getNodeId();
        String curNodeName = curNodeInfo.getNodeName();
        Integer actType = flowInfoVO.getActType();
        if(actType == FlowActTypeEnum.TRANSFER.ordinal() || actType == FlowActTypeEnum.JUMP.ordinal()){
            ProcessTask curTask = this.getById(flowInfoVO.getCurTaskId());
            curNodeId = curTask.getIncomingNodeid();
            curNodeName = curTask.getIncomingNodename();
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
            subTask.setOutgoingTaskids(nextHandlerDTO.getOutgoingTaskids());
            subTask.setMultiHandletype(nextHandlerDTO.getMultiHandletype());
            subTask.setTaskGroupid(taskGroupid);
            subTask.setAssignHandlerids(nextHandler.getHandlerId());
            subTask.setAssignHandlernames(nextHandler.getHandlerName());
            subTask.setAssignHandlerorgids(nextHandler.getHandlerOrgid());
            subTask.setAssignHandlerorgnames(nextHandler.getHandlerOrgname());
            subTask.setHandlerId(nextHandler.getHandlerId());
            subTask.setHandlerName(nextHandler.getHandlerName());
            subTask.setHandlerOrgid(nextHandler.getHandlerOrgid());
            subTask.setHandlerOrgname(nextHandler.getHandlerOrgname());
            subTask.setTaskStarttime(curDateTime);
            subTask.setTaskDeadline(processForm.getHandleDeadline());
            this.save(subTask);
        }
    }

    private void assignFreeTask(FlowInfoVO flowInfoVO, FlowProcessForm processForm, FlowNextHandlerDTO nextHandlerDTO){
        LocalDateTime curDateTime = LocalDateTime.now();
        FlowNodeVO curNodeInfo = flowInfoVO.getCurNodeInfo();
        String curNodeId = curNodeInfo.getNodeId();
        String curNodeName = curNodeInfo.getNodeName();
        Integer actType = flowInfoVO.getActType();
        if(actType == FlowActTypeEnum.TRANSFER.ordinal() || actType == FlowActTypeEnum.JUMP.ordinal()){
            ProcessTask curTask = this.getById(flowInfoVO.getCurTaskId());
            curNodeId = curTask.getIncomingNodeid();
            curNodeName = curTask.getIncomingNodename();
        }
        List<FlowTaskHandler> nextHandlers = nextHandlerDTO.getHandlers();

        for(int i = 0; i < nextHandlers.size(); i++){
            FlowTaskHandler nextHandler = nextHandlers.get(i);
            ProcessTask subTask = new ProcessTask();
            subTask.setProcessId(flowInfoVO.getProcessId());
            subTask.setTaskState(FlowTaskStateEnum.RUNNING.ordinal());
            subTask.setTaskNodeid(nextHandlerDTO.getNodeId());
            subTask.setTaskNodename(nextHandlerDTO.getNodeName());
            subTask.setIncomingNodeid(curNodeId);
            subTask.setIncomingNodename(curNodeName);
            subTask.setOutgoingTaskids(nextHandlerDTO.getOutgoingTaskids());
            subTask.setMultiHandletype(nextHandlerDTO.getMultiHandletype());
            subTask.setAssignHandlerids(nextHandler.getHandlerId());
            subTask.setAssignHandlernames(nextHandler.getHandlerName());
            subTask.setAssignHandlerorgids(nextHandler.getHandlerOrgid());
            subTask.setAssignHandlerorgnames(nextHandler.getHandlerOrgname());
            subTask.setHandlerId(nextHandler.getHandlerId());
            subTask.setHandlerName(nextHandler.getHandlerName());
            subTask.setHandlerOrgid(nextHandler.getHandlerOrgid());
            subTask.setHandlerOrgname(nextHandler.getHandlerOrgname());
            subTask.setTaskStarttime(curDateTime);
            subTask.setTaskDeadline(processForm.getHandleDeadline());
            this.save(subTask);
        }
    }

}
