package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.modules.system.api.SysUserApi;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.workflow.annotation.FlowEntity;
import com.ohohmiao.modules.workflow.enums.FlowActTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowEventTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowHandlerTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowNodeTypeEnum;
import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNextNodeQueryDTO;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.*;
import com.ohohmiao.modules.workflow.service.*;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程核心Service实现
 *
 * @author ohohmiao
 * @date 2025-06-08 11:24
 */
@Slf4j
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

    @Resource
    private FlowEventService flowEventService;

    @Resource(name = "sysUserApi")
    private SysUserApi sysUserApi;

    @Override
    public FlowInfoVO getFlowInfo(FlowInfoQueryDTO queryDTO, boolean includeExtraInfo){
        FlowInfoVO flowInfoVO = new FlowInfoVO();
        FlowDefVO flowDefVO = null;
        if(StrUtil.isNotBlank(queryDTO.getProcessId())){
            // TODO 从流程实例表+流程任务表获取
        }else{
            flowInfoVO.setStartFlowFlag(true);
            flowInfoVO.setDefCode(queryDTO.getDefCode());
            flowInfoVO.setDefVersion(queryDTO.getDefVersion());
            flowInfoVO.setDoQueryFlag(false);
            Integer defVersion = ObjectUtil.isNotNull(queryDTO.getDefVersion())? queryDTO.getDefVersion(): 1;
            // 查询指定版本流程定义
            flowDefVO = flowHisDeployService.get(queryDTO.getDefCode(), defVersion, false);
            // 查询第一个任务节点
            Map firstTaskNode = WorkflowUtil.getFirstTaskNode(flowDefVO.getDefJson());
            String curNodeId = (String)firstTaskNode.get("id");
            FlowNodeVO curNodeInfo = flowNodeService.get(queryDTO.getDefCode(), queryDTO.getDefVersion(), curNodeId);
            if(ObjectUtil.isNull(curNodeInfo)){
                throw new CommonException("操作失败，未配置流程环节属性！");
            }
            flowInfoVO.setCurNodeInfo(curNodeInfo);
            // 当前正在运行的节点ids
            flowInfoVO.setCurRunningNodeIds(curNodeId);
        }
        flowInfoVO.setFlowEntityClassName(flowDefVO.getFlowentityClassname());
        flowInfoVO.setDefName(flowDefVO.getDefName());
        flowInfoVO.setDefJson(flowDefVO.getDefJson());
        flowInfoVO.setDefXml(flowDefVO.getDefXml());
        if(includeExtraInfo){
            // 查询绑定的流程表单
            FlowFormVO flowFormVO = flowFormService.getBindForm(flowInfoVO.getDefCode(),
                    flowInfoVO.getDefVersion(), flowInfoVO.getCurNodeInfo().getNodeId());
            if(ObjectUtil.isNull(flowFormVO)){
                throw new CommonException("操作失败，未绑定流程环节表单！");
            }
            flowInfoVO.setFormId(flowFormVO.getFormId());
            flowInfoVO.setFormPath(flowFormVO.getFormPath());
            // 查询环节绑定的按钮
            List<FlowBtnVO> flowBtnVOList = flowBtnService.listBindBtns(
                    queryDTO.getDefCode(), queryDTO.getDefVersion(), flowInfoVO.getCurNodeInfo().getNodeId());
            flowInfoVO.setFlowBtns(flowBtnVOList);
        }
        // 执行业务数据读取事件
        if(flowEventService.executeBindEvent(flowInfoVO, FlowEventTypeEnum.READ.ordinal()) == null){
            this.executeDefaultReadEvent(flowInfoVO);
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
     * 执行默认的业务数据读取事件
     * @param flowInfoVO
     */
    private void executeDefaultReadEvent(FlowInfoVO flowInfoVO){
        if(StrUtil.isNotBlank(flowInfoVO.getProcessId())){
            // TODO 从流程实例表获取业务表数据
        }else{
            try {
                Class<?> clazz = Class.forName(flowInfoVO.getFlowEntityClassName());
                flowInfoVO.setEntityVO(clazz.getAnnotation(FlowEntity.class).value().newInstance());
            } catch (Exception e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                throw new CommonException(String.format("操作失败，获取流程业务实体异常！"));
            }
        }
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
            List<FlowTaskNodeVO> nodeList = CollectionUtil.newArrayList();
            for(int i = 0; i < nextNodeList.size(); i++){
                Map nextNode = nextNodeList.get(i);
                List<FlowTaskNodeVO> thizNextHandlerList = this.getNextFlowHandlerList(
                        flowInfoVO, nextNode, flowInfoVO.getCurNodeInfo().getNodeId());
                nodeList.addAll(thizNextHandlerList);
            }
            FlowTaskNodeVO nextHandler = new FlowTaskNodeVO();
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
            FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName, nodeType);
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
            // 分支节点情形
            flowInfoVO.getCurNodeInfo().setNodeId(nodeId);
            flowInfoVO = flowEventService.executeBindEvent(flowInfoVO, FlowEventTypeEnum.DECIDE.ordinal());
            if(ObjectUtil.isNull(flowInfoVO) || CollectionUtil.isEmpty(flowInfoVO.getNextTaskNodeIds())){
                throw new CommonException("操作失败，未给分支判断节点配置流程事件！");
            }
            flowInfoVO.getCurNodeInfo().setNodeId(curNodeId);
            Set<String> decideNextNodeIds = flowInfoVO.getNextTaskNodeIds();
            for(String decideNextNodeId: decideNextNodeIds){
                Map decideNextNode = WorkflowUtil.getFlowNode(flowInfoVO.getDefJson(), decideNextNodeId);
                nodeId = (String)decideNextNode.get("id");
                nodeName = (String)decideNextNode.get("name");
                nodeType = (String)decideNextNode.get("nodetype");
                FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName, nodeType);
                nextHandlerList.add(nextHandler);
            }
        }else if(nodeType.equals(FlowNodeTypeEnum.PARALLEL.getCode())){
            // 并行节点情形
            List<Map> nextTaskNodeList = WorkflowUtil.getNextTaskNodes(flowInfoVO.getDefJson(), nodeId);
            for(Map nextTaskNode: nextTaskNodeList){
                nodeId = (String)nextTaskNode.get("id");
                nodeName = (String)nextTaskNode.get("name");
                nodeType = (String)nextTaskNode.get("nodetype");
                FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName, nodeType);
                nextHandlerList.add(nextHandler);
            }
        }else if(nodeType.equals(FlowNodeTypeEnum.INCLUSIVE.getCode())){
            // 合并节点情形
            List<Map> nextTaskNodeList = WorkflowUtil.getNextTaskNodes(flowInfoVO.getDefJson(), nodeId);
            for(Map nextTaskNode: nextTaskNodeList){
                nodeId = (String)nextTaskNode.get("id");
                nodeName = (String)nextTaskNode.get("name");
                nodeType = (String)nextTaskNode.get("nodetype");
                FlowTaskNodeVO nextHandler = this.getNextTaskFLowHandler(flowInfoVO, nodeId, nodeName, nodeType);
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
     * @param nextNodeType
     * @return
     */
    private FlowTaskNodeVO getNextTaskFLowHandler(FlowInfoVO flowInfoVO, String nextNodeId,
                                                  String nextNodeName, String nextNodeType){
        FlowTaskNodeVO nextHandlerVO = new FlowTaskNodeVO();
        nextHandlerVO.setNodeType(nextNodeType);
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
            try {
                String[] interfaceCode = flowHandlerVO.getInterfaceCode().split("\\.");
                String beanId = interfaceCode[0];
                String method = interfaceCode[1];
                Object serviceBean = SpringUtil.getBean(beanId);
                Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, FlowInfoVO.class);
                List<FlowTaskHandler> thizHandlers = (List<FlowTaskHandler>)invokeMethod.invoke(serviceBean, flowInfoVO);
                nextHandlerVO.setHandlers(thizHandlers);
            } catch (Exception e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                throw new CommonException(String.format("指定接口%s调用错误，请检查流程配置！", flowHandlerVO.getInterfaceCode()));
            }
        }else{
            // 自行选择情形
            List<FlowTaskHandler> thizHandlers = CollectionUtil.newArrayList();
            nextHandlerVO.setHandlers(thizHandlers);
            nextHandlerVO.setReselectPermit(CommonWhetherEnum.YES.getCode());
        }
        // 人员过滤规则
        if(CollectionUtil.isNotEmpty(nextHandlerVO.getHandlers()) && StrUtil.isNotBlank(flowHandlerVO.getFilterRule())){
            try {
                String[] filterRule = flowHandlerVO.getFilterRule().split("\\.");
                String beanId = filterRule[0];
                String method = filterRule[1];
                Object serviceBean = SpringUtil.getBean(beanId);
                Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, FlowInfoVO.class, List.class);
                List<FlowTaskHandler> thizHandlers = (List<FlowTaskHandler>)invokeMethod.invoke(
                        serviceBean, flowInfoVO, nextHandlerVO.getHandlers());
                nextHandlerVO.setHandlers(thizHandlers);
            } catch (Exception e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                throw new CommonException(String.format("过滤规则%s调用错误，请检查流程配置！", flowHandlerVO.getFilterRule()));
            }
        }
        return nextHandlerVO;
    }

}
