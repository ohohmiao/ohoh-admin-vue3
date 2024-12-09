package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowDefAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowDefPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;

/**
 * 流程定义Service
 *
 * @author ohohmiao
 * @date 2024-12-08 19:55
 */
public interface FlowDefService extends CommonService<FlowDef> {

    /**
     * 获取流程定义分页列表
     * @param flowDefPageDTO
     * @return
     */
    Page<FlowDefVO> listByPage(FlowDefPageDTO flowDefPageDTO);

    /**
     * 判断是否重复的流程编码
     * @param flowDefAddOrEditDTO
     * @return
     */
    boolean isExistDefCode(FlowDefAddOrEditDTO flowDefAddOrEditDTO);

    /**
     * 新增流程定义
     * @param flowDefAddOrEditDTO
     */
    void add(FlowDefAddOrEditDTO flowDefAddOrEditDTO);

    /**
     * 获取单个流程定义
     * @param defId
     * @return
     */
    FlowDefVO get(String defId);

}
