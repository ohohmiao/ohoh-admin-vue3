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
    public void addOrEdit(FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO){
        //先删除
        delete(flowNodeBindAddOrEditDTO.getDefCode(), flowNodeBindAddOrEditDTO.getDefVersion(),
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
    public void delete(String defCode, Integer defVersion, String bindObjid){
        LambdaUpdateWrapper<FlowNodeBind> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(FlowNodeBind::getDefCode, defCode)
                .eq(FlowNodeBind::getDefVersion, defVersion)
                .eq(FlowNodeBind::getBindObjid, bindObjid);
        this.remove(deleteWrapper);
    }

}
