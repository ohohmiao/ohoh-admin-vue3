package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowDefMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowDefAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowDefPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 流程定义Service实现类
 *
 * @author ohohmiao
 * @date 2024-12-08 19:57
 */
@Service
public class FlowDefServiceImpl extends CommonServiceImpl<FlowDefMapper, FlowDef> implements FlowDefService {

    @Resource
    private FlowDefMapper flowDefMapper;

    @Override
    public Page<FlowDefVO> listByPage(FlowDefPageDTO flowDefPageDTO){
        QueryWrapper<FlowDefVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.DEFTYPE_ID");
        queryWrapper.eq("d.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        //查询条件
        queryWrapper.like(StrUtil.isNotEmpty(flowDefPageDTO.getParentId()),
                "p.TREE_PATH", flowDefPageDTO.getParentId()+".");
        queryWrapper.like(StrUtil.isNotEmpty(flowDefPageDTO.getDefName()),
                "d.DEF_NAME", flowDefPageDTO.getDefName());
        queryWrapper.like(StrUtil.isNotEmpty(flowDefPageDTO.getDefCode()),
                "d.DEF_CODE", flowDefPageDTO.getDefCode());
        //排序
        queryWrapper.orderByAsc(CollectionUtil.newArrayList("p.TREE_LEVEL", "p.TREE_SORT", "d.DEF_SORT"));
        return flowDefMapper.pageByWrapper(CommonPageRequest.constructPage(
                flowDefPageDTO.getCurrent(), flowDefPageDTO.getSize()), queryWrapper);
    }

    @Override
    public boolean isExistDefCode(FlowDefAddOrEditDTO flowDefAddOrEditDTO){
        LambdaQueryWrapper<FlowDef> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(FlowDef::getDefCode, flowDefAddOrEditDTO.getDefCode());
        countWrapper.ne(StrUtil.isNotEmpty(flowDefAddOrEditDTO.getDefId()),
                FlowDef::getDefId, flowDefAddOrEditDTO.getDefId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowDefAddOrEditDTO flowDefAddOrEditDTO){

    }

    @Override
    public FlowDefVO get(String defId){
        FlowDef flowDef = flowDefMapper.selectById(defId);
        if(ObjectUtil.isNotNull(flowDef)){
            FlowDefVO flowDefVO = new FlowDefVO();
            BeanUtil.copyProperties(flowDef, flowDefVO);
            return flowDefVO;
        }else{
            return null;
        }
    }

}
