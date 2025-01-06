package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowHisDeployMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.entity.FlowHisDeploy;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 流程历史部署Service实现类
 *
 * @author ohohmiao
 * @date 2024-12-08 19:58
 */
@Service
public class FlowHisDeployServiceImpl extends CommonServiceImpl<FlowHisDeployMapper, FlowHisDeploy> implements FlowHisDeployService {

    @Resource
    private FlowDefService flowDefService;

    @Override
    public FlowDefVO get(String defCode, Integer defVersion){
        // 先查找流程定义表
        LambdaQueryWrapper<FlowDef> getWrapper = new LambdaQueryWrapper<>();
        getWrapper.eq(FlowDef::getDefCode, defCode);
        getWrapper.eq(FlowDef::getDefVersion, defVersion);
        FlowDef flowDef = flowDefService.getOne(getWrapper);
        if(ObjectUtil.isNotNull(flowDef)){
            return BeanUtil.copyProperties(flowDef, FlowDefVO.class);
        }else{
            // TODO 不存在，再查找历史部署表
            return null;
        }
    }

}
