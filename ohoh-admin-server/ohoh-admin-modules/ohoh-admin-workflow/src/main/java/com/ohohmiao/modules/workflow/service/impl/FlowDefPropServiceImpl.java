package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import com.ohohmiao.modules.workflow.mapper.FlowDefPropMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowDefProp;
import com.ohohmiao.modules.workflow.service.FlowDefPropService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 流程定义属性Service实现
 *
 * @author ohohmiao
 * @date 2025-05-27 17:15
 */
@Service
public class FlowDefPropServiceImpl extends ServiceImpl<FlowDefPropMapper, FlowDefProp> implements FlowDefPropService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer propType, String defCode, Integer defVersion){
        QueryWrapper<FlowDefProp> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(FlowDefProp::getPropType, propType);
        deleteWrapper.lambda().eq(FlowDefProp::getDefCode, defCode);
        deleteWrapper.lambda().eq(FlowDefProp::getDefVersion, defVersion);
        this.remove(deleteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Integer propType, String defCode, Integer defVersion, List<CommonReferRes> referResList){
        this.delete(propType, defCode, defVersion);

        if(CollUtil.isNotEmpty(referResList)){
            Set<FlowDefProp> propList = referResList.stream().map(res -> {
                FlowDefProp prop = BeanUtil.copyProperties(res, FlowDefProp.class);
                prop.setPropType(propType);
                prop.setDefCode(defCode);
                prop.setDefVersion(defVersion);
                return prop;
            }).collect(Collectors.toSet());
            this.saveBatch(propList);
        }
    }

    @Override
    public List<CommonReferRes> list(Integer propType, String defCode, Integer defVersion){
        LambdaQueryWrapper<FlowDefProp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowDefProp::getPropType, propType);
        queryWrapper.eq(FlowDefProp::getDefCode, defCode);
        queryWrapper.eq(FlowDefProp::getDefVersion, defVersion);
        return this.list(queryWrapper).stream().map(item ->
                BeanUtil.copyProperties(item, CommonReferRes.class)).collect(Collectors.toList());
    }

}
