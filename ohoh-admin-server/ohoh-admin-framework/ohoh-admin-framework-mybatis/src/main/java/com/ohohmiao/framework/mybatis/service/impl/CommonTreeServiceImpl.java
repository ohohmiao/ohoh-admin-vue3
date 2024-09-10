package com.ohohmiao.framework.mybatis.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;

import java.util.Map;

/**
 * 基础树形Service实现类
 *
 * @author ohohmiao
 * @date 2023-08-24 11:52
 */
public class CommonTreeServiceImpl<M extends BaseMapper<T>, T extends CommonTreeEntity>
        extends CommonServiceImpl<M, T> implements CommonTreeService<T> {

    @Override
    public void saveTreeData(T entity){
        // 设置树形结构相关字段
        // 树形路径+树形等级
        String parentPath;
        if(!"0".equals(entity.getParentId())){
            T parentEntity = this.getById(entity.getParentId());
            if(ObjectUtil.isNull(parentEntity)){
                throw new CommonException("上级节点不存在，请重新选择！");
            }
            entity.setTreeLevel(parentEntity.getTreeLevel()+1);
            parentPath = parentEntity.getTreePath();
        }else{
            entity.setTreeLevel(1);
            parentPath = "0.";
        }
        // 树形排序
        Integer maxTreeSort = this.getMaxSortByParentId(entity.getParentId());
        entity.setTreeSort(ObjectUtil.isNull(maxTreeSort) ? 1 : maxTreeSort + 1);
        this.save(entity);
        // 更新树path
        Object primaryKeyVal = this.getPrimaryKeyVals(entity)[0];
        entity.setTreePath(StrUtil.format("{}{}.", parentPath, primaryKeyVal));
        this.updateById(entity);
    }

    @Override
    public Integer getMaxSortByParentId(String parentId){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(TREE_SORT) as TREE_SORT");
        queryWrapper.eq("PARENT_ID", parentId);
        Map<String, Object> result = this.getMap(queryWrapper);
        if(MapUtil.isNotEmpty(result)){
            return Integer.valueOf(result.get("TREE_SORT").toString());
        }else{
            return 0;
        }
    }

    @Override
    public void updateTreeData(T entity, String oldTreePath, Integer oldTreeLevel){
        // 设置树形结构相关字段
        // 树形路径+树形等级
        String newTreePath;
        int newTreeLevel;
        Object primaryKeyVal = this.getPrimaryKeyVals(entity)[0];
        if(!"0".equals(entity.getParentId())){
            T parentEntity = this.getById(entity.getParentId());
            if(ObjectUtil.isNull(parentEntity)){
                throw new CommonException("上级节点不存在，请重新选择！");
            }
            newTreeLevel = parentEntity.getTreeLevel() + 1;
            newTreePath = StrUtil.format("{}{}.", parentEntity.getTreePath(), primaryKeyVal);
        }else{
            newTreeLevel = 1;
            newTreePath = StrUtil.format("0.{}.", primaryKeyVal);
        }
        entity.setTreeLevel(newTreeLevel);
        entity.setTreePath(newTreePath);
        this.updateById(entity);
        // 更新孩子节点的树形数据
        UpdateWrapper<T> updateChildWrapper = new UpdateWrapper<>();
        updateChildWrapper.likeRight("TREE_PATH", oldTreePath);
        updateChildWrapper.setSql("TREE_PATH=replace(TREE_PATH, '"+oldTreePath+"', '"+newTreePath+"')");
        int treeLevelOffset = newTreeLevel - oldTreeLevel;
        updateChildWrapper.setSql("TREE_LEVEL=TREE_LEVEL+("+treeLevelOffset+")");
        this.update(updateChildWrapper);
    }

    @Override
    public Long countByParentId(String parentId){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PARENT_ID", parentId);
        return this.count(queryWrapper);
    }

}
