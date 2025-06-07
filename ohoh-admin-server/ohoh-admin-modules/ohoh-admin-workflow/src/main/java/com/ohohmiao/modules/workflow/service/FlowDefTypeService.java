package com.ohohmiao.modules.workflow.service;

import cn.hutool.core.lang.tree.Tree;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;
import com.ohohmiao.modules.workflow.model.dto.FlowDefTypeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowDefType;
import com.ohohmiao.modules.workflow.model.vo.FlowDefTypeVO;

import java.util.List;

/**
 * 流程定义类别Service
 *
 * @author ohohmiao
 * @date 2024-12-01 21:49
 */
public interface FlowDefTypeService extends CommonTreeService<FlowDefType> {

    /**
     * 从缓存获取全量系统字典类别数据
     * @return
     */
    List<FlowDefTypeVO> listCachedAll();

    /**
     * 获取流程定义类别树
     * @return
     */
    List<Tree<String>> getTreeData();

    /**
     * 判断流程定义编码是否重复
     * @param flowDefTypeAddOrEditDTO
     * @return
     */
    boolean isExistDefTypeCode(FlowDefTypeAddOrEditDTO flowDefTypeAddOrEditDTO);

    /**
     * 新增流程定义类别
     * @param flowDefTypeAddOrEditDTO
     */
    void add(FlowDefTypeAddOrEditDTO flowDefTypeAddOrEditDTO);

    /**
     * 编辑流程定义类别
     * @param flowDefTypeAddOrEditDTO
     */
    void edit(FlowDefTypeAddOrEditDTO flowDefTypeAddOrEditDTO);

    /**
     * 删除流程定义类别
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

    /**
     * 查询流程类别的第一层树节点
     * @return
     */
    List<FlowDefTypeVO> listFirstLevelNodes();

}
