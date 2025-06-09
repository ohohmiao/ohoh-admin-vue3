package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnBindPageDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowBtn;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnBindVO;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnVO;

import java.util.List;

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
    Page<FlowBtn> listByPage(FlowBtnPageDTO pageDTO);

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

    /**
     * 获取流程按钮环节绑定分页列表
     * @param pageDTO
     * @return
     */
    Page<FlowBtnBindVO> listBindByPage(FlowBtnBindPageDTO pageDTO);

    /**
     * 保存或更新流程按钮绑定
     * @param addOrEditDTO
     */
    void saveOrUpdateBind(FlowBtnBindAddOrEditDTO addOrEditDTO);

    /**
     * 批量删除流程按钮绑定
     * @param idListDTO
     */
    void multiDeleteBind(CommonIdListDTO idListDTO);

    /**
     * 获取流程按钮下拉框结构数据
     * @return
     */
    List<CommonSelectVO> select();

}
