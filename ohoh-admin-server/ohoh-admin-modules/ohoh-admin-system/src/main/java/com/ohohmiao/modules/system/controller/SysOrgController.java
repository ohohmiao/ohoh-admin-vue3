package com.ohohmiao.modules.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.model.entity.SysOrg;
import com.ohohmiao.modules.system.model.dto.SysOrgAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysOrgPageDTO;
import com.ohohmiao.modules.system.service.SysOrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组织机构controller
 *
 * @author ohohmiao
 * @date 2023-04-13 9:10
 */
@Api(tags = "组织机构")
@ApiSupport(order = 6)
@RestController
public class SysOrgController {

    @Resource
    private SysOrgService sysOrgService;

    /**
     * 获取授权的组织机构树
     *    包含授权节点的父辈节点
     * @return
     */
    @ApiOperation(value = "获取授权的组织机构树-包含授权节点的父辈节点")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysOrg/authTree")
    @PostMapping("/sysOrg/authTree")
    public CommonResp<List<Tree<String>>> authTree(){
        return CommonResp.data(sysOrgService.getAuthTreeData());
    }

    /**
     * 获取全部组织机构树
     * @return
     */
    @ApiOperation(value = "获取全部组织机构树")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/sysOrg/tree")
    @PostMapping("/sysOrg/tree")
    public CommonResp<List<Tree<String>>> tree(){
        return CommonResp.data(sysOrgService.getTreeData());
    }

    /**
     * 获取授权的组织机构列表
     * @param sysOrgPageDTO 分页dto
     * @return
     */
    @ApiOperation(value = "获取授权的组织机构列表")
    @ApiOperationSupport(order = 3)
    @SaPcCheckPermission("/sysOrg/authPage")
    @PostMapping("/sysOrg/authPage")
    public CommonResp<Page<SysOrg>> authPage(@RequestBody SysOrgPageDTO sysOrgPageDTO) {
        return CommonResp.data(sysOrgService.listAuthByPage(sysOrgPageDTO));
    }

    /**
     * 新增组织机构
     * @param sysOrgAddOrEditDTO 新增组织dto
     * @return
     */
    @ApiOperation(value = "新增组织机构")
    @ApiOperationSupport(order = 4)
    @CommonLog("新增组织机构")
    @SaPcCheckPermission("/sysOrg/add")
    @PostMapping("/sysOrg/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysOrgAddOrEditDTO sysOrgAddOrEditDTO){
        sysOrgService.add(sysOrgAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改组织机构
     * @param sysOrgAddOrEditDTO 修改组织dto
     * @return
     */
    @ApiOperation(value = "修改组织机构")
    @ApiOperationSupport(order = 5)
    @CommonLog("修改组织机构")
    @SaPcCheckPermission("/sysOrg/edit")
    @PostMapping("/sysOrg/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysOrgAddOrEditDTO sysOrgAddOrEditDTO){
        sysOrgService.edit(sysOrgAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除组织机构
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "删除组织机构")
    @ApiOperationSupport(order = 6)
    @CommonLog("删除组织机构")
    @SaPcCheckPermission("/sysOrg/delete")
    @PostMapping("/sysOrg/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        sysOrgService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
