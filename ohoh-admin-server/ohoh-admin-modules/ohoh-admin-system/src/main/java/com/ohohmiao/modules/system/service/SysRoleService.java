package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.modules.system.model.entity.SysRole;
import com.ohohmiao.modules.system.model.dto.SysRoleAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantDataDTO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantResDTO;
import com.ohohmiao.modules.system.model.dto.SysRolePageDTO;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;

import java.util.Set;

/**
 * 系统角色Service
 *
 * @author ohohmiao
 * @date 2023-04-06 14:20
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 列出某角色的数据范围
     * @param roleId
     * @return
     */
    Set<String> listDataScopesByRoleId(String roleId);

    /**
     * 获取授权的系统角色列表
     * @param sysRolePageDTO
     * @return
     */
    Page<SysRoleVO> listAuthByPage(SysRolePageDTO sysRolePageDTO);

    /**
     * 判断是否存在重复的角色名称
     * @param sysRoleAddOrEditDTO
     * @return
     */
    boolean isExistRoleName(SysRoleAddOrEditDTO sysRoleAddOrEditDTO);

    /**
     * 新增角色
     * @param sysRoleAddOrEditDTO
     */
    void add(SysRoleAddOrEditDTO sysRoleAddOrEditDTO);

    /**
     * 编辑角色
     * @param sysRoleAddOrEditDTO
     */
    void edit(SysRoleAddOrEditDTO sysRoleAddOrEditDTO);

    /**
     * 删除角色
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

    /**
     * 给角色授权数据范围
     * @param sysRoleGrantDataDTO
     */
    void grantDataScope(SysRoleGrantDataDTO sysRoleGrantDataDTO);

    /**
     * 给角色授权系统资源
     * @param grantResDTO
     */
    void grantRes(SysRoleGrantResDTO grantResDTO);

    /**
     * 列出某角色所授予的资源id集合
     * @param roleId
     * @return
     */
    Set<String> listGrantedResByRoleId(String roleId);

    /**
     * 根据机构id统计
     * @param orgId
     * @return
     */
    Long countByOrgId(String orgId);

}
