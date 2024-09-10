package com.ohohmiao.modules.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.mybatis.service.CommonTreeService;
import com.ohohmiao.modules.system.model.entity.SysOrg;
import com.ohohmiao.modules.system.model.dto.SysOrgAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysOrgPageDTO;
import com.ohohmiao.modules.system.model.vo.SysOrgVO;

import java.util.List;

/**
 * 组织机构Service
 *
 * @author ohohmiao
 * @date 2023-04-06 14:17
 */
public interface SysOrgService extends CommonTreeService<SysOrg> {

    /**
     * 从缓存获取全量机构数据
     * @return
     */
    List<SysOrgVO> listCachedAllOrgs();

    /**
     * 从缓存中查找某机构的孩子
     * @param orgId
     * @param isIncludeSelf 是否包含父节点
     * @return
     */
    List<SysOrgVO> listCachedChildrenByOrgId(String orgId, boolean isIncludeSelf);

    /**
     * 获取授权的组织机构树
     * @return
     */
    List<Tree<String>> getAuthTreeData();

    /**
     * 获取组织机构树
     * @return
     */
    List<Tree<String>> getTreeData();

    /**
     * 获取授权的组织机构列表
     * @param sysOrgPageDTO
     * @return
     */
    Page<SysOrg> listAuthByPage(SysOrgPageDTO sysOrgPageDTO);

    /**
     * 判断同级是否存在重复的组织机构名称
     * @param sysOrgAddOrEditDTO
     * @return
     */
    boolean isExistOrgNameInSameLevel(SysOrgAddOrEditDTO sysOrgAddOrEditDTO);

    /**
     * 判断是否重复的组织机构编码
     * @param sysOrgAddOrEditDTO
     * @return
     */
    boolean isExistOrgCode(SysOrgAddOrEditDTO sysOrgAddOrEditDTO);

    /**
     * 新增组织机构
     * @param sysOrgAddOrEditDTO
     */
    void add(SysOrgAddOrEditDTO sysOrgAddOrEditDTO);

    /**
     * 编辑组织机构
     * @param sysOrgAddOrEditDTO
     */
    void edit(SysOrgAddOrEditDTO sysOrgAddOrEditDTO);

    /**
     * 删除组织机构
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

    /**
     * 根据组织机构id集合查询
     * @param orgIds
     * @return
     */
    List<SysOrgVO> listByOrgIds(List<String> orgIds);

}
