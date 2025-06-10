package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.modules.system.api.SysUserApi;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.workflow.enums.FlowActTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowHandlerTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowNodeTypeEnum;
import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNextNodeQueryDTO;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.*;
import com.ohohmiao.modules.workflow.service.*;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程核心Service实现
 *
 * @author ohohmiao
 * @date 2025-06-08 11:24
 */
@Service
public class FlowServiceImpl implements FlowService {

    @Resource
    private FlowHisDeployService flowHisDeployService;

    @Resource
    private FlowFormService flowFormService;

    @Resource
    private FlowBtnService flowBtnService;

    @Resource
    private FlowNodeService flowNodeService;

    @Resource
    private FlowHandlerService flowHandlerService;

    @Resource(name = "sysUserApi")
    private SysUserApi sysUserApi;

    @Override
    public FlowInfoVO getFlowInfo(FlowInfoQueryDTO queryDTO, boolean includeExtraInfo){
        FlowInfoVO flowInfoVO = new FlowInfoVO();
        FlowDefVO flowDefVO = null;
        if(StrUtil.isNotBlank(queryDTO.getExeId())){

        }else{
            flowInfoVO.setStartFlowFlag(true);
            flowInfoVO.setDefCode(queryDTO.getDefCode());
            flowInfoVO.setDefVersion(queryDTO.getDefVersion());
            flowInfoVO.setDoQueryFlag(false);
            Integer defVersion = ObjectUtil.isNotNull(queryDTO.getDefVersion())? queryDTO.getDefVersion(): 1;
            // 查询指定版本流程定义
            flowDefVO = flowHisDeployService.get(queryDTO.getDefCode(), defVersion, false);
            flowInfoVO.setDefName(flowDefVO.getDefName());
            flowInfoVO.setDefJson(flowDefVO.getDefJson());
            flowInfoVO.setDefXml(flowDefVO.getDefXml());
            // 查询第一个任务节点
            Map firstTaskNode = WorkflowUtil.getFirstTaskNode(flowDefVO.getDefJson());
            String curNodeId = (String)firstTaskNode.get("id");
            FlowNodeVO curNodeInfo = flowNodeService.get(queryDTO.getDefCode(), queryDTO.getDefVersion(), curNodeId);
            flowInfoVO.setCurNodeInfo(curNodeInfo);
            // 当前正在运行的节点ids
            flowInfoVO.setCurRunningNodeIds(curNodeId);
            if(includeExtraInfo){
                // 查询环节绑定的按钮
                List<FlowBtnVO> flowBtnVOList = flowBtnService.listBindBtns(queryDTO.getDefCode(), queryDTO.getDefVersion(), curNodeId);
                flowInfoVO.setFlowBtns(flowBtnVOList);
            }
        }
        if(includeExtraInfo){
            // 查询绑定的流程表单
            FlowFormVO flowFormVO = flowFormService.getBindForm(flowInfoVO.getDefCode(),
                    flowInfoVO.getDefVersion(), flowInfoVO.getCurNodeInfo().getNodeId());
            if(ObjectUtil.isNull(flowFormVO)){
                throw new CommonException("操作出错，流程表单未绑定！");
            }
            flowInfoVO.setFormId(flowFormVO.getFormId());
            flowInfoVO.setFormPath(flowFormVO.getFormPath());
        }

        return flowInfoVO;
    }

    @Override
    public List<FlowTaskNodeVO> getNextNodeList(FlowNextNodeQueryDTO queryDTO){
        FlowInfoVO flowInfoVO = this.getFlowInfo(queryDTO, false);
        List<FlowTaskNodeVO> nextHandlerList = CollectionUtil.newArrayList();
        if(queryDTO.getActType().equals(FlowActTypeEnum.SUBMIT.ordinal())){
            // 流程提交情形
            nextHandlerList = this.getSubmitNextHandlerList(flowInfoVO);
        }else if(queryDTO.getActType().equals(FlowActTypeEnum.RETURN.ordinal())){
            // TODO 流程退回情形

        }
        return nextHandlerList;
    }

    /**
     * 获取提交情形下一步环节办理人配置结果
     * @param flowInfoVO
     * @return
     */
    private List<FlowTaskNodeVO> getSubmitNextHandlerList(FlowInfoVO flowInfoVO){
        List<FlowTaskNodeVO> nextHandlerList = CollectionUtil.newArrayList();
        if(StrUtil.isNotEmpty(flowInfoVO.getCurTaskId())){
            // TODO 从流程任务表查询去往任务信息，组装返回
        }
        // 从流程定义查询下一节点信息
        List<Map> nextNodeList = WorkflowUtil.getNextNodes(
                flowInfoVO.getDefJson(), flowInfoVO.getCurNodeInfo().getNodeId());
        if(nextNodeList.size() == 1){
            // 下一步是单个节点
            Map nextNode = nextNodeList.get(0);
            nextHandlerList = this.getNextFlowHandlerList(
                    flowInfoVO, nextNode, flowInfoVO.getCurNodeInfo().getNodeId());
        }else if(nextNodeList.size() > 1){
            // 下一步是多个节点
            Map<String, List<FlowTaskNodeVO>> subFlowHandlers = new HashMap<>();
            for(int i = 0; i < nextNodeList.size(); i++){
                Map nextNode = nextNodeList.get(i);
                List<FlowTaskNodeVO> thizNextHandlerList = this.getNextFlowHandlerList(
                        flowInfoVO, nextNode, flowInfoVO.getCurNodeInfo().getNodeId());
                subFlowHandlers.put((String)nextNode.get("id"), thizNextHandlerList);
            }
            FlowTaskNodeVO nextHandler = new FlowTaskNodeVO();
            List<FlowTaskNodeVO> nodeList = CollectionUtil.newArrayList();
            for(Map.Entry<String, List<FlowTaskNodeVO>> entry: subFlowHandlers.entrySet()){
                nodeList.addAll(entry.getValue());
            }
            nextHandler.setNodeList(nodeList);
            nextHandlerList.add(nextHandler);
        }
        return nextHandlerList;
    }

    /**
     * 获取下一步环节办理人配置结果
     * @param flowInfoVO
     * @param nextNode
     * @param curNodeId
     * @return
     */
    private List<FlowTaskNodeVO> getNextFlowHandlerList(FlowInfoVO flowInfoVO, Map nextNode, String curNodeId){
        List<FlowTaskNodeVO> nextHandlerList = CollectionUtil.newArrayList();
        String nodeId = (String)nextNode.get("id");
        String nodeName = (String)nextNode.get("name");
        String nodeType = (String)nextNode.get("nodetype");
        if(nodeType.equals(FlowNodeTypeEnum.TASK.getCode())){
            // 任务节点情形
            FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName);
            nextHandlerList.add(nextHandler);
        }else if(nodeType.equals(FlowNodeTypeEnum.END.getCode())){
            // 办结节点情形
            FlowTaskNodeVO nextHandler = new FlowTaskNodeVO();
            nextHandler.setNodeType(FlowNodeTypeEnum.END.getCode());
            nextHandler.setReselectPermit(CommonWhetherEnum.NO.getCode());
            nextHandler.setNodeId(nodeId);
            nextHandler.setNodeName(StrUtil.isNotBlank(nodeName)? nodeName: "办结");
            nextHandlerList.add(nextHandler);
        }else if(nodeType.equals(FlowNodeTypeEnum.DECISION.getCode())){
            // TODO 分支节点情形

        }else if(nodeType.equals(FlowNodeTypeEnum.PARALLEL.getCode())){
            // 并行节点情形
            List<Map> nextTaskNodeList = WorkflowUtil.getNextTaskNodes(flowInfoVO.getDefJson(), nodeId);
            for(Map nextTaskNode: nextTaskNodeList){
                nodeId = (String)nextTaskNode.get("id");
                nodeName = (String)nextTaskNode.get("name");
                FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName);
                nextHandlerList.add(nextHandler);
            }
        }else if(nodeType.equals(FlowNodeTypeEnum.INCLUSIVE.getCode())){
            // 合并节点情形
            List<Map> nextTaskNodeList = WorkflowUtil.getNextTaskNodes(flowInfoVO.getDefJson(), nodeId);
            for(Map nextTaskNode: nextTaskNodeList){
                nodeId = (String)nextTaskNode.get("id");
                nodeName = (String)nextTaskNode.get("name");
                FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName);
                nextHandler.setInclusiveGateWayId(nodeId);
                nextHandlerList.add(nextHandler);
            }
        }
        return nextHandlerList;
    }

    /**
     * 获取任务环节办理人配置结果
     * @param flowInfoVO
     * @param nextNodeId
     * @param nextNodeName
     * @return
     */
    private FlowTaskNodeVO getNextTaskFLowHandler(FlowInfoVO flowInfoVO, String nextNodeId, String nextNodeName){
        FlowTaskNodeVO nextHandlerVO = new FlowTaskNodeVO();
        nextHandlerVO.setNodeType(FlowNodeTypeEnum.TASK.getCode());
        nextHandlerVO.setNodeId(nextNodeId);
        nextHandlerVO.setNodeName(nextNodeName);
        FlowHandlerVO flowHandlerVO = flowHandlerService.getNextNodeFlowHandler(
                          flowInfoVO.getDefCode(), flowInfoVO.getDefVersion(), nextNodeId);
        nextHandlerVO.setMultiHandletype(flowHandlerVO.getMultiHandletype());
        nextHandlerVO.setReselectPermit(flowHandlerVO.getReselectPermit());
        if(flowHandlerVO.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
            // 指定人员情形
            List<SysUserVO> sysUserVOS = sysUserApi.listByReferRes(flowHandlerVO.getTargetReferResList());
            List<FlowTaskHandler> thizHandlers = sysUserVOS.stream().map(u -> {
                FlowTaskHandler thizHandler = new FlowTaskHandler();
                thizHandler.setHandlerId(u.getUserId());
                thizHandler.setHandlerName(u.getUserName());
                return thizHandler;
            }).collect(Collectors.toList());
            nextHandlerVO.setHandlers(thizHandlers);
        }else if(flowHandlerVO.getHandlerType().equals(FlowHandlerTypeEnum.INTERFACE.ordinal())){
            // 指定接口情形

        }else{
            // 自行选择情形
            nextHandlerVO.setReselectPermit(CommonWhetherEnum.YES.getCode());
        }
        // TODO 人员过滤规则

        return nextHandlerVO;
    }

}
