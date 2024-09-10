package com.ohohmiao.modules.system.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.model.dto.SysUserAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysUserGrantDataDTO;
import com.ohohmiao.modules.system.model.dto.SysUserGrantRoleDTO;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantUserDTO;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import com.ohohmiao.modules.system.enums.SysUserStatusEnum;
import com.ohohmiao.modules.system.model.dto.SysUserPageDTO;
import com.ohohmiao.modules.system.model.vo.SysUserPropVO;
import com.ohohmiao.modules.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 系统用户controller
 *
 * @author ohohmiao
 * @date 2023/4/16 15:51
 */
@Api(tags = "系统用户")
@ApiSupport(order = 8)
@RestController
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * 获取授权的用户列表
     * @param sysUserPageDTO 分页dto
     * @return
     */
    @ApiOperation(value = "获取授权的用户列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysUser/authPage")
    @PostMapping("/sysUser/authPage")
    public CommonResp<Page<SysUserVO>> authPage(@RequestBody SysUserPageDTO sysUserPageDTO) {
        return CommonResp.data(sysUserService.listAuthByPage(sysUserPageDTO));
    }

    /**
     * 查询某用户的所在组织列表
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "查询某用户的所在组织列表")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/sysUser/ownSysOrg")
    @PostMapping("/sysUser/ownSysOrg")
    public CommonResp<List<SysUserPropVO>> ownSysOrg(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysUserService.listSysOrgByUserId(idDTO.getId()));
    }

    /**
     * 新增用户
     * @param sysUserAddOrEditDTO 新增用户dto
     * @return
     */
    @ApiOperation(value = "新增用户")
    @ApiOperationSupport(order = 3)
    @CommonLog("新增用户")
    @SaPcCheckPermission("/sysUser/add")
    @PostMapping("/sysUser/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysUserAddOrEditDTO sysUserAddOrEditDTO){
        sysUserService.add(sysUserAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改用户
     * @param sysUserAddOrEditDTO 修改用户dto
     * @return
     */
    @ApiOperation(value = "修改用户")
    @ApiOperationSupport(order = 4)
    @CommonLog("修改用户")
    @SaPcCheckPermission("/sysUser/edit")
    @PostMapping("/sysUser/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysUserAddOrEditDTO sysUserAddOrEditDTO){
        sysUserService.edit(sysUserAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除用户
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "删除用户")
    @ApiOperationSupport(order = 5)
    @CommonLog("删除用户")
    @SaPcCheckPermission("/sysUser/delete")
    @PostMapping("/sysUser/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        sysUserService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

    /**
     * 查询某用户所授权的角色列表
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "查询某用户所授权的角色列表")
    @ApiOperationSupport(order = 6)
    @SaPcCheckPermission("/sysUser/ownSysRole")
    @PostMapping("/sysUser/ownSysRole")
    public CommonResp<List<SysRoleVO>> ownSysRole(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysUserService.listAuthSysRoleByUserId(idDTO.getId()));
    }

    /**
     * 给系统用户授权角色
     * @param grantRoleDTO 给用户授权角色dto
     * @return
     */
    @ApiOperation(value = "给系统用户授权角色")
    @ApiOperationSupport(order = 7)
    @CommonLog("给系统用户授权角色")
    @SaPcCheckPermission("/sysUser/grantRole")
    @PostMapping("/sysUser/grantRole")
    public CommonResp<String> grantRole(@RequestBody @Validated SysUserGrantRoleDTO grantRoleDTO){
        sysUserService.grantRole(grantRoleDTO);
        return CommonResp.success("操作成功");
    }

    /**
     * 启用用户
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "启用用户")
    @ApiOperationSupport(order = 8)
    @CommonLog("启用用户")
    @SaPcCheckPermission("/sysUser/enable")
    @PostMapping("/sysUser/enable")
    public CommonResp<String> enable(@RequestBody @Validated CommonIdDTO idDTO){
        sysUserService.updateUserStatus(idDTO, SysUserStatusEnum.ENABLE.getStatus());
        return CommonResp.success("操作成功");
    }

    /**
     * 禁用用户
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "禁用用户")
    @ApiOperationSupport(order = 9)
    @CommonLog("禁用用户")
    @SaPcCheckPermission("/sysUser/disable")
    @PostMapping("/sysUser/disable")
    public CommonResp<String> disable(@RequestBody @Validated CommonIdDTO idDTO){
        sysUserService.updateUserStatus(idDTO, SysUserStatusEnum.DISABLED.getStatus());
        return CommonResp.success("操作成功");
    }

    /**
     * 重置密码
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "重置密码")
    @ApiOperationSupport(order = 10)
    @CommonLog("重置密码")
    @SaPcCheckPermission("/sysUser/resetPassword")
    @PostMapping("/sysUser/resetPassword")
    public CommonResp<String> resetPassword(@RequestBody @Validated CommonIdDTO idDTO){
        sysUserService.resetPassword(idDTO);
        return CommonResp.success(StrUtil.format("密码已重置为{}", AuthConstEnum.DEFAULT_PASSWORD.getValue()));
    }

    /**
     * 获取所授权的机构用户树
     * @return
     */
    @ApiOperation(value = "获取所授权的机构用户树")
    @ApiOperationSupport(order = 11)
    @SaPcCheckPermission("/sysUser/authOrgUserTree")
    @PostMapping("/sysUser/authOrgUserTree")
    public CommonResp<List<Tree<String>>> authOrgUserTree(){
        return CommonResp.data(sysUserService.getAuthOrgUserTree());
    }

    /**
     * 列出某角色所授予的用户集合
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "列出某角色所授予的用户集合")
    @ApiOperationSupport(order = 12)
    @SaPcCheckPermission("/sysUser/listAuthSysUsersByRoleId")
    @PostMapping("/sysUser/listAuthSysUsersByRoleId")
    public CommonResp<List<SysUserVO>> listAuthSysUsersByRoleId(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysUserService.listAuthSysUsersByRoleId(idDTO.getId()));
    }

    /**
     * 给系统角色分配用户
     * @param grantUserDTO 给角色授权用户dto
     * @return
     */
    @ApiOperation(value = "给系统角色分配用户")
    @ApiOperationSupport(order = 12)
    @CommonLog("给系统角色分配用户")
    @SaPcCheckPermission("/sysUser/grantRoleToUser")
    @PostMapping("/sysUser/grantRoleToUser")
    public CommonResp<String> grantRoleToUser(@RequestBody @Validated SysRoleGrantUserDTO grantUserDTO){
        sysUserService.grantRoleToUser(grantUserDTO);
        return CommonResp.success("操作成功");
    }

    /**
     * 列出某用户所授予的数据范围
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "列出某用户所授予的数据范围")
    @ApiOperationSupport(order = 13)
    @SaPcCheckPermission("/sysUser/ownDataScope")
    @PostMapping("/sysUser/ownDataScope")
    public CommonResp<Set<String>> ownDataScope(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysUserService.listDataScopesByUserId(idDTO.getId()));
    }

    /**
     * 给用户授权数据范围
     * @param grantDataDTO 给用户授权数据范围dto
     * @return
     */
    @ApiOperation(value = "给用户授权数据范围")
    @ApiOperationSupport(order = 14)
    @CommonLog("给用户授权数据范围")
    @SaPcCheckPermission("/sysUser/grantDataScope")
    @PostMapping("/sysUser/grantDataScope")
    public CommonResp<String> grantDataScope(@RequestBody @Validated SysUserGrantDataDTO grantDataDTO){
        sysUserService.grantDataScope(grantDataDTO);
        return CommonResp.success("操作成功");
    }

    /**
     * 获取岗位用户树
     * @return
     */
    @ApiOperation(value = "获取岗位用户树")
    @ApiOperationSupport(order = 15)
    @SaPcCheckPermission("/sysUser/positionUserTree")
    @PostMapping("/sysUser/positionUserTree")
    public CommonResp<List<Tree<String>>> positionUserTree(){
        return CommonResp.data(sysUserService.getPositionUserTree());
    }

}
