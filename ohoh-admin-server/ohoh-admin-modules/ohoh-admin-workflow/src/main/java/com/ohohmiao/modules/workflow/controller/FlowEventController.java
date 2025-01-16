package com.ohohmiao.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
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
@ApiSupport(order = 5)
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

}
