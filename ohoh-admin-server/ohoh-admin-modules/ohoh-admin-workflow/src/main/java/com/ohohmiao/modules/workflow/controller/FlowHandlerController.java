package com.ohohmiao.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import com.ohohmiao.modules.workflow.service.FlowHandlerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 流程环节办理人配置controller
 *
 * @author ohohmiao
 * @date 2025-06-01 11:03
 */
@Api(tags = "流程环节办理人配置")
@ApiSupport(order = 6)
@RestController
public class FlowHandlerController {

    @Resource
    private FlowHandlerService flowHandlerService;

    /**
     * 获取流程某环节办理人配置分页列表
     * @param pageDTO
     * @return
     */
    @ApiOperation(value = "获取流程某环节办理人配置分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowHandler/page")
    @PostMapping("/workflowHandler/page")
    public CommonResp<Page<FlowHandler>> page(@RequestBody @Validated FlowHandlerPageDTO pageDTO){
        return CommonResp.data(flowHandlerService.listByPage(pageDTO));
    }

}
