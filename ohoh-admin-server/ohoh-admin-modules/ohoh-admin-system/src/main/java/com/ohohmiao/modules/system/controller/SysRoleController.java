package com.ohohmiao.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.model.dto.SysRoleAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantDataDTO;
import com.ohohmiao.modules.system.model.dto.SysRoleGrantResDTO;
import com.ohohmiao.modules.system.model.dto.SysRolePageDTO;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import com.ohohmiao.modules.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 角色controller
 *
 * @author ohohmiao
 * @date 2023/4/15 13:13
 */
@Api(tags = "组织机构")
@ApiSupport(order = 7)
@RestController
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取授权的角色列表
     * @param sysRolePageDTO 分页dto
     * @return
     */
    @ApiOperation(value = "获取授权的角色列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysRole/authPage")
    @PostMapping("/sysRole/authPage")
    public CommonResp<Page<SysRoleVO>> authPage(@RequestBody SysRolePageDTO sysRolePageDTO) {
        return CommonResp.data(sysRoleService.listAuthByPage(sysRolePageDTO));
    }

    /**
     * 新增角色
     * @param sysRoleAddOrEditDTO 新增角色dto
     * @return
     */
    @ApiOperation(value = "新增角色")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增角色")
    @SaPcCheckPermission("/sysRole/add")
    @PostMapping("/sysRole/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysRoleAddOrEditDTO sysRoleAddOrEditDTO){
        sysRoleService.add(sysRoleAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改角色
     * @param sysRoleAddOrEditDTO 修改角色dto
     * @return
     */
    @ApiOperation(value = "修改角色")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改角色")
    @SaPcCheckPermission("/sysRole/edit")
    @PostMapping("/sysRole/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysRoleAddOrEditDTO sysRoleAddOrEditDTO){
        sysRoleService.edit(sysRoleAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除角色
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "删除角色")
    @ApiOperationSupport(order = 4)
    @CommonLog("删除角色")
    @SaPcCheckPermission("/sysRole/delete")
    @PostMapping("/sysRole/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        sysRoleService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

    /**
     * 给角色授权数据范围
     * @param grantDataParam 给角色授权数据范围dto
     * @return
     */
    @ApiOperation(value = "给角色授权数据范围")
    @ApiOperationSupport(order = 5)
    @CommonLog("给角色授权数据范围")
    @SaPcCheckPermission("/sysRole/grantDataScope")
    @PostMapping("/sysRole/grantDataScope")
    public CommonResp<String> grantDataScope(@RequestBody @Validated SysRoleGrantDataDTO grantDataParam){
        sysRoleService.grantDataScope(grantDataParam);
        return CommonResp.success("操作成功");
    }

    /**
     * 给角色授权系统资源
     * @param grantResDTO 给角色授权资源dto
     * @return
     */
    @ApiOperation(value = "给角色授权系统资源")
    @ApiOperationSupport(order = 6)
    @CommonLog("给角色授权系统资源")
    @SaPcCheckPermission("/sysRole/grantRes")
    @PostMapping("/sysRole/grantRes")
    public CommonResp<String> grantRes(@RequestBody @Validated SysRoleGrantResDTO grantResDTO){
        sysRoleService.grantRes(grantResDTO);
        return CommonResp.success("操作成功");
    }

    /**
     * 列出某角色所授予的资源id集合
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "列出某角色所授予的资源id集合")
    @ApiOperationSupport(order = 7)
    @SaPcCheckPermission("/sysRole/ownSysRes")
    @PostMapping("/sysRole/ownSysRes")
    public CommonResp<Set<String>> ownSysRes(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysRoleService.listGrantedResByRoleId(idDTO.getId()));
    }

}
