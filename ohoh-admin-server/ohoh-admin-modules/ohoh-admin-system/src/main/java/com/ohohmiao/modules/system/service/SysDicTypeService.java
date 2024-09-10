package com.ohohmiao.modules.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;
import com.ohohmiao.modules.system.model.entity.SysDicType;
import com.ohohmiao.modules.system.model.dto.SysDicTypeAddOrEditDTO;
import com.ohohmiao.modules.system.model.vo.SysDicTypeVO;

import java.util.List;

/**
 * 系统字典类别Service
 *
 * @author ohohmiao
 * @date 2023-05-25 11:54
 */
public interface SysDicTypeService extends CommonTreeService<SysDicType> {

    /**
     * 从缓存获取全量系统字典类别数据
     * @return
     */
    List<SysDicTypeVO> listCachedAll();

    /**
     * 获取系统字典类别树
     * @return
     */
    List<Tree<String>> getTreeData();

    /**
     * 判断系统字典编码是否重复
     * @param sysDicTypeAddOrEditDTO
     * @return
     */
    boolean isExistDicTypeCode(SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO);

    /**
     * 新增系统字典类别
     * @param sysDicTypeAddOrEditDTO
     */
    void add(SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO);

    /**
     * 编辑系统字典类别
     * @param sysDicTypeAddOrEditDTO
     */
    void edit(SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO);

    /**
     * 删除系统字典类别
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

}
