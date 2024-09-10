package com.ohohmiao.modules.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;
import com.ohohmiao.modules.system.model.entity.SysRes;
import com.ohohmiao.modules.system.model.dto.SysResAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysResQueryDTO;

import java.util.List;
import java.util.Map;

/**
 * 系统资源Service
 *
 * @author ohohmiao
 * @date 2023-04-04 11:22
 */
public interface SysResService extends CommonTreeService<SysRes> {

    /**
     * 列出当前用户所授权的菜单
     * @return
     */
    List<Tree<String>> listAuthMenu();

    /**
     * 列出当前用户所授权的按钮key
     * @return
     */
    Map<String, List<String>> listAuthButton();

    /**
     * 获取系统资源树
     * @param sysResQueryDTO
     * @return
     */
    List<Tree<String>> getTreeData(SysResQueryDTO sysResQueryDTO);

    /**
     * 获取授权的系统资源树
     * @return
     */
    List<Tree<String>> getAuthTreeData();

    /**
     * 判断同级是否存在重复的资源名称
     * @param sysResAddOrEditDTO
     * @return
     */
    boolean isExistResNameInSameLevel(SysResAddOrEditDTO sysResAddOrEditDTO);

    /**
     * 判断是否重复的资源编码
     * @param sysResAddOrEditDTO
     * @return
     */
    boolean isExistResCode(SysResAddOrEditDTO sysResAddOrEditDTO);

    /**
     * 新增资源
     * @param sysResAddOrEditDTO
     */
    void add(SysResAddOrEditDTO sysResAddOrEditDTO);

    /**
     * 修改资源
     * @param sysResAddOrEditDTO
     */
    void edit(SysResAddOrEditDTO sysResAddOrEditDTO);

    /**
     * 删除资源
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

}
