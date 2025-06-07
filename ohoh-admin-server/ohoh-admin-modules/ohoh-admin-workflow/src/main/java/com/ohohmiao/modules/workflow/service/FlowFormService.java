package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowFormAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowFormPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormVO;

/**
 * 流程表单Service
 *
 * @author ohohmiao
 * @date 2025-06-07 14:53
 */
public interface FlowFormService extends CommonService<FlowForm> {

    /**
     * 获取流程表单分页列表
     * @param pageDTO
     * @return
     */
    Page<FlowFormVO> listByPage(FlowFormPageDTO pageDTO);

    /**
     * 获取单个流程表单
     * @param formId
     * @return
     */
    FlowFormVO get(String formId);

    /**
     * 新增流程表单
     * @param addOrEditDTO
     */
    void add(FlowFormAddOrEditDTO addOrEditDTO);

    /**
     * 编辑流程表单
     * @param addOrEditDTO
     */
    void edit(FlowFormAddOrEditDTO addOrEditDTO);

    /**
     * 删除流程表单
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

}
