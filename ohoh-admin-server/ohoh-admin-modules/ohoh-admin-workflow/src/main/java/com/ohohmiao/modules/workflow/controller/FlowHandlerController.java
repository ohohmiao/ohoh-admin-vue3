package com.ohohmiao.modules.workflow.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHandlerPageDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowHandlerVO;
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

    /**
     * 获取单个环节办理人配置
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "获取单个环节办理人配置")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/workflowHandler/get")
    @PostMapping("/workflowHandler/get")
    public CommonResp<FlowHandlerVO> get(@RequestBody @Validated CommonIdDTO idDTO){
        FlowHandlerVO FlowHandlerVO = flowHandlerService.get(idDTO.getId());
        if(ObjectUtil.isNotNull(FlowHandlerVO)){
            return CommonResp.data(FlowHandlerVO);
        }else{
            return CommonResp.error("不存在的记录");
        }
    }

    /**
     * 新增环节办理人配置
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增环节办理人配置")
    @ApiOperationSupport(order = 3)
    @CommonLog("新增环节办理人配置")
    @SaPcCheckPermission("/workflowHandler/add")
    @PostMapping("/workflowHandler/add")
    public CommonResp<String> add(@RequestBody @Validated(CommonAddGroup.class)FlowHandlerAddOrEditDTO addOrEditDTO){
        flowHandlerService.add(addOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改环节办理人配置
     * @param addOrEditDTO
     * @return
     */
    @ApiOperation(value = "修改环节办理人配置")
    @ApiOperationSupport(order = 4)
    @CommonLog("修改环节办理人配置")
    @SaPcCheckPermission("/workflowHandler/edit")
    @PostMapping("/workflowHandler/edit")
    public CommonResp<String> edit(@RequestBody @Validated(CommonEditGroup.class)FlowHandlerAddOrEditDTO addOrEditDTO){
        flowHandlerService.edit(addOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 批量删除环节办理人配置
     * @param idListDTO
     * @return
     */
    @ApiOperation(value = "批量删除环节办理人配置")
    @ApiOperationSupport(order = 5)
    @CommonLog("批量删除环节办理人配置")
    @SaPcCheckPermission("/workflowHandler/multiDelete")
    @PostMapping("/workflowHandler/multiDelete")
    public CommonResp<String> multiDelete(@RequestBody @Validated CommonIdListDTO idListDTO){
        flowHandlerService.multiDelete(idListDTO);
        return CommonResp.success("删除成功");
    }

}
