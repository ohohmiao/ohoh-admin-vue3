package com.ohohmiao.modules.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.model.dto.SysResAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysResQueryDTO;
import com.ohohmiao.modules.system.service.SysResService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统资源controller
 *
 * @author ohohmiao
 * @date 2023-04-07 11:30
 */
@Api(tags = "系统资源")
@ApiSupport(order = 3)
@RestController
public class SysResController {

    @Resource
    private SysResService sysResService;

    /**
     * 查询授权的菜单
     * @return
     */
    @ApiOperation(value = "查询授权的菜单")
    @ApiOperationSupport(order = 1)
    @GetMapping("/sysRes/listAuthMenu")
    public CommonResp<List<Tree<String>>> listAuthMenu() {
        return CommonResp.data(sysResService.listAuthMenu());
    }

    /**
     * 查询授权的按钮
     * @return
     */
    @ApiOperation(value = "查询授权的按钮")
    @ApiOperationSupport(order = 2)
    @GetMapping("/sysRes/listAuthButton")
    public CommonResp<Map<String, List<String>>> listAuthButton(){
        return CommonResp.data(sysResService.listAuthButton());
    }

    /**
     * 获取系统资源树
     * @param sysResQueryDTO 查询dto
     * @return
     */
    @ApiOperation(value = "获取系统资源树")
    @ApiOperationSupport(order = 3)
    @SaPcCheckPermission("/sysRes/tree")
    @PostMapping("/sysRes/tree")
    public CommonResp<List<Tree<String>>> tree(@RequestBody SysResQueryDTO sysResQueryDTO){
        return CommonResp.data(sysResService.getTreeData(sysResQueryDTO));
    }

    /**
     * 获取授权的系统资源树
     * @return
     */
    @ApiOperation(value = "获取授权的系统资源树")
    @ApiOperationSupport(order = 4)
    @SaPcCheckPermission("/sysRes/authTree")
    @PostMapping("/sysRes/authTree")
    public CommonResp<List<Tree<String>>> authTree(){
        return CommonResp.data(sysResService.getAuthTreeData());
    }

    /**
     * 新增系统资源
     * @param sysResAddOrEditDTO 新增资源dto
     * @return
     */
    @ApiOperation(value = "新增系统资源")
    @ApiOperationSupport(order = 5)
    @CommonLog("新增系统资源")
    @SaPcCheckPermission("/sysRes/add")
    @PostMapping("/sysRes/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysResAddOrEditDTO sysResAddOrEditDTO){
        sysResService.add(sysResAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改系统资源
     * @param sysResAddOrEditDTO 修改资源dto
     * @return
     */
    @ApiOperation(value = "修改系统资源")
    @ApiOperationSupport(order = 6)
    @CommonLog("修改系统资源")
    @SaPcCheckPermission("/sysRes/edit")
    @PostMapping("/sysRes/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysResAddOrEditDTO sysResAddOrEditDTO){
        sysResService.edit(sysResAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除系统资源
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "删除系统资源")
    @ApiOperationSupport(order = 7)
    @CommonLog("删除系统资源")
    @SaPcCheckPermission("/sysRes/delete")
    @PostMapping("/sysRes/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        sysResService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
