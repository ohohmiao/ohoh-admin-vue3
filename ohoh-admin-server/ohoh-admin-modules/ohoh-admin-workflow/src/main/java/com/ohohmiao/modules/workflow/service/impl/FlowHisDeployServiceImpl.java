package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowHisDeployMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.entity.FlowDefType;
import com.ohohmiao.modules.workflow.model.entity.FlowHisDeploy;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import com.ohohmiao.modules.workflow.service.FlowDefTypeService;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Resource
    private FlowDefTypeService flowDefTypeService;

    @Override
    public FlowDefVO get(String defCode, Integer defVersion){
        // 先查找流程定义表
        FlowDef flowDef = flowDefService.getByDefCodeAndDefVersion(defCode, defVersion);
        if(ObjectUtil.isNotNull(flowDef)){
            return BeanUtil.copyProperties(flowDef, FlowDefVO.class);
        }else{
            // TODO 不存在，再查找历史部署表
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowHisDeployDTO hisDTO){
        FlowDefType flowDefType = flowDefTypeService.getById(hisDTO.getDeftypeId());
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("流程类别不存在！");
        }
        if(flowDefType.getTreeLevel() != 2){
            throw new CommonException("仅限挂在二级类别下！");
        }
        FlowDef flowDef = flowDefService.getByDefCodeAndDefVersion(hisDTO.getDefCode(), hisDTO.getDefVersion());
        if(ObjectUtil.isNotNull(flowDef)){
            // 流程定义表
            BeanUtil.copyProperties(hisDTO, flowDef);
            flowDefService.updateById(flowDef);
        }else{
            // 历史部署表
            LambdaQueryWrapper<FlowHisDeploy> getWrapper = new LambdaQueryWrapper<>();
            getWrapper.eq(FlowHisDeploy::getDefCode, hisDTO.getDefCode());
            getWrapper.eq(FlowHisDeploy::getDefVersion, hisDTO.getDefVersion());
            FlowHisDeploy flowHisDeploy = this.getOne(getWrapper);
            if(ObjectUtil.isNull(flowHisDeploy)){
                throw new CommonException("流程定义不存在！");
            }
            BeanUtil.copyProperties(hisDTO, flowHisDeploy);
            this.updateById(flowHisDeploy);
        }
    }

}
