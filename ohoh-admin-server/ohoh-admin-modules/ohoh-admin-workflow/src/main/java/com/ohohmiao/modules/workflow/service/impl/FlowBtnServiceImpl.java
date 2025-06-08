package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.impl.CommonServiceImpl;
import com.ohohmiao.modules.workflow.mapper.FlowBtnMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowBtn;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnVO;
import com.ohohmiao.modules.workflow.service.FlowBtnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Override
    public Page<FlowBtnVO> listByPage(FlowBtnPageDTO pageDTO){
        return null;
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
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowBtnAddOrEditDTO addOrEditDTO){
        FlowBtn flowBtn = BeanUtil.copyProperties(addOrEditDTO, FlowBtn.class);
        this.save(flowBtn);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowBtnAddOrEditDTO addOrEditDTO){
        /*FlowBtn flowBtn = this.getById(addOrEditDTO.getBtnId());
        if(ObjectUtil.isNull(flowBtn)){
            throw new CommonException("流程按钮不存在！");
        }
        BeanUtil.copyProperties(addOrEditDTO, flowBtn);
        this.updateById(flowBtn);
*/

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(CommonIdDTO idDTO){
        //TODO 处理定义绑定相关
        this.removeById(idDTO.getId());
    }

}
