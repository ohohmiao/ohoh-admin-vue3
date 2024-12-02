package com.ohohmiao.modules.workflow.controller;

import cn.hutool.core.lang.tree.Tree;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.WorkflowDefTypeAddOrEditDTO;
import com.ohohmiao.modules.workflow.service.WorkflowDefTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程定义类别controller
 *
 * @author ohohmiao
 * @date 2024-12-01 21:54
 */
@Api(tags = "流程定义类别")
@ApiSupport(order = 1)
@RestController
public class WorkflowDefTypeController {

    @Resource
    private WorkflowDefTypeService workflowDefTypeService;

    /**
     * 获取流程定义类别树
     * @return
     */
    @ApiOperation(value = "获取流程定义类别树")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowDefType/tree")
    @PostMapping("/workflowDefType/tree")
    public CommonResp<List<Tree<String>>> tree(){
        return CommonResp.data(workflowDefTypeService.getTreeData());
    }

    /**
     * 新增流程定义类别
     * @param workflowDefTypeAddOrEditDTO 新增流程定义类别dto
     * @return
     */
    @ApiOperation(value = "新增流程定义类别")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增流程定义类别")
    @SaPcCheckPermission("/workflowDefType/add")
    @PostMapping("/workflowDefType/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO){
        workflowDefTypeService.add(workflowDefTypeAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改流程定义类别
     * @param workflowDefTypeAddOrEditDTO 修改流程定义类别dto
     * @return
     */
    @ApiOperation(value = "修改流程定义类别")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改流程定义类别")
    @SaPcCheckPermission("/workflowDefType/edit")
    @PostMapping("/workflowDefType/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) WorkflowDefTypeAddOrEditDTO workflowDefTypeAddOrEditDTO){
        workflowDefTypeService.edit(workflowDefTypeAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除流程定义类别
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "删除流程定义类别")
    @ApiOperationSupport(order = 4)
    @CommonLog("删除流程定义类别")
    @SaPcCheckPermission("/workflowDefType/delete")
    @PostMapping("/workflowDefType/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        workflowDefTypeService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
