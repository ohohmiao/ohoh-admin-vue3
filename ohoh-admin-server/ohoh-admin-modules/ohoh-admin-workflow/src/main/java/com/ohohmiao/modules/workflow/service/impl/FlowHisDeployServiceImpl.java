package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowDefPropTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowInitiatorScopeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowHisDeployMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployListDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.entity.FlowDefType;
import com.ohohmiao.modules.workflow.model.entity.FlowHisDeploy;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowDefPropService;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import com.ohohmiao.modules.workflow.service.FlowDefTypeService;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private FlowDefPropService flowDefPropService;

    @Override
    public FlowDefVO get(String defCode, Integer defVersion){
        FlowDefVO flowDefVO = null;
        // 先查找流程定义表
        FlowDef flowDef = flowDefService.getByDefCodeAndDefVersion(defCode, defVersion);
        if(ObjectUtil.isNotNull(flowDef)){
            flowDefVO = BeanUtil.copyProperties(flowDef, FlowDefVO.class);
        }else{
            // 再查找历史部署表
            FlowHisDeploy flowHisDeploy = this.getByDefCodeAndDefVersion(defCode, defVersion);
            if(ObjectUtil.isNotNull(flowHisDeploy)){
                flowDefVO = BeanUtil.copyProperties(flowHisDeploy, FlowDefVO.class);
            }
        }
        // 组装流程属性
        if(ObjectUtil.isNotNull(flowDefVO)){
            if(flowDefVO.getInitiatorScope() != null &&
               flowDefVO.getInitiatorScope().equals(FlowInitiatorScopeEnum.TARGET.ordinal())){
                flowDefVO.setTargetInitiators(flowDefPropService.list(
                        FlowDefPropTypeEnum.INITIATOR.ordinal(), defCode, defVersion));
            }
        }
        return flowDefVO;
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
        // 更新流程发起范围数据
        if(hisDTO.getInitiatorScope().equals(FlowInitiatorScopeEnum.ALL.ordinal())){
            flowDefPropService.delete(FlowDefPropTypeEnum.INITIATOR.ordinal(), hisDTO.getDefCode(), hisDTO.getDefVersion());
        }else{
            flowDefPropService.saveOrUpdate(FlowDefPropTypeEnum.INITIATOR.ordinal(), hisDTO.getDefCode(),
                    hisDTO.getDefVersion(), hisDTO.getTargetInitiators());
        }
        FlowDef flowDef = flowDefService.getByDefCodeAndDefVersion(hisDTO.getDefCode(), hisDTO.getDefVersion());
        if(ObjectUtil.isNotNull(flowDef)){
            // 流程定义表
            BeanUtil.copyProperties(hisDTO, flowDef);
            flowDefService.updateById(flowDef);
        }else{
            // 历史部署表
            FlowHisDeploy flowHisDeploy = this.getByDefCodeAndDefVersion(
                    hisDTO.getDefCode(), hisDTO.getDefVersion());
            if(ObjectUtil.isNull(flowHisDeploy)){
                throw new CommonException("流程定义不存在！");
            }
            BeanUtil.copyProperties(hisDTO, flowHisDeploy);
            this.updateById(flowHisDeploy);
        }
    }

    @Override
    public List<FlowDefVO> list(FlowHisDeployListDTO listDTO){
        LambdaQueryWrapper<FlowHisDeploy> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.eq(FlowHisDeploy::getDefCode, listDTO.getDefCode());
        listWrapper.eq(ObjectUtil.isNotNull(listDTO.getDefVersion()),
                FlowHisDeploy::getDefVersion, listDTO.getDefVersion());
        listWrapper.orderByDesc(FlowHisDeploy::getDefVersion);
        return this.list(listWrapper).stream().map(f ->
                BeanUtil.copyProperties(f, FlowDefVO.class)).collect(Collectors.toList());
    }

    @Override
    public FlowHisDeploy getByDefCodeAndDefVersion(String defCode, Integer defVersion){
        LambdaQueryWrapper<FlowHisDeploy> getWrapper = new LambdaQueryWrapper<>();
        getWrapper.eq(FlowHisDeploy::getDefCode, defCode);
        getWrapper.eq(FlowHisDeploy::getDefVersion, defVersion);
        return this.getOne(getWrapper);
    }

}
