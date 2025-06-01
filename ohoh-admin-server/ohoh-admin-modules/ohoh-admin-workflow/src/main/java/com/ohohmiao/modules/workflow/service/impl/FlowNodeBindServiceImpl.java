package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowNodeBindMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowNodeBind;
import com.ohohmiao.modules.workflow.service.FlowNodeBindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程环节绑定信息Service实现类
 *
 * @author ohohmiao
 * @date 2025-01-16 10:02
 */
@Service
public class FlowNodeBindServiceImpl extends CommonServiceImpl<FlowNodeBindMapper, FlowNodeBind> implements FlowNodeBindService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO){
        //先删除
        deleteByBindTypeAndBindObjid(flowNodeBindAddOrEditDTO.getBindType(),
                flowNodeBindAddOrEditDTO.getBindObjid());
        //再新增
        List<FlowNodeBind> flowNodeBinds = CollectionUtil.newArrayList();
        for(int index = 0; index < flowNodeBindAddOrEditDTO.getBindNodeIds().size(); index++){
            FlowNodeBind flowNodeBind = BeanUtil.copyProperties(flowNodeBindAddOrEditDTO, FlowNodeBind.class);
            flowNodeBind.setNodeId(flowNodeBindAddOrEditDTO.getBindNodeIds().get(index));
            flowNodeBind.setNodeName(flowNodeBindAddOrEditDTO.getBindNodeNames().get(index));
            flowNodeBinds.add(flowNodeBind);
        }
        this.saveBatch(flowNodeBinds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByBindTypeAndBindObjid(Integer bindType, String... bindObjid){
        if(bindObjid.length == 0){
            return;
        }
        LambdaUpdateWrapper<FlowNodeBind> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(FlowNodeBind::getBindType, bindType)
                .in(FlowNodeBind::getBindObjid, bindObjid);
        this.remove(deleteWrapper);
    }

}
