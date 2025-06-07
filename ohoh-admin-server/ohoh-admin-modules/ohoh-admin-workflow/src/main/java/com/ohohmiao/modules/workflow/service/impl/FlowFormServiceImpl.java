package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowFormMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowFormAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowFormPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDefType;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormVO;
import com.ohohmiao.modules.workflow.service.FlowDefTypeService;
import com.ohohmiao.modules.workflow.service.FlowFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 流程表单Service实现
 *
 * @author ohohmiao
 * @date 2025-06-07 14:54
 */
@Service
public class FlowFormServiceImpl extends CommonServiceImpl<FlowFormMapper, FlowForm> implements FlowFormService {

    @Resource
    private FlowFormMapper flowFormMapper;

    @Resource
    private FlowDefTypeService flowDefTypeService;

    @Override
    public Page<FlowFormVO> listByPage(FlowFormPageDTO pageDTO){
        QueryWrapper<FlowFormVO> pageWrapper = new QueryWrapper<>();
        pageWrapper.isNotNull("p.DEFTYPE_ID");
        pageWrapper.eq("f.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        // 查询条件
        pageWrapper.like(StrUtil.isNotEmpty(pageDTO.getParentId()),
                "p.TREE_PATH", pageDTO.getParentId()+".");
        pageWrapper.like(StrUtil.isNotEmpty(pageDTO.getFormName()),
                "f.FORM_NAME", pageDTO.getFormName());
        // 排序
        pageWrapper.orderByAsc(CollectionUtil.newArrayList("p.TREE_LEVEL", "p.TREE_SORT"));
        pageWrapper.orderByDesc("f.CREATE_TIME");
        return flowFormMapper.pageByWrapper(CommonPageRequest.constructPage(
                pageDTO.getCurrent(), pageDTO.getSize()), pageWrapper);
    }

    @Override
    public FlowFormVO get(String formId){
        FlowForm flowForm = flowFormMapper.selectById(formId);
        if(ObjectUtil.isNotNull(flowForm)){
            return BeanUtil.copyProperties(flowForm, FlowFormVO.class);
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowFormAddOrEditDTO addOrEditDTO){
        FlowDefType flowDefType = flowDefTypeService.getById(addOrEditDTO.getDeftypeId());
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("流程类别不存在！");
        }
        FlowForm flowForm = BeanUtil.copyProperties(addOrEditDTO, FlowForm.class);
        this.save(flowForm);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowFormAddOrEditDTO addOrEditDTO){
        FlowForm flowForm = this.getById(addOrEditDTO.getFormId());
        if(ObjectUtil.isNull(flowForm)){
            throw new CommonException("流程表单不存在！");
        }
        FlowDefType flowDefType = flowDefTypeService.getById(addOrEditDTO.getDeftypeId());
        if(ObjectUtil.isNull(flowDefType)){
            throw new CommonException("流程类别不存在！");
        }
        BeanUtil.copyProperties(addOrEditDTO, flowForm);
        this.updateById(flowForm);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        //TODO 处理定义绑定相关
        this.removeById(idDTO.getId());
    }

}
