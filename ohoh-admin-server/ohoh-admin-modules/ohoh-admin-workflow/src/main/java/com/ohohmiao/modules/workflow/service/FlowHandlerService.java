package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowHandlerVO;

import java.util.List;

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

    /**
     * 获取单个环节办理人配置
     * @param handlerId
     * @return
     */
    FlowHandlerVO get(String handlerId);

    /**
     * 获取下一环节办理人配置
     * @param defCode
     * @param defVersion
     * @param nextNodeId
     * @return
     */
    FlowHandlerVO getNextNodeFlowHandler(String defCode, Integer defVersion, String nextNodeId);

    /**
     * 新增环节办理人配置
     * @param flowHandlerAddOrEditDTO
     */
    void add(FlowHandlerAddOrEditDTO flowHandlerAddOrEditDTO);

    /**
     * 编辑环节办理人配置
     * @param flowHandlerAddOrEditDTO
     */
    void edit(FlowHandlerAddOrEditDTO flowHandlerAddOrEditDTO);

    /**
     * 批量删除环节办理人配置
     * @param idListDTO
     */
    void multiDelete(CommonIdListDTO idListDTO);

    /**
     * 查询配置为指定人员情形的流程环节办理人
     * @param defCode
     * @param defVersion
     * @param nodeId
     * @return
     */
    List<FlowTaskHandler> listFlowNodeHandler4ReferRes(String defCode, Integer defVersion, String nodeId);

}
