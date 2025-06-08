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
import com.ohohmiao.modules.workflow.model.dto.FlowBtnAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowBtnPageDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnVO;
import com.ohohmiao.modules.workflow.service.FlowBtnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 流程按钮controller
 *
 * @author ohohmiao
 * @date 2025-06-08 09:57
 */
@Api(tags = "流程按钮")
@ApiSupport(order = 8)
@RestController
public class FlowBtnController {

    @Resource
    private FlowBtnService flowBtnService;

    /**
     * 获取流程按钮分页列表
     * @param pageDTO
     * @return
     */
    @ApiOperation(value = "获取流程按钮分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowBtn/page")
    @PostMapping("/workflowBtn/page")
    public CommonResp<Page<FlowBtnVO>> page(@RequestBody FlowBtnPageDTO pageDTO){
        return CommonResp.data(flowBtnService.listByPage(pageDTO));
    }

    /**
     * 获取单个流程按钮
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "获取单个流程按钮")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/workflowBtn/get")
    @PostMapping("/workflowBtn/get")
    public CommonResp<FlowBtnVO> get(@RequestBody @Validated CommonIdDTO idDTO){
        FlowBtnVO flowBtnVO = flowBtnService.get(idDTO.getId());
        if(ObjectUtil.isNotNull(flowBtnVO)){
            return CommonResp.data(flowBtnVO);
        }else{
            return CommonResp.error("不存在的记录");
        }
    }

    /**
     * 新增流程按钮
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增流程按钮")
    @ApiOperationSupport(order = 3)
    @CommonLog("新增流程按钮")
    @SaPcCheckPermission("/workflowBtn/add")
    @PostMapping("/workflowBtn/add")
    public CommonResp<String> add(@RequestBody @Validated(CommonAddGroup.class) FlowBtnAddOrEditDTO addOrEditDTO){
        flowBtnService.add(addOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改流程按钮
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "修改流程按钮")
    @ApiOperationSupport(order = 4)
    @CommonLog("修改流程按钮")
    @SaPcCheckPermission("/workflowBtn/edit")
    @PostMapping("/workflowBtn/edit")
    public CommonResp<String> edit(@RequestBody @Validated(CommonEditGroup.class) FlowBtnAddOrEditDTO addOrEditDTO){
        flowBtnService.edit(addOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除流程按钮
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "删除流程按钮")
    @ApiOperationSupport(order = 5)
    @CommonLog("删除流程按钮")
    @SaPcCheckPermission("/workflowBtn/delete")
    @PostMapping("/workflowBtn/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        flowBtnService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

}
