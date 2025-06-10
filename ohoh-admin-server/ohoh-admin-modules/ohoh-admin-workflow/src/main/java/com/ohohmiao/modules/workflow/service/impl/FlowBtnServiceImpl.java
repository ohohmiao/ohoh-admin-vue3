package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.enums.FlowNodeBindTypeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowBtnMapper;
import com.ohohmiao.modules.workflow.model.dto.*;
import com.ohohmiao.modules.workflow.model.entity.FlowBtn;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnBindVO;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnVO;
import com.ohohmiao.modules.workflow.service.FlowBtnService;
import com.ohohmiao.modules.workflow.service.FlowNodeBindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程按钮Service实现
 *
 * @author ohohmiao
 * @date 2025-06-08 09:52
 */
@Service
public class FlowBtnServiceImpl extends CommonServiceImpl<FlowBtnMapper, FlowBtn> implements FlowBtnService {

    @Resource
    private FlowBtnMapper flowBtnMapper;

    @Resource
    private FlowNodeBindService flowNodeBindService;

    @Override
    public Page<FlowBtn> listByPage(FlowBtnPageDTO pageDTO){
        LambdaQueryWrapper<FlowBtn> pageWrapper = new LambdaQueryWrapper<>();
        pageWrapper.like(StrUtil.isNotBlank(pageDTO.getBtnText()), FlowBtn::getBtnText, pageDTO.getBtnText());
        pageWrapper.like(StrUtil.isNotBlank(pageDTO.getBtnFun()), FlowBtn::getBtnFun, pageDTO.getBtnFun());
        pageWrapper.orderByDesc(FlowBtn::getBtnSort);
        return this.page(CommonPageRequest.constructPage(pageDTO.getCurrent(), pageDTO.getSize()), pageWrapper);
    }

    @Override
    public FlowBtnVO get(String btnId){
        FlowBtn flowBtn = flowBtnMapper.selectById(btnId);
        if(ObjectUtil.isNotNull(flowBtn)){
            return BeanUtil.copyProperties(flowBtn, FlowBtnVO.class);
        }else{
            return null;
        }
    }

    @Override
    public boolean isExistBtnText(FlowBtnAddOrEditDTO addOrEditDTO){
        LambdaQueryWrapper<FlowBtn> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(FlowBtn::getBtnText, addOrEditDTO.getBtnText());
        countWrapper.ne(StrUtil.isNotEmpty(addOrEditDTO.getBtnId()),
                FlowBtn::getBtnId, addOrEditDTO.getBtnId());
        return this.count(countWrapper) > 0;
    }

    @Override
    public boolean isExistBtnFun(FlowBtnAddOrEditDTO addOrEditDTO){
        LambdaQueryWrapper<FlowBtn> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(FlowBtn::getBtnFun, addOrEditDTO.getBtnFun());
        countWrapper.ne(StrUtil.isNotEmpty(addOrEditDTO.getBtnId()),
                FlowBtn::getBtnId, addOrEditDTO.getBtnId());
        return this.count(countWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowBtnAddOrEditDTO addOrEditDTO){
        boolean isExistBtnText = this.isExistBtnText(addOrEditDTO);
        if(isExistBtnText){
            throw new CommonException("按钮文本重复，请重新输入！");
        }
        boolean isExistBtnFun = this.isExistBtnFun(addOrEditDTO);
        if(isExistBtnFun){
            throw new CommonException("执行方法，请重新输入！");
        }
        FlowBtn flowBtn = BeanUtil.copyProperties(addOrEditDTO, FlowBtn.class);
        Integer maxSort = flowBtnMapper.getMaxBtnSort();
        flowBtn.setBtnSort(ObjectUtil.isNotNull(maxSort)? maxSort+1: 1);
        this.save(flowBtn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowBtnAddOrEditDTO addOrEditDTO){
        FlowBtn flowBtn = this.getById(addOrEditDTO.getBtnId());
        if(ObjectUtil.isNull(flowBtn)){
            throw new CommonException("流程按钮不存在！");
        }
        boolean isExistBtnText = this.isExistBtnText(addOrEditDTO);
        if(isExistBtnText){
            throw new CommonException("按钮文本重复，请重新输入！");
        }
        boolean isExistBtnFun = this.isExistBtnFun(addOrEditDTO);
        if(isExistBtnFun){
            throw new CommonException("执行方法，请重新输入！");
        }
        BeanUtil.copyProperties(addOrEditDTO, flowBtn);
        this.updateById(flowBtn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        // 删除主表
        this.removeById(idDTO.getId());
        // 删除环节绑定关系
        flowNodeBindService.deleteByBindTypeAndBindObjid(FlowNodeBindTypeEnum.BTN.ordinal(), idDTO.getId());
    }

    @Override
    public Page<FlowBtnBindVO> listBindByPage(FlowBtnBindPageDTO pageDTO){
        FlowNodeBindQueryDTO queryDTO = new FlowNodeBindQueryDTO();
        queryDTO.setDefCode(pageDTO.getDefCode());
        queryDTO.setDefVersion(pageDTO.getDefVersion());
        queryDTO.setBindType(FlowNodeBindTypeEnum.BTN.ordinal());
        return flowBtnMapper.listBindByPage(CommonPageRequest.constructPage(
                   pageDTO.getCurrent(), pageDTO.getSize()), queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBind(FlowBtnBindAddOrEditDTO addOrEditDTO){
        flowNodeBindService.delete(FlowNodeBindTypeEnum.BTN.ordinal(),
                addOrEditDTO.getDefCode(), addOrEditDTO.getDefVersion());
        FlowNodeBindAddOrEditDTO flowNodeBindAddOrEditDTO =
                BeanUtil.copyProperties(addOrEditDTO, FlowNodeBindAddOrEditDTO.class);
        flowNodeBindAddOrEditDTO.setBindType(FlowNodeBindTypeEnum.BTN.ordinal());
        flowNodeBindAddOrEditDTO.setBindObjid(addOrEditDTO.getBtnId());
        flowNodeBindService.save(flowNodeBindAddOrEditDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiDeleteBind(CommonIdListDTO idListDTO){
        flowNodeBindService.deleteByBindTypeAndBindObjid(
                FlowNodeBindTypeEnum.BTN.ordinal(),
                idListDTO.getId().toArray(new String[0]));
    }

    @Override
    public List<CommonSelectVO> select(){
        LambdaQueryWrapper<FlowBtn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(FlowBtn::getBtnSort);
        return this.list(queryWrapper).stream().map(btn -> {
            CommonSelectVO selectVO = new CommonSelectVO();
            selectVO.setLabel(btn.getBtnText());
            selectVO.setValue(btn.getBtnId());
            return selectVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FlowBtnVO> listBindBtns(String defCode, Integer defVersion, String nodeId){
        return flowBtnMapper.listBindBtns(defCode, defVersion, nodeId);
    }

}
