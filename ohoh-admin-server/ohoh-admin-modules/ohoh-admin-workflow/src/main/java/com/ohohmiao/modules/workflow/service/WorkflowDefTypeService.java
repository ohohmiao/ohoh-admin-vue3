package com.ohohmiao.modules.workflow.service;

import cn.hutool.core.lang.tree.Tree;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;
import com.ohohmiao.modules.workflow.model.dto.WorkflowDefTypeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.WorkflowDefType;
import com.ohohmiao.modules.workflow.model.vo.WorkflowDefTypeVO;

import java.util.List;

/**
 * 流程定义类别Service
 *
 * @author ohohmiao
 * @date 2024-12-01 21:49
 */
public interface WorkflowDefTypeService extends CommonTreeService<WorkflowDefType> {

    /**
     * 从缓存获取全量系统字典类别数据
     * @return
     */
    List<WorkflowDefTypeVO> listCachedAll();

    /**
     * 获取流程定义类别树
     * @return
     */
    List<Tree<String>> getTreeData();

    /**
     * 判断流程定义编码是否重复
     * @param workflowDefTypeAddOrEditDTO
     * @return
     */
    boolean isExistDefTypeCode(WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO);

    /**
     * 新增流程定义类别
     * @param workflowDefTypeAddOrEditDTO
     */
    void add(WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO);

    /**
     * 编辑流程定义类别
     * @param workflowDefTypeAddOrEditDTO
     */
    void edit(WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO);

    /**
     * 删除流程定义类别
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

}
