package com.ohohmiao.modules.workflow.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeGetDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;
import com.ohohmiao.modules.workflow.service.FlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
        FlowNodeVO flowNodeVO = flowNodeService.get(getDTO.getDefCode(), getDTO.getDefVersion(), getDTO.getNodeId());
        if(ObjectUtil.isNotNull(flowNodeVO)){
            return CommonResp.data(flowNodeVO);
        }else{
            return CommonResp.data(BeanUtil.copyProperties(getDTO, FlowNodeVO.class));
        }
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

}
