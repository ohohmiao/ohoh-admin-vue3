package com.ohohmiao.framework.mybatis.service;

import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;

/**
 * 基础树形Service
 *
 * @author ohohmiao
 * @date 2023-08-24 11:52
 */
public interface CommonTreeService<T extends CommonTreeEntity> extends CommonService<T> {

    /**
     * 保存树形实体数据
     * @param entity 实体
     */
    void saveTreeData(T entity);

    /**
     * 查找某父节点下的最大排序
     * @param parentId 父节点id
     * @return 最大排序值
     */
    Integer getMaxSortByParentId(String parentId);

    /**
     * 更新树形实体数据
     * @param entity 实体
     * @param oldTreePath 树形路径旧值
     * @param oldTreeLevel 树形等级旧值
     */
    void updateTreeData(T entity, String oldTreePath, Integer oldTreeLevel);

    /**
     * 统计某父节点的孩子数量
     * @param parentId 父节点id
     * @return 孩子数量
     */
    Long countByParentId(String parentId);

}
