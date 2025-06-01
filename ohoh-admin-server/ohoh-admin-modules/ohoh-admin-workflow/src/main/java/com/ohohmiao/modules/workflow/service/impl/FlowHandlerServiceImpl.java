package com.ohohmiao.modules.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.modules.workflow.enums.FlowDefBindTypeEnum;
import com.ohohmiao.modules.workflow.enums.FlowHandlerTypeEnum;
import com.ohohmiao.modules.workflow.mapper.FlowHandlerMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowHandlerVO;
import com.ohohmiao.modules.workflow.service.FlowDefBindService;
import com.ohohmiao.modules.workflow.service.FlowHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 流程环节办理人配置Service实现
 *
 * @author ohohmiao
 * @date 2025-06-01 11:01
 */
@Service
public class FlowHandlerServiceImpl extends ServiceImpl<FlowHandlerMapper, FlowHandler> implements FlowHandlerService {

    @Resource
    private FlowHandlerMapper flowHandlerMapper;

    @Resource
    private FlowDefBindService flowDefBindService;

    public FlowHandlerServiceImpl(FlowHandlerMapper flowHandlerMapper) {
        this.flowHandlerMapper = flowHandlerMapper;
    }

    @Override
    public Page<FlowHandler> listByPage(FlowHandlerPageDTO pageDTO){
        LambdaQueryWrapper<FlowHandler> pageWrapper = new LambdaQueryWrapper<>();
        pageWrapper.eq(FlowHandler::getDefCode, pageDTO.getDefCode());
        pageWrapper.eq(FlowHandler::getDefVersion, pageDTO.getDefVersion());
        pageWrapper.eq(FlowHandler::getNodeId, pageDTO.getNodeId());
        pageWrapper.orderByDesc(FlowHandler::getCreateTime);
        return this.page(CommonPageRequest.constructPage(pageDTO.getCurrent(), pageDTO.getSize()), pageWrapper);
    }

    @Override
    public FlowHandlerVO get(String handlerId){
        FlowHandler flowHandler = flowHandlerMapper.selectById(handlerId);
        if(ObjectUtil.isNotNull(flowHandler)){
            FlowHandlerVO flowHandlerVO = new FlowHandlerVO();
            BeanUtil.copyProperties(flowHandler, flowHandlerVO);
            if(flowHandlerVO.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
                // 组装指定人员数据
                List<CommonReferRes> targetReferResList = flowDefBindService.list(FlowDefBindTypeEnum.HANDLER.ordinal(),
                        flowHandlerVO.getDefCode(), flowHandlerVO.getDefVersion());
                flowHandlerVO.setTargetReferResList(targetReferResList);
            }
            return flowHandlerVO;
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(FlowHandlerAddOrEditDTO flowHandlerAddOrEditDTO){
        FlowHandler flowHandler = BeanUtil.copyProperties(flowHandlerAddOrEditDTO, FlowHandler.class);
        flowHandler.setCreateTime(new Date());
        this.save(flowHandler);
        if(flowHandler.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
            // 更新指定人员
            flowDefBindService.saveOrUpdate(FlowDefBindTypeEnum.HANDLER.ordinal(),
                    flowHandler.getDefCode(), flowHandler.getDefVersion(),
                    flowHandlerAddOrEditDTO.getTargetReferResList(), flowHandler.getHandlerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(FlowHandlerAddOrEditDTO flowHandlerAddOrEditDTO){
        FlowHandler flowHandler = this.getById(flowHandlerAddOrEditDTO.getHandlerId());
        if(ObjectUtil.isNull(flowHandler)){
            throw new CommonException("环节办理人配置不存在！");
        }
        BeanUtil.copyProperties(flowHandlerAddOrEditDTO, flowHandler);
        this.updateById(flowHandler);
        if(flowHandler.getHandlerType().equals(FlowHandlerTypeEnum.REFERRES.ordinal())){
            // 更新指定人员
            flowDefBindService.saveOrUpdate(FlowDefBindTypeEnum.HANDLER.ordinal(),
                    flowHandler.getDefCode(), flowHandler.getDefVersion(),
                    flowHandlerAddOrEditDTO.getTargetReferResList(), flowHandler.getHandlerId());
        }else{
            // 删除指定人员
            flowDefBindService.deleteByBindTypeAndBindObjid(
                    FlowDefBindTypeEnum.HANDLER.ordinal(), flowHandler.getHandlerId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void multiDelete(CommonIdListDTO idListDTO){
        this.removeBatchByIds(idListDTO.getId());
        flowDefBindService.deleteByBindTypeAndBindObjid(
                FlowDefBindTypeEnum.HANDLER.ordinal(), idListDTO.getId().toArray(new String[]{}));
    }

}
