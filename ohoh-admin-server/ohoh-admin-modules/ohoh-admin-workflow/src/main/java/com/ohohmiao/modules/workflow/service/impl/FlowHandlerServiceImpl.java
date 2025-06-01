package com.ohohmiao.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.modules.workflow.mapper.FlowHandlerMapper;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import com.ohohmiao.modules.workflow.service.FlowHandlerService;
import org.springframework.stereotype.Service;

/**
 * 流程环节办理人配置Service实现
 *
 * @author ohohmiao
 * @date 2025-06-01 11:01
 */
@Service
public class FlowHandlerServiceImpl extends ServiceImpl<FlowHandlerMapper, FlowHandler> implements FlowHandlerService {

    @Override
    public Page<FlowHandler> listByPage(FlowHandlerPageDTO pageDTO){
        LambdaQueryWrapper<FlowHandler> pageWrapper = new LambdaQueryWrapper<>();
        pageWrapper.eq(FlowHandler::getDefCode, pageDTO.getDefCode());
        pageWrapper.eq(FlowHandler::getDefVersion, pageDTO.getDefVersion());
        pageWrapper.eq(FlowHandler::getNodeId, pageDTO.getNodeId());
        pageWrapper.orderByDesc(FlowHandler::getCreateTime);
        return this.page(CommonPageRequest.constructPage(pageDTO.getCurrent(), pageDTO.getSize()), pageWrapper);
    }

}
