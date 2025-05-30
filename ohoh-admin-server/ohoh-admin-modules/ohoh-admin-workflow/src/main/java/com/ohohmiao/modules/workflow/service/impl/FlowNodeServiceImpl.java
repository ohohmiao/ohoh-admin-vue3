package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowNodeMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowNode;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;
import com.ohohmiao.modules.workflow.service.FlowNodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 流程环节Service实现
 *
 * @author ohohmiao
 * @date 2025-05-30 16:52
 */
@Service
public class FlowNodeServiceImpl extends ServiceImpl<FlowNodeMapper, FlowNode> implements FlowNodeService {

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

}
