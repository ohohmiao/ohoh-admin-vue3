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
import com.ohohmiao.modules.system.model.dto.SysDicTypeAddOrEditDTO;
import com.ohohmiao.modules.system.service.SysDicTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统字典类别controller
 *
 * @author ohohmiao
 * @date 2023-05-25 11:59
 */
@Api(tags = "系统字典类别")
@ApiSupport(order = 1)
@RestController
public class SysDicTypeController {

    @Resource
    private SysDicTypeService sysDicTypeService;

    /**
     * 获取系统字典类别树
     * @return
     */
    @ApiOperation(value = "获取系统字典类别树")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysDicType/tree")
    @PostMapping("/sysDicType/tree")
    public CommonResp<List<Tree<String>>> tree(){
        return CommonResp.data(sysDicTypeService.getTreeData());
    }

    /**
     * 新增系统字典类别
     * @param sysDicTypeAddOrEditDTO 新增字典类别dto
     * @return
     */
    @ApiOperation(value = "新增系统字典类别")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增系统字典类别")
    @SaPcCheckPermission("/sysDicType/add")
    @PostMapping("/sysDicType/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO){
        sysDicTypeService.add(sysDicTypeAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改系统字典类别
     * @param sysDicTypeAddOrEditDTO 修改字典类别dto
     * @return
     */
    @ApiOperation(value = "修改系统字典类别")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改系统字典类别")
    @SaPcCheckPermission("/sysDicType/edit")
    @PostMapping("/sysDicType/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysDicTypeAddOrEditDTO sysDicTypeAddOrEditDTO){
        sysDicTypeService.edit(sysDicTypeAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除系统字典类别
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "删除系统字典类别")
    @ApiOperationSupport(order = 4)
    @CommonLog("删除系统字典类别")
    @SaPcCheckPermission("/sysDicType/delete")
    @PostMapping("/sysDicType/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        sysDicTypeService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
