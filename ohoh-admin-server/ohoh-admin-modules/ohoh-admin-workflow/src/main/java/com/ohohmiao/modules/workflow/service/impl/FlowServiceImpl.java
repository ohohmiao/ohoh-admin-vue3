package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.api.SysUserApi;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.workflow.annotation.FlowEntity;
import com.ohohmiao.modules.workflow.enums.*;
import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNextNodeQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowSubmitDTO;
import com.ohohmiao.modules.workflow.model.entity.ProcessInstance;
import com.ohohmiao.modules.workflow.model.entity.ProcessTask;
import com.ohohmiao.modules.workflow.model.pojo.FlowProcessForm;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.*;
import com.ohohmiao.modules.workflow.service.*;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    @Resource
    private ProcessInstanceService processInstanceService;

    @Resource
    private ProcessTaskService processTaskService;

    @Override
    public FlowInfoVO getFlowInfo(FlowInfoQueryDTO queryDTO, boolean includeExtraInfo){
        FlowInfoVO flowInfoVO = new FlowInfoVO();
        FlowDefVO flowDefVO = null;
        if(StrUtil.isNotBlank(queryDTO.getProcessId())){
            // TODO 从流程实例表+流程任务表获取
            flowInfoVO.setStartFlowFlag(false);
            ProcessInstance processInstance = processInstanceService.getById(queryDTO.getProcessId());
            if(ObjectUtil.isNull(processInstance)){
                throw new CommonException("操作失败，不存在的流程实例！");
            }
            flowInfoVO.setDefCode(processInstance.getDefCode());
            flowInfoVO.setDefVersion(processInstance.getDefVersion());
            flowInfoVO.setProcessId(processInstance.getProcessId());
            flowInfoVO.setCurTaskId(queryDTO.getCurTaskId());
            flowInfoVO.setProcessSubject(processInstance.getProcessSubject());
            flowInfoVO.setCreatorId(processInstance.getCreatorId());
            flowInfoVO.setCreatorName(processInstance.getCreatorName());
            flowInfoVO.setCreatorOrgid(processInstance.getCreatorOrgid());
            flowInfoVO.setCreatorOrgname(processInstance.getCreatorOrgname());
            flowInfoVO.setCurRunningNodeIds(processInstance.getCurrunningNodeids());
            flowInfoVO.setBusTableName(processInstance.getBusTablename());
            flowInfoVO.setBusRecordId(processInstance.getBusRecordid());
            // 查询指定版本流程定义
            flowDefVO = flowHisDeployService.get(processInstance.getDefCode(), processInstance.getDefVersion(), false);
            if(StrUtil.isNotBlank(queryDTO.getCurTaskId())){
                ProcessTask curTask = processTaskService.getById(queryDTO.getCurTaskId());
                FlowNodeVO curNodeInfo = flowNodeService.get(queryDTO.getDefCode(), queryDTO.getDefVersion(), curTask.getTaskNodeid());
                if(ObjectUtil.isNull(curNodeInfo)){
                    throw new CommonException("操作失败，流程环节属性未配置！");
                }
                flowInfoVO.setCurNodeInfo(curNodeInfo);
                flowInfoVO.setDoQueryFlag(false);
            }else{
                // TODO 需要考虑查阅情形，当前操作节点的回填？
                // 查阅情况，无当前操作节点信息
                flowInfoVO.setCurNodeInfo(null);
                flowInfoVO.setDoQueryFlag(true);
            }
        }else{
            flowInfoVO.setStartFlowFlag(true);
            flowInfoVO.setDefCode(queryDTO.getDefCode());
            flowInfoVO.setDefVersion(queryDTO.getDefVersion());
            flowInfoVO.setProcessId(null);
            flowInfoVO.setCurTaskId(null);
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            flowInfoVO.setCreatorId(loginUser.getUserId());
            flowInfoVO.setCreatorName(loginUser.getUserName());
            flowInfoVO.setCreatorOrgid(loginUser.getSwitchOrg().getOrgId());
            flowInfoVO.setCreatorOrgname(loginUser.getSwitchOrg().getOrgName());
            flowInfoVO.setDoQueryFlag(false);
            Integer defVersion = ObjectUtil.isNotNull(queryDTO.getDefVersion())? queryDTO.getDefVersion(): 1;
            // 查询指定版本流程定义
            flowDefVO = flowHisDeployService.get(queryDTO.getDefCode(), defVersion, false);
            // 查询第一个任务节点
            Map firstTaskNode = WorkflowUtil.getFirstTaskNode(flowDefVO.getDefJson());
            String curNodeId = (String)firstTaskNode.get("id");
            FlowNodeVO curNodeInfo = flowNodeService.get(queryDTO.getDefCode(), queryDTO.getDefVersion(), curNodeId);
            if(ObjectUtil.isNull(curNodeInfo)){
                throw new CommonException("操作失败，流程环节属性未配置！");
            }
            flowInfoVO.setCurNodeInfo(curNodeInfo);
            // 当前正在运行的节点ids
            flowInfoVO.setCurRunningNodeIds(curNodeId);
            try {
                Class<?> clazz = Class.forName(flowDefVO.getFlowentityClassname());
                flowInfoVO.setBusTableName(clazz.getAnnotation(TableName.class).value());
            } catch (Exception e) {
                log.error(ExceptionUtil.stacktraceToString(e));
                throw new CommonException("操作失败，获取业务实体信息异常！");
            }
        }
        flowInfoVO.setFlowEntityClassName(flowDefVO.getFlowentityClassname());
        flowInfoVO.setDefName(flowDefVO.getDefName());
        flowInfoVO.setDefJson(flowDefVO.getDefJson());
        flowInfoVO.setDefXml(flowDefVO.getDefXml());
        flowInfoVO.setProcessLimittype(flowDefVO.getProcessLimittype());
        flowInfoVO.setProcessLimitvalue(flowDefVO.getProcessLimitvalue());
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
        flowInfoVO.setActType(queryDTO.getActType());
        // 将页面传递的流程表单业务字段注入
        if(ObjectUtil.isNotNull(queryDTO.getBusinessForm())){
            flowInfoVO.setEntityVO(BeanUtil.copyProperties(
                    queryDTO.getBusinessForm(), flowInfoVO.getEntityVO().getClass()));
        }
        List<FlowTaskNodeVO> nextHandlerList = CollectionUtil.newArrayList();
        if(flowInfoVO.getActType() == FlowActTypeEnum.SUBMIT.ordinal()){
            // 流程提交情形
            nextHandlerList = this.getSubmitNextHandlerList(flowInfoVO);
        }else if(flowInfoVO.getActType() == FlowActTypeEnum.RETURN.ordinal()){
            // TODO 流程退回情形
            ProcessTask curTask = processTaskService.getById(flowInfoVO.getCurTaskId());
            FlowNodeVO curNodeInfo = flowNodeService.get(flowInfoVO.getDefCode(),
                    flowInfoVO.getDefVersion(), flowInfoVO.getCurNodeInfo().getNodeId());
            FlowNodeVO incomingNodeInfo = flowNodeService.get(flowInfoVO.getDefCode(),
                    flowInfoVO.getDefVersion(), curTask.getIncomingNodeid());
            if(incomingNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.SINGLE.ordinal() ||
               incomingNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.MULTI.ordinal()){
                List<ProcessTask> handledTaskList = processTaskService.listHandledTasks(
                        flowInfoVO.getProcessId(), curTask.getIncomingNodeid(), incomingNodeInfo.getTaskAssigntype());
                for(int i = 0; i < handledTaskList.size(); i++){
                    ProcessTask handledTask = handledTaskList.get(i);
                    FlowTaskNodeVO nextHandler = new FlowTaskNodeVO();
                    nextHandler.setNodeId(curTask.getIncomingNodeid());
                    nextHandler.setNodeName(handledTask.getTaskNodename());
                    nextHandler.setNodeType(FlowNodeTypeEnum.TASK.getCode());
                    // TODO nextHandler.setMultiHandletype();
                    nextHandler.setReselectPermit(CommonWhetherEnum.NO.getCode());
                    FlowTaskHandler handler = new FlowTaskHandler();
                    handler.setHandlerId(handledTask.getHandlerId());
                    handler.setHandlerName(handledTask.getHandlerName());
                    handler.setHandlerOrgid(handledTask.getHandlerOrgid());
                    handler.setHandlerOrgname(handledTask.getHandlerOrgname());
                    nextHandler.setHandlers(CollUtil.newArrayList(handler));
                    if(curNodeInfo.getTaskReturntype() == FlowTaskReturnTypeEnum.DIRECT.ordinal()){
                        if(incomingNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.SINGLE.ordinal()){
                            nextHandler.setOutgoingTaskids(flowInfoVO.getCurTaskId());
                        }else{
                            String thizTaskids = processTaskService.getMultiHandleNodeOutgoingTaskids(flowInfoVO.getCurTaskId());
                            nextHandler.setOutgoingTaskids(thizTaskids);
                        }
                    }
                    if(incomingNodeInfo.getTaskAssigntype() == FlowTaskAssignTypeEnum.SINGLE.ordinal()){
                        if(i == 0){
                            nextHandlerList.add(nextHandler);
                        }
                    }else{
                        nextHandlerList.add(nextHandler);
                    }
                }
            }
        }
        return nextHandlerList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSubmit(FlowSubmitDTO submitDTO){
        // 1、获取流程核心信息
        FlowInfoVO flowInfoVO = this.getFlowInfo(submitDTO, false);
        flowInfoVO.setActType(submitDTO.getActType());
        // 将页面传递的流程表单业务字段注入
        if(ObjectUtil.isNotNull(submitDTO.getBusinessForm())){
            flowInfoVO.setEntityVO(BeanUtil.copyProperties(
                    submitDTO.getBusinessForm(), flowInfoVO.getEntityVO().getClass()));
        }
        // 2、根据当前任务id，判断是否重复请求
        if(StrUtil.isNotEmpty(flowInfoVO.getCurTaskId())){
            if(!processTaskService.isExist(flowInfoVO.getCurTaskId())){
                throw new CommonException("当前任务已失效，请刷新后重试！");
            }
        }
        // 3、执行绑定的流程前置事件
        flowEventService.executeBindEvent(flowInfoVO, FlowEventTypeEnum.PRE.ordinal());
        // 4、执行绑定的流程存储事件，有则执行，无则执行默认的存储事件
        if(flowEventService.executeBindEvent(flowInfoVO, FlowEventTypeEnum.WRITE.ordinal()) == null){
            this.executeDefaultWriteEvent(flowInfoVO);
        }
        // 5、保存或更新流程实例表
        processInstanceService.saveOrUpdate(flowInfoVO, false);
        // 6、派发流程任务
        processTaskService.assignTask(flowInfoVO, submitDTO.getProcessForm(), submitDTO.getNextHandlerList());
        // 7、更新流程实例
        this.updateProcessInstanceState(flowInfoVO, submitDTO.getProcessForm());
        // 8、执行绑定的流程后置事件
        flowEventService.executeBindEvent(flowInfoVO, FlowEventTypeEnum.AFT.ordinal());
    }

    /**
     * 执行默认的流程读取事件
     * @param flowInfoVO
     */
    private void executeDefaultReadEvent(FlowInfoVO flowInfoVO){
        try {
            if(StrUtil.isNotBlank(flowInfoVO.getProcessId())){
                // 获取实体类类型
                Class<?> entityClazz = Class.forName(flowInfoVO.getFlowEntityClassName());
                // 获取对应Mapper类
                Class<?> beanMapperClazz = entityClazz.getAnnotation(FlowEntity.class).mapper();
                // 获取Mapper实例
                Object mapperBean = SpringUtil.getBean(beanMapperClazz);
                // 调用selectById方法
                Method selectByIdMethod = beanMapperClazz.getMethod("selectById", Serializable.class);
                flowInfoVO.setEntityVO(selectByIdMethod.invoke(mapperBean, flowInfoVO.getBusRecordId(), entityClazz));
            }else{
                // 发起流程情形，构造空业务实体
                Class<?> clazz = Class.forName(flowInfoVO.getFlowEntityClassName());
                flowInfoVO.setEntityVO(clazz.getAnnotation(FlowEntity.class).value().newInstance());
                flowInfoVO.setBusRecordId(null);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            throw new CommonException(String.format("操作失败，执行默认流程读取事件异常！"));
        }
    }

    /**
     * 执行默认的流程存储事件
     * @param flowInfoVO
     */
    private void executeDefaultWriteEvent(FlowInfoVO flowInfoVO){
        try {
            // 获取实体类类型
            Class<?> entityClazz = Class.forName(flowInfoVO.getFlowEntityClassName());
            // 获取对应Mapper类
            Class<?> beanMapperClazz = entityClazz.getAnnotation(FlowEntity.class).mapper();
            // 获取Mapper实例
            Object mapperBean = SpringUtil.getBean(beanMapperClazz);
            // 复制对象并保留引用
            Object entityObj = BeanUtil.copyProperties(flowInfoVO.getEntityVO(), entityClazz);
            // 调用保存或更新方法
            if(StrUtil.isNotBlank(flowInfoVO.getBusRecordId())){
                Method updateMethod = beanMapperClazz.getMethod("updateById", Object.class);
                updateMethod.invoke(mapperBean, entityObj);
            }else{
                Method insertMethod = beanMapperClazz.getMethod("insert", Object.class);
                insertMethod.invoke(mapperBean, entityObj);
                // !!! 获取主键值
                String pkValue = null;
                for(Field field: entityClazz.getDeclaredFields()){
                    if(field.isAnnotationPresent(TableId.class)) {
                        field.setAccessible(true);
                        pkValue = (String)field.get(entityObj);
                        break;
                    }
                }
                flowInfoVO.setBusRecordId(pkValue);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
            throw new CommonException(String.format("操作失败，执行默认流程存储事件异常！"));
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
            ProcessTask curTask = processTaskService.getById(flowInfoVO.getCurTaskId());
            // 从流程任务表查询去往任务信息，组装返回
            if(StrUtil.isNotEmpty(curTask.getOutgoingTaskids())){
                // TODO 验证
                List<ProcessTaskVO> nextTaskList = processTaskService.listTasksByGroup(curTask.getOutgoingTaskids().split(","));
                for(ProcessTaskVO nextTask: nextTaskList){
                    FlowTaskNodeVO nextHandler = new FlowTaskNodeVO();
                    nextHandler.setNodeId(nextTask.getTaskNodeid());
                    nextHandler.setNodeName(nextTask.getTaskNodename());
                    nextHandler.setNodeType(FlowNodeTypeEnum.TASK.getCode());
                    String[] handlerIds = nextTask.getHandlerId().split(",");
                    String[] handlerNames = nextTask.getHandlerName().split(",");
                    String[] handlerOrgIds = nextTask.getHandlerOrgid().split(",");
                    String[] handlerOrgNames = nextTask.getHandlerOrgname().split(",");
                    List<FlowTaskHandler> handlerList = new ArrayList<>();
                    for(int i = 0; i < handlerIds.length; i++){
                        FlowTaskHandler handler = new FlowTaskHandler();
                        handler.setHandlerId(handlerIds[i]);
                        handler.setHandlerName(handlerNames[i]);
                        handler.setHandlerOrgid(handlerOrgIds[i]);
                        handler.setHandlerOrgname(handlerOrgNames[i]);
                        handlerList.add(handler);
                    }
                    nextHandler.setHandlers(handlerList);
                    nextHandler.setReselectPermit(CommonWhetherEnum.NO.getCode());
                    nextHandlerList.add(nextHandler);
                }
                return nextHandlerList;
            }
            // 串审环节，取出下一等待办理人
            if(curTask.getMultiHandletype() == FlowTaskMultiHandleTypeEnum.SERIAL.ordinal()){
                // TODO 验证
                FlowTaskNodeVO nextHandler = processTaskService.getMultiHandleNodeNextWaitingHandler(
                        flowInfoVO.getProcessId(), flowInfoVO.getCurTaskId());
                if(nextHandler != null){
                    nextHandlerList.add(nextHandler);
                    return nextHandlerList;
                }
            }
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
        if(flowHandlerVO.getHandlerType() == FlowHandlerTypeEnum.REFERRES.ordinal()){
            // 指定人员情形
            List<SysUserVO> sysUserVOS = sysUserApi.listByReferRes(flowHandlerVO.getTargetReferResList());
            List<FlowTaskHandler> thizHandlers = sysUserVOS.stream().map(u -> {
                FlowTaskHandler thizHandler = new FlowTaskHandler();
                thizHandler.setHandlerId(u.getUserId());
                thizHandler.setHandlerName(u.getUserName());
                thizHandler.setHandlerOrgid(u.getOrgId());
                thizHandler.setHandlerOrgname(u.getOrgName());
                return thizHandler;
            }).collect(Collectors.toList());
            nextHandlerVO.setHandlers(thizHandlers);
        }else if(flowHandlerVO.getHandlerType() == FlowHandlerTypeEnum.INTERFACE.ordinal()){
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

    private void updateProcessInstanceState(FlowInfoVO flowInfoVO, FlowProcessForm processForm){
        List<ProcessTask> taskList = processTaskService.listCurRunningProcessTasks(flowInfoVO.getProcessId());
        if(CollUtil.isNotEmpty(taskList)){
            String curRunningNodeIds = taskList.stream().map(ProcessTask::getTaskNodeid).collect(Collectors.joining(","));
            String curRunningNodeNames = taskList.stream().map(ProcessTask::getTaskNodename).collect(Collectors.joining(","));
            String curHandlerIds = taskList.stream().map(ProcessTask::getAssignHandlerids).collect(Collectors.joining(","));
            String curHandlerNames = taskList.stream().map(ProcessTask::getAssignHandlernames).collect(Collectors.joining(","));
            processInstanceService.updateCurRunningInfo(flowInfoVO.getProcessId(), curRunningNodeIds, curRunningNodeNames, curHandlerIds, curHandlerNames);
        }else{
            Integer taskState = FlowTaskStateEnum.HANDLED.ordinal();
            processTaskService.updateTaskStateCascade(flowInfoVO.getCurTaskId(), taskState);
            Integer processState = FlowProcessStateEnum.END.ordinal();
            if(processForm.getAppovalResult() != null){
                processState = processForm.getAppovalResult() == CommonWhetherEnum.NO.getCode()?
                        FlowProcessStateEnum.NOTAPPROVED.ordinal(): FlowProcessStateEnum.APPROVED.ordinal();
            }
            processInstanceService.updateHandleEndedInfo(flowInfoVO.getProcessId(), processState, processForm.getHandleOpinion());
        }
    }

}
