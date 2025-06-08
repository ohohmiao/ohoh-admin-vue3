package com.ohohmiao.modules.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowFormBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowFormBindPageDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowFormBindVO;
import com.ohohmiao.modules.workflow.service.FlowFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 流程表单controller
 *
 * @author ohohmiao
 * @date 2025-06-07 14:56
 */
@Api(tags = "流程表单")
@ApiSupport(order = 7)
@RestController
public class FlowFormController {

    @Resource
    private FlowFormService flowFormService;

    /**
     * 获取流程表单绑定分页列表
     * @param pageDTO
     * @return
     */
    @ApiOperation(value = "获取流程表单绑定分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowForm/listBindByPage")
    @PostMapping("/workflowForm/listBindByPage")
    public CommonResp<Page<FlowFormBindVO>> listBindByPage(@RequestBody @Validated FlowFormBindPageDTO pageDTO){
        return CommonResp.data(flowFormService.listBindByPage(pageDTO));
    }

    /**
     * 新增流程表单绑定
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增流程表单绑定")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增流程表单绑定")
    @SaPcCheckPermission("/workflowForm/addBind")
    @PostMapping("/workflowForm/addBind")
    public CommonResp<String> addBind(@RequestBody @Validated FlowFormBindAddOrEditDTO addOrEditDTO){
        flowFormService.addBind(addOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改流程表单绑定
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "修改流程表单绑定")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改流程表单绑定")
    @SaPcCheckPermission("/workflowForm/editBind")
    @PostMapping("/workflowForm/editBind")
    public CommonResp<String> editBind(@RequestBody @Validated FlowFormBindAddOrEditDTO addOrEditDTO){
        flowFormService.editBind(addOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 批量删除流程表单绑定
     * @param idListDTO
     * @return
     */
    @ApiOperation(value = "批量删除流程表单绑定")
    @ApiOperationSupport(order = 4)
    @CommonLog("批量删除流程表单绑定")
    @SaPcCheckPermission("/workflowForm/multiDeleteBind")
    @PostMapping("/workflowForm/multiDeleteBind")
    public CommonResp<String> multiDeleteBind(@RequestBody @Validated CommonIdListDTO idListDTO){
        flowFormService.multiDeleteBind(idListDTO);
        return CommonResp.success("删除成功");
    }

}
