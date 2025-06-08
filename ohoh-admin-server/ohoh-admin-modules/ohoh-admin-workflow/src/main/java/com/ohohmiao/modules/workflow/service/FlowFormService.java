package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowFormBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowFormBindPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormBindVO;

/**
 * 流程表单Service
 *
 * @author ohohmiao
 * @date 2025-06-07 14:53
 */
public interface FlowFormService extends CommonService<FlowForm> {

    /**
     * 获取流程表单绑定分页列表
     * @param pageDTO
     * @return
     */
    Page<FlowFormBindVO> listBindByPage(FlowFormBindPageDTO pageDTO);

    /**
     * 新增流程表单绑定
     * @param addOrEditDTO
     */
    void addBind(FlowFormBindAddOrEditDTO addOrEditDTO);

    /**
     * 修改流程表单绑定
     * @param addOrEditDTO
     */
    void editBind(FlowFormBindAddOrEditDTO addOrEditDTO);

    /**
     * 批量删除流程表单绑定
     * @param idListDTO
     */
    void multiDeleteBind(CommonIdListDTO idListDTO);

}
