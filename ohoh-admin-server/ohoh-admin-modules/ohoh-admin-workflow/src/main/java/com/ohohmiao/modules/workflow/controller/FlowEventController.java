package com.ohohmiao.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowEventAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowEventPageDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowEventVO;
import com.ohohmiao.modules.workflow.service.FlowEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 流程事件controller
 *
 * @author ohohmiao
 * @date 2025-01-16 13:24
 */
@Api(tags = "流程事件")
@ApiSupport(order = 4)
@RestController
public class FlowEventController {

    @Resource
    private FlowEventService flowEventService;

    /**
     * 获取流程事件分页列表
     * @param flowEventPageDTO
     * @return
     */
    @ApiOperation(value = "获取流程事件分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowEvent/page")
    @PostMapping("/workflowEvent/page")
    public CommonResp<Page<FlowEventVO>> page(@RequestBody @Validated FlowEventPageDTO flowEventPageDTO){
        return CommonResp.data(flowEventService.listByPage(flowEventPageDTO));
    }

    /**
     * 新增流程事件
     * @param flowEventAddOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增流程事件")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增流程事件")
    @SaPcCheckPermission("/workflowEvent/add")
    @PostMapping("/workflowEvent/add")
    public CommonResp<String> add(@RequestBody @Validated(CommonAddGroup.class) FlowEventAddOrEditDTO flowEventAddOrEditDTO){
        flowEventService.add(flowEventAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改流程事件
     * @param flowEventAddOrEditDTO
     * @return
     */
    @ApiOperation(value = "修改流程事件")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改流程事件")
    @SaPcCheckPermission("/workflowEvent/edit")
    @PostMapping("/workflowEvent/edit")
    public CommonResp<String> edit(@RequestBody @Validated(CommonEditGroup.class) FlowEventAddOrEditDTO flowEventAddOrEditDTO){
        flowEventService.edit(flowEventAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除流程事件
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "删除流程事件")
    @ApiOperationSupport(order = 4)
    @CommonLog("删除流程事件")
    @SaPcCheckPermission("/workflowEvent/delete")
    @PostMapping("/workflowEvent/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        flowEventService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
