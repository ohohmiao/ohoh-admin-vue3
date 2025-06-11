package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowEventAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowEventPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowEvent;
import com.ohohmiao.modules.workflow.model.vo.FlowEventVO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;

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

    /**
     * 新增流程事件
     * @param flowEventAddOrEditDTO
     */
    void add(FlowEventAddOrEditDTO flowEventAddOrEditDTO);

    /**
     * 修改流程事件
     * @param flowEventAddOrEditDTO
     */
    void edit(FlowEventAddOrEditDTO flowEventAddOrEditDTO);

    /**
     * 批量删除流程事件
     * @param idListDTO
     */
    void multiDelete(CommonIdListDTO idListDTO);

    /**
     * 获取流程事件
     * @param defCode
     * @param defVersion
     * @param nodeId
     * @param eventType
     * @return
     */
    FlowEventVO get(String defCode, Integer defVersion, String nodeId, Integer eventType);

    /**
     * 执行流程事件
     * @param flowInfoVO
     * @param eventType
     * @return
     */
    FlowInfoVO executeBindEvent(FlowInfoVO flowInfoVO, Integer eventType);

}
