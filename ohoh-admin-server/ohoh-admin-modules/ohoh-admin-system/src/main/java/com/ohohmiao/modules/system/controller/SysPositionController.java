package com.ohohmiao.modules.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.model.dto.SysPositionAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysPositionGrantUserDTO;
import com.ohohmiao.modules.system.model.dto.SysPositionUserPageDTO;
import com.ohohmiao.modules.system.model.vo.SysPositionUserVO;
import com.ohohmiao.modules.system.service.SysPositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位controller
 *
 * @author ohohmiao
 * @date 2023-06-20 11:47
 */
@Api(tags = "岗位")
@ApiSupport(order = 9)
@RestController
public class SysPositionController {

    @Resource
    private SysPositionService sysPositionService;

    /**
     * 获取全部岗位树
     * @return
     */
    @ApiOperation(value = "获取全部岗位树")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysPosition/tree")
    @PostMapping("/sysPosition/tree")
    public CommonResp<List<Tree<String>>> tree(){
        return CommonResp.data(sysPositionService.getTreeData());
    }

    /**
     * 新增岗位
     * @param sysPositionAddOrEditDTO 新增岗位dto
     * @return
     */
    @ApiOperation(value = "新增岗位")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增岗位")
    @SaPcCheckPermission("/sysPosition/add")
    @PostMapping("/sysPosition/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysPositionAddOrEditDTO sysPositionAddOrEditDTO){
        sysPositionService.add(sysPositionAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改岗位
     * @param sysPositionAddOrEditDTO 修改岗位dto
     * @return
     */
    @ApiOperation(value = "修改岗位")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改岗位")
    @SaPcCheckPermission("/sysPosition/edit")
    @PostMapping("/sysPosition/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysPositionAddOrEditDTO sysPositionAddOrEditDTO){
        sysPositionService.edit(sysPositionAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除岗位
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "删除岗位")
    @ApiOperationSupport(order = 4)
    @CommonLog("删除岗位")
    @SaPcCheckPermission("/sysPosition/delete")
    @PostMapping("/sysPosition/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        sysPositionService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

    /**
     * 获取岗位用户分页列表
     * @param sysPositionUserPageDTO 分页dto
     * @return
     */
    @ApiOperation(value = "获取岗位用户分页列表")
    @ApiOperationSupport(order = 5)
    @SaPcCheckPermission("/sysPosition/pageUser")
    @PostMapping("/sysPosition/pageUser")
    public CommonResp<Page<SysPositionUserVO>> pageUser(@RequestBody SysPositionUserPageDTO sysPositionUserPageDTO){
        return CommonResp.data(sysPositionService.listUserByPage(sysPositionUserPageDTO));
    }

    /**
     * 获取某岗位的用户列表
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "获取某岗位的用户列表")
    @ApiOperationSupport(order = 6)
    @SaPcCheckPermission("/sysPosition/listUser")
    @PostMapping("/sysPosition/listUser")
    public CommonResp<List<SysPositionUserVO>> listUser(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysPositionService.listUserByPositionId(idDTO.getId()));
    }

    /**
     * 给岗位授权用户
     * @param grantUserDTO 给岗位授权用户dto
     * @return
     */
    @ApiOperation(value = "给岗位授权用户")
    @ApiOperationSupport(order = 7)
    @CommonLog("给岗位授权用户")
    @SaPcCheckPermission("/sysPosition/grantUser")
    @PostMapping("/sysPosition/grantUser")
    public CommonResp<String> grantUser(@RequestBody @Validated SysPositionGrantUserDTO grantUserDTO){
        sysPositionService.grantUser(grantUserDTO);
        return CommonResp.success("操作成功");
    }

    /**
     * 取消授权岗位用户
     * @param idListDTO id集合dto
     * @return
     */
    @ApiOperation(value = "取消授权岗位用户")
    @ApiOperationSupport(order = 8)
    @CommonLog("取消授权岗位用户")
    @SaPcCheckPermission("/sysPosition/revokeUser")
    @PostMapping("/sysPosition/revokeUser")
    public CommonResp<String> revokeUser(@RequestBody @Validated CommonIdListDTO idListDTO){
        sysPositionService.revokeUser(idListDTO);
        return CommonResp.success("操作成功");
    }

}
