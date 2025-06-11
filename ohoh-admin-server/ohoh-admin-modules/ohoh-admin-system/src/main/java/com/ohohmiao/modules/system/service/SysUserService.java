package com.ohohmiao.modules.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.modules.system.model.entity.SysUser;
import com.ohohmiao.modules.system.model.dto.SysUserAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysUserGrantDataDTO;
import com.ohohmiao.modules.system.model.dto.SysUserGrantRoleDTO;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantUserDTO;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import com.ohohmiao.modules.system.model.dto.SysUserPageDTO;
import com.ohohmiao.modules.system.model.vo.SysUserPropVO;

import java.util.List;
import java.util.Set;

/**
 * 系统用户Service
 *
 * @author ohohmiao
 * @date 2023-03-31 10:25
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 从缓存获取全量用户数据
     * @return
     */
    List<SysUserVO> listCachedAllUsers();

    /**
     * 根据用户id，列出其拥有的角色
     * @param userId
     * @return
     */
    List<SysRoleVO> listSysRoleByUserId(String userId);

    /**
     * 根据用户id，列出其拥有的角色
     *  -判断登录用户数据范围
     * @param userId
     * @return
     */
    List<SysRoleVO> listAuthSysRoleByUserId(String userId);

    /**
     * 获取授权的用户列表
     * @param sysUserPageDTO
     * @return
     */
    Page<SysUserVO> listAuthByPage(SysUserPageDTO sysUserPageDTO);

    /**
     * 根据用户id，列出其所在的组织
     * @param userId
     * @return
     */
    List<SysUserPropVO> listSysOrgByUserId(String userId);

    /**
     * 根据用户id，列出其所在的组织id
     * @param userId
     * @return
     */
    List<String> listSysOrgIdsByUserId(String userId);

    /**
     * 判断用户账号是否重复
     * @param sysUserAddOrEditDTO
     * @return
     */
    boolean isExistUserAccount(SysUserAddOrEditDTO sysUserAddOrEditDTO);

    /**
     * 给系统用户授权角色
     * @param grantRoleDTO
     */
    void grantRole(SysUserGrantRoleDTO grantRoleDTO);

    /**
     * 新增系统用户
     * @param sysUserAddOrEditDTO
     */
    void add(SysUserAddOrEditDTO sysUserAddOrEditDTO);

    /**
     * 编辑系统用户
     * @param sysUserAddOrEditDTO
     */
    void edit(SysUserAddOrEditDTO sysUserAddOrEditDTO);

    /**
     * 删除系统用户
     * @param idDTO
     */
    void delete(CommonIdDTO idDTO);

    /**
     * 更新用户状态
     * @param idDTO
     * @param userStatus
     */
    void updateUserStatus(CommonIdDTO idDTO, int userStatus);

    /**
     * 重置密码
     * @param idDTO
     */
    void resetPassword(CommonIdDTO idDTO);

    /**
     * 构建所授权的机构用户树
     * @return
     */
    List<Tree<String>> getAuthOrgUserTree();

    /**
     * 构建机构用户树
     * @return
     */
    List<Tree<String>> getOrgUserTree();

    /**
     * 根据角色id，列出其拥有的用户
     *    -判断登录用户数据范围
     * @param roleId
     * @return
     */
    List<SysUserVO> listAuthSysUsersByRoleId(String roleId);

    /**
     * 给系统角色分配用户
     * @param grantUserDTO
     */
    void grantRoleToUser(SysRoleGrantUserDTO grantUserDTO);

    /**
     * 列出某用户所配置的数据范围
     * @param userId
     * @return
     */
    Set<String> listDataScopesByUserId(String userId);

    /**
     * 给用户授权数据范围
     * @param grantDataDTO
     */
    void grantDataScope(SysUserGrantDataDTO grantDataDTO);

    /**
     * 给当前登录用户新增数据范围授权
     * @param addDatascopeOrgIds
     */
    void addGrantDataScope4LoginUser(List<String> addDatascopeOrgIds);

    /**
     * 构建岗位用户树
     * @return
     */
    List<Tree<String>> getPositionUserTree();

}
