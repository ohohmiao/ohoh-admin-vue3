package com.ohohmiao.modules.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeGetDTO;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskMultiAssignWeight;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;
import com.ohohmiao.modules.workflow.service.FlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程环节controller
 *
 * @author ohohmiao
 * @date 2025-05-30 16:55
 */
@Api(tags = "流程环节")
@ApiSupport(order = 5)
@RestController
public class FlowNodeController {

    @Resource
    private FlowNodeService flowNodeService;

    /**
     * 获取流程某环节属性
     * @param getDTO
     * @return
     */
    @ApiOperation(value = "获取流程某环节属性")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowNode/get")
    @PostMapping("/workflowNode/get")
    public CommonResp<FlowNodeVO> get(@RequestBody @Validated FlowNodeGetDTO getDTO){
        return CommonResp.data(flowNodeService.get(
                getDTO.getDefCode(), getDTO.getDefVersion(), getDTO.getNodeId()));
    }

    /**
     * 新增或修改流程某环节属性
     * @param flowNodeAddOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增或修改流程某环节属性")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增或修改流程某环节属性")
    @SaPcCheckPermission("/workflowNode/addOrEdit")
    @PostMapping("/workflowNode/addOrEdit")
    public CommonResp<String> addOrEdit(@RequestBody @Validated FlowNodeAddOrEditDTO flowNodeAddOrEditDTO){
        flowNodeService.saveOrUpdate(flowNodeAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 获取流程某环节多人决策权重配置列表
     * @param listDTO
     * @return
     */
    @ApiOperation(value = "获取流程某环节多人决策权重配置列表")
    @ApiOperationSupport(order = 3)
    @SaPcCheckPermission("/workflowNode/listMultiAssignWeight")
    @PostMapping("/workflowNode/listMultiAssignWeight")
    public CommonResp<List<FlowTaskMultiAssignWeight>> listMultiAssignWeight(@RequestBody @Validated FlowNodeGetDTO listDTO){
        return CommonResp.data(flowNodeService.listMultiAssignWeight(listDTO));
    }

}
