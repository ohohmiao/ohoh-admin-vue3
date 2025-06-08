package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowNodeBindTypeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowFormMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowFormBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowFormBindPageDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormBindVO;
import com.ohohmiao.modules.workflow.service.FlowFormService;
import com.ohohmiao.modules.workflow.service.FlowNodeBindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

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
    private FlowNodeBindService flowNodeBindService;

    @Override
    public Page<FlowFormBindVO> listBindByPage(FlowFormBindPageDTO pageDTO){
        FlowNodeBindQueryDTO queryDTO = new FlowNodeBindQueryDTO();
        queryDTO.setDefCode(pageDTO.getDefCode());
        queryDTO.setDefVersion(pageDTO.getDefVersion());
        queryDTO.setBindType(FlowNodeBindTypeEnum.FORM.ordinal());
        return flowFormMapper.listBindByPage(CommonPageRequest.constructPage(
                pageDTO.getCurrent(), pageDTO.getSize()), queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBind(FlowFormBindAddOrEditDTO addOrEditDTO){
        FlowForm flowForm = BeanUtil.copyProperties(addOrEditDTO, FlowForm.class);
        flowForm.setCreateTime(new Date());
        flowFormMapper.insert(flowForm);
        // 绑定关系表
        FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO =
                BeanUtil.copyProperties(addOrEditDTO, FlowNodeBindAddOrEditDTO.class);
        flowNodeBindAddOrEditDTO.setBindType(FlowNodeBindTypeEnum.FORM.ordinal());
        flowNodeBindAddOrEditDTO.setBindObjid(flowForm.getFormId());
        flowNodeBindService.saveOrUpdate(flowNodeBindAddOrEditDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editBind(FlowFormBindAddOrEditDTO addOrEditDTO){
        FlowForm flowForm = this.getById(addOrEditDTO.getFormId());
        if(ObjectUtil.isNull(flowForm)){
            throw new CommonException("流程表单不存在！");
        }
        BeanUtil.copyProperties(addOrEditDTO, flowForm);
        flowFormMapper.updateById(flowForm);
        // 绑定关系表
        FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO =
                BeanUtil.copyProperties(addOrEditDTO, FlowNodeBindAddOrEditDTO.class);
        flowNodeBindAddOrEditDTO.setBindType(FlowNodeBindTypeEnum.FORM.ordinal());
        flowNodeBindAddOrEditDTO.setBindObjid(addOrEditDTO.getFormId());
        flowNodeBindService.saveOrUpdate(flowNodeBindAddOrEditDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiDeleteBind(CommonIdListDTO idListDTO){
        // 删除流程表单
        this.removeBatchByIds(idListDTO.getId());
        // 删除绑定关系表
        flowNodeBindService.deleteByBindTypeAndBindObjid(
                FlowNodeBindTypeEnum.FORM.ordinal(),
                idListDTO.getId().toArray(new String[0]));
    }

}
