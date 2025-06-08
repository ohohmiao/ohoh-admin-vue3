package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.modules.workflow.mapper.FlowNodeMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeGetDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.entity.FlowNode;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskMultiAssignWeight;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import com.ohohmiao.modules.workflow.service.FlowHandlerService;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import com.ohohmiao.modules.workflow.service.FlowNodeService;
import com.ohohmiao.modules.workflow.util.WorkflowUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程环节Service实现
 *
 * @author ohohmiao
 * @date 2025-05-30 16:52
 */
@Service
public class FlowNodeServiceImpl extends ServiceImpl<FlowNodeMapper, FlowNode> implements FlowNodeService {

    @Resource
    private FlowDefService flowDefService;

    @Resource
    private FlowHisDeployService flowHisDeployService;

    @Resource
    private FlowHandlerService flowHandlerService;

    @Override
    public FlowNodeVO get(String defCode, Integer defVersion, String nodeId){
        LambdaQueryWrapper<FlowNode> getWrapper = new LambdaQueryWrapper<>();
        getWrapper.eq(FlowNode::getDefCode, defCode);
        getWrapper.eq(FlowNode::getDefVersion, defVersion);
        getWrapper.eq(FlowNode::getNodeId, nodeId);
        FlowNode flowNode = this.getOne(getWrapper);
        if(ObjectUtil.isNotNull(flowNode)){
            return BeanUtil.copyProperties(flowNode, FlowNodeVO.class);
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(FlowNodeAddOrEditDTO addOrEditDTO){
        FlowNode flowNode = BeanUtil.copyProperties(addOrEditDTO, FlowNode.class);
        FlowNodeVO flowNodeVO = this.get(addOrEditDTO.getDefCode(),
                addOrEditDTO.getDefVersion(), addOrEditDTO.getNodeId());
        if(ObjectUtil.isNotNull(flowNodeVO)){
            // 更新
            flowNode.setPropId(flowNodeVO.getPropId());
            this.updateById(flowNode);
        }else{
            // 新增
            this.save(flowNode);
        }
    }

    @Override
    public List<FlowTaskMultiAssignWeight> listMultiAssignWeight(FlowNodeGetDTO listDTO){
        List<FlowTaskMultiAssignWeight> resultList = CollectionUtil.newArrayList();
        // 判断是否第一个节点，如果是，无当前环节办理人，则返回空
        FlowDefVO flowDefVO = flowHisDeployService.get(listDTO.getDefCode(), listDTO.getDefVersion(), false);
        Map firstTaskNode = WorkflowUtil.getFirstTaskNode(flowDefVO.getDefJson());
        if(listDTO.getNodeId().equals(firstTaskNode.get("id").toString())){
            return resultList;
        }
        FlowNodeVO flowNodeVO = get(listDTO.getDefCode(), listDTO.getDefVersion(), listDTO.getNodeId());
        if(ObjectUtil.isNotNull(flowNodeVO)){
            // 查询环节属性表，返回权重字段
            resultList = flowNodeVO.getMultiassignWeightjson();
        }
        // 查询出当前环节办理人员，构造空配置
        if(CollectionUtil.isEmpty(resultList)){
            resultList = flowHandlerService.listFlowNodeHanlder(
                    listDTO.getDefCode(), listDTO.getDefVersion(), listDTO.getNodeId())
                    .stream().map(m -> BeanUtil.copyProperties(m, FlowTaskMultiAssignWeight.class))
                    .collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FlowTaskMultiAssignWeight> resetMultiAssignWeight(FlowNodeGetDTO getDTO){
        // 清空配置
        LambdaUpdateWrapper<FlowNode> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FlowNode::getDefCode, getDTO.getDefCode());
        updateWrapper.eq(FlowNode::getDefVersion, getDTO.getDefVersion());
        updateWrapper.eq(FlowNode::getNodeId, getDTO.getNodeId());
        updateWrapper.set(FlowNode::getMultiassignWeightjson, null);
        this.update(updateWrapper);
        // 查询出当前环节办理人员，构造空配置
        return flowHandlerService.listFlowNodeHanlder(
                        getDTO.getDefCode(), getDTO.getDefVersion(), getDTO.getNodeId())
                .stream().map(m -> BeanUtil.copyProperties(m, FlowTaskMultiAssignWeight.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommonSelectVO> listNextTaskNodeInfo(FlowNodeGetDTO getDTO){
        FlowDef flowDef = flowDefService.getByDefCodeAndDefVersion(getDTO.getDefCode(), getDTO.getDefVersion());
        if(ObjectUtil.isNull(flowDef)){
            return CollectionUtil.newArrayList();
        }
        List<Map> nextTaskNodeList = WorkflowUtil.getNextTaskNodes(flowDef.getDefJson(), getDTO.getNodeId());
        return nextTaskNodeList.stream().map(node -> {
            CommonSelectVO selectVO = new CommonSelectVO();
            selectVO.setValue(node.get("id").toString());
            selectVO.setLabel(node.get("name").toString());
            return selectVO;
        }).collect(Collectors.toList());
    }

}
