package com.ohohmiao.modules.workflow.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowFormAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowFormPageDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowFormVO;
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
     * 获取流程表单分页列表
     * @param pageDTO
     * @return
     */
    @ApiOperation(value = "获取流程表单分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowForm/page")
    @PostMapping("/workflowForm/page")
    public CommonResp<Page<FlowFormVO>> page(@RequestBody FlowFormPageDTO pageDTO){
        return CommonResp.data(flowFormService.listByPage(pageDTO));
    }

    /**
     * 获取单个流程表单
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "获取单个流程表单")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/workflowForm/get")
    @PostMapping("/workflowForm/get")
    public CommonResp<FlowFormVO> get(@RequestBody @Validated CommonIdDTO idDTO){
        FlowFormVO flowFormVO = flowFormService.get(idDTO.getId());
        if(ObjectUtil.isNotNull(flowFormVO)){
            return CommonResp.data(flowFormVO);
        }else{
            return CommonResp.error("不存在的记录");
        }
    }

    /**
     * 新增流程表单
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增流程表单")
    @ApiOperationSupport(order = 3)
    @CommonLog("新增流程表单")
    @SaPcCheckPermission("/workflowForm/add")
    @PostMapping("/workflowForm/add")
    public CommonResp<String> add(@RequestBody @Validated(CommonAddGroup.class) FlowFormAddOrEditDTO addOrEditDTO){
        flowFormService.add(addOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改流程表单
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "修改流程表单")
    @ApiOperationSupport(order = 4)
    @CommonLog("修改流程表单")
    @SaPcCheckPermission("/workflowForm/edit")
    @PostMapping("/workflowForm/edit")
    public CommonResp<String> edit(@RequestBody @Validated(CommonEditGroup.class) FlowFormAddOrEditDTO addOrEditDTO){
        flowFormService.edit(addOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除流程表单
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "删除流程表单")
    @ApiOperationSupport(order = 5)
    @CommonLog("删除流程表单")
    @SaPcCheckPermission("/workflowForm/delete")
    @PostMapping("/workflowForm/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        flowFormService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
