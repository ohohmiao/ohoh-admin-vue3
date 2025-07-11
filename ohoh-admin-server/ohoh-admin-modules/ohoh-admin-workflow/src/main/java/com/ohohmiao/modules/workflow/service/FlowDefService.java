package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowDefAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowDefPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;

import java.util.Set;

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

    /**
     * 修改流程定义
     * @param flowDefAddOrEditDTO
     */
    void edit(FlowDefAddOrEditDTO flowDefAddOrEditDTO);

    /**
     * 删除流程定义
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

    /**
     * 根据流程编码和版本号获取
     * @param defCode
     * @param defVersion
     * @return
     */
    FlowDef getByDefCodeAndDefVersion(String defCode, Integer defVersion);

    /**
     * 列出被@FlowEntity标注的类名
     * @return
     */
    Set<String> listFlowEnitityClassNames();

}
