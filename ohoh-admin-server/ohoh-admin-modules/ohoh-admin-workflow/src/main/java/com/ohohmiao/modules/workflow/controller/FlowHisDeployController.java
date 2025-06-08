package com.ohohmiao.modules.workflow.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowDefListByTypeDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployGetDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployListDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 流程历史部署controller
 *
 * @author ohohmiao
 * @date 2025-01-06 17:28
 */
@Api(tags = "流程历史部署")
@ApiSupport(order = 3)
@RestController
public class FlowHisDeployController {

    @Resource
    private FlowHisDeployService flowHisDeployService;

    /**
     * 获取某版本流程定义
     * @param flowHisDeployGetDTO
     * @return
     */
    @ApiOperation(value = "获取某版本流程定义")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowHisDeploy/get")
    @PostMapping("/workflowHisDeploy/get")
    public CommonResp<FlowDefVO> targetVersion(@RequestBody @Validated FlowHisDeployGetDTO flowHisDeployGetDTO){
        FlowDefVO flowDefVO = flowHisDeployService.get(
                flowHisDeployGetDTO.getDefCode(), flowHisDeployGetDTO.getDefVersion(), true);
        if(ObjectUtil.isNotNull(flowDefVO)){
            return CommonResp.data(flowDefVO);
        }else{
            return CommonResp.error("不存在的记录");
        }
    }

    /**
     * 修改流程历史定义
     * @param flowHisDeployDTO
     * @return
     */
    @ApiOperation(value = "修改流程历史定义")
    @ApiOperationSupport(order = 2)
    @CommonLog("修改流程历史定义")
    @SaPcCheckPermission("/workflowHisDeploy/edit")
    @PostMapping("/workflowHisDeploy/edit")
    public CommonResp<String> edit(@RequestBody @Validated FlowHisDeployDTO flowHisDeployDTO){
        flowHisDeployService.edit(flowHisDeployDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 获取流程历史版本列表
     * @param listDTO
     * @return
     */
    @ApiOperation(value = "获取流程历史版本列表")
    @ApiOperationSupport(order = 3)
    @SaPcCheckPermission("/workflowHisDeploy/list")
    @PostMapping("/workflowHisDeploy/list")
    public CommonResp<List<FlowDefVO>> list(@RequestBody @Validated FlowHisDeployListDTO listDTO){
        return CommonResp.data(flowHisDeployService.list(listDTO));
    }

    /**
     * 根据流程类别，获取可发起的流程列表
     * @param dto
     * @return
     */
    @ApiOperation(value = "根据流程类别，获取可发起的流程列表")
    @ApiOperationSupport(order = 4)
    @SaPcCheckPermission("/workflowHisDeploy/listInitiableByDeftype")
    @PostMapping("/workflowHisDeploy/listInitiableByDeftype")
    public CommonResp<Map<String, List<FlowDefVO>>> listInitiableByDeftype(@RequestBody @Validated FlowDefListByTypeDTO dto){
        return CommonResp.data(flowHisDeployService.listInitiableByDeftype(dto.getDeftypeId()));
    }

}
