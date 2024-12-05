package com.ohohmiao.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.WorkflowDefPageDTO;
import com.ohohmiao.modules.workflow.model.vo.WorkflowDefVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程定义controller
 *
 * @author ohohmiao
 * @date 2024-12-02 11:16
 */
@Api(tags = "流程定义")
@ApiSupport(order = 2)
@RestController
public class WorkflowDefController {

    /**
     * 获取流程定义分页列表
     * @param workflowDefPageDTO 流程定义分页dto
     * @return
     */
    @ApiOperation(value = "获取流程定义分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowDef/page")
    @PostMapping("/workflowDef/page")
    public CommonResp<Page<WorkflowDefVO>> page(@RequestBody WorkflowDefPageDTO workflowDefPageDTO){
        return CommonResp.data(null);
    }

}
