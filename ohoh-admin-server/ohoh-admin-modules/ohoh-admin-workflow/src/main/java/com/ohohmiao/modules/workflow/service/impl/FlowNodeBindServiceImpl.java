package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    public void save(FlowNodeBindAddOrEditDTO addOrEditDTO){
        List<FlowNodeBind> flowNodeBinds = CollectionUtil.newArrayList();
        for(int index = 0; index < addOrEditDTO.getBindNodeIds().size(); index++){
            FlowNodeBind flowNodeBind = BeanUtil.copyProperties(addOrEditDTO, FlowNodeBind.class);
            flowNodeBind.setNodeId(addOrEditDTO.getBindNodeIds().get(index));
            flowNodeBind.setNodeName(addOrEditDTO.getBindNodeNames().get(index));
            flowNodeBinds.add(flowNodeBind);
        }
        this.saveBatch(flowNodeBinds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer bindType, String defCode, Integer defVersion, String bindObjid){
        LambdaQueryWrapper<FlowNodeBind> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(FlowNodeBind::getBindType, bindType);
        deleteWrapper.eq(FlowNodeBind::getDefCode, defCode);
        deleteWrapper.eq(FlowNodeBind::getDefVersion, defVersion);
        deleteWrapper.eq(StrUtil.isNotBlank(bindObjid), FlowNodeBind::getBindObjid, bindObjid);
        this.remove(deleteWrapper);
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
