package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowBtn;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnVO;

/**
 * 流程按钮Service
 *
 * @author ohohmiao
 * @date 2025-06-08 09:49
 */
public interface FlowBtnService extends CommonService<FlowBtn> {

    /**
     * 获取流程按钮分页列表
     * @param pageDTO
     * @return
     */
    Page<FlowBtnVO> listByPage(FlowBtnPageDTO pageDTO);

    /**
     * 获取单个流程按钮
     * @param btnId
     * @return
     */
    FlowBtnVO get(String btnId);

    /**
     * 新增流程按钮
     * @param addOrEditDTO
     */
    void add(FlowBtnAddOrEditDTO addOrEditDTO);

    /**
     * 编辑流程按钮
     * @param addOrEditDTO
     */
    void edit(FlowBtnAddOrEditDTO addOrEditDTO);

    /**
     * 删除流程按钮
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

}
