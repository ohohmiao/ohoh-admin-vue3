package com.ohohmiao.modules.workflow.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployGetDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowHisDeployService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @SaPcCheckPermission("/worflowHisDeploy/get")
    @PostMapping("/worflowHisDeploy/get")
    public CommonResp<FlowDefVO> targetVersion(@RequestBody @Validated FlowHisDeployGetDTO flowHisDeployGetDTO){
        FlowDefVO flowDefVO = flowHisDeployService.get(
                flowHisDeployGetDTO.getDefCode(), flowHisDeployGetDTO.getDefVersion());
        if(ObjectUtil.isNotNull(flowDefVO)){
            return CommonResp.data(flowDefVO);
        }else{
            return CommonResp.error("不存在的记录");
        }
    }

}
