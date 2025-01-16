package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowEventPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowEvent;
import com.ohohmiao.modules.workflow.model.vo.FlowEventVO;

/**
 * 流程事件Service
 *
 * @author ohohmiao
 * @date 2025-01-16 09:58
 */
public interface FlowEventService extends CommonService<FlowEvent> {

    /**
     * 获取流程事件分页列表
     * @param flowEventPageDTO
     * @return
     */
    Page<FlowEventVO> listByPage(FlowEventPageDTO flowEventPageDTO);

}
