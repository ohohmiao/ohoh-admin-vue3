package com.ohohmiao.modules.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowInfoQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNextNodeQueryDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowSubmitDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;
import com.ohohmiao.modules.workflow.model.vo.FlowTaskNodeVO;
import com.ohohmiao.modules.workflow.service.FlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程核心controller
 *
 * @author ohohmiao
 * @date 2025-06-08 10:50
 */
@Api(tags = "流程核心控制器")
@ApiSupport(order = 9)
@RestController
public class FlowController {

    @Resource
    private FlowService flowService;

    /**
     * 获取流程核心信息
     * @param queryDTO
     * @return
     */
    @ApiOperation(value = "获取流程核心信息")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflow/getFlowInfo")
    @PostMapping("/workflow/getFlowInfo")
    public CommonResp<FlowInfoVO> getFlowInfo(@RequestBody @Validated FlowInfoQueryDTO queryDTO){
        return CommonResp.data(flowService.getFlowInfo(queryDTO, true));
    }

    /**
     * 查询流程下一环节信息
     * @param queryDTO
     * @return
     */
    @ApiOperation(value = "查询流程下一环节信息")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/workflow/getNextNodeList")
    @PostMapping("/workflow/getNextNodeList")
    public CommonResp<List<FlowTaskNodeVO>> getNextNodeList(@RequestBody @Validated FlowNextNodeQueryDTO queryDTO){
        return CommonResp.data(flowService.getNextNodeList(queryDTO));
    }

    /**
     * 提交流程
     * @param submitDTO
     * @return
     */
    @ApiOperation(value = "提交流程")
    @ApiOperationSupport(order = 3)
    @CommonLog("提交流程")
    @SaPcCheckPermission("/workflow/submit")
    @PostMapping("/workflow/submit")
    public CommonResp<String> submit(@RequestBody @Validated FlowSubmitDTO submitDTO){
        flowService.doSubmit(submitDTO);
        return CommonResp.success("操作成功");
    }

}
