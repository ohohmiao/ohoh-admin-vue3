package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;

/**
 * 流程环节办理人配置Service
 *
 * @author ohohmiao
 * @date 2025-06-01 11:00
 */
public interface FlowHandlerService extends IService<FlowHandler> {

    /**
     * 获取流程某环节办理人配置分页列表
     * @param pageDTO
     * @return
     */
    Page<FlowHandler> listByPage(FlowHandlerPageDTO pageDTO);

}
