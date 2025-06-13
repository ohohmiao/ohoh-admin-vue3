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
import com.ohohmiao.modules.workflow.model.dto.FlowDefAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowDefPageDTO;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import com.ohohmiao.modules.workflow.service.FlowDefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 流程定义controller
 *
 * @author ohohmiao
 * @date 2024-12-02 11:16
 */
@Api(tags = "流程定义")
@ApiSupport(order = 2)
@RestController
public class FlowDefController {

    @Resource
    private FlowDefService flowDefService;

    /**
     * 获取流程定义分页列表
     * @param flowDefPageDTO 流程定义分页dto
     * @return
     */
    @ApiOperation(value = "获取流程定义分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/workflowDef/page")
    @PostMapping("/workflowDef/page")
    public CommonResp<Page<FlowDefVO>> page(@RequestBody FlowDefPageDTO flowDefPageDTO){
        return CommonResp.data(flowDefService.listByPage(flowDefPageDTO));
    }

    /**
     * 获取单个流程定义
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "获取单个流程定义")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/workflowDef/get")
    @PostMapping("/workflowDef/get")
    public CommonResp<FlowDefVO> get(@RequestBody @Validated CommonIdDTO idDTO){
        FlowDefVO flowDefVO = flowDefService.get(idDTO.getId());
        if(ObjectUtil.isNotNull(flowDefVO)){
            return CommonResp.data(flowDefVO);
        }else{
            return CommonResp.error("不存在的记录");
        }
    }

    /**
     * 新增流程定义
     * @param flowDefAddOrEditDTO
     * @return
     */
    @ApiOperation(value = "新增流程定义")
    @ApiOperationSupport(order = 3)
    @CommonLog("新增流程定义")
    @SaPcCheckPermission("/workflowDef/add")
    @PostMapping("/workflowDef/add")
    public CommonResp<String> add(@RequestBody @Validated(CommonAddGroup.class) FlowDefAddOrEditDTO flowDefAddOrEditDTO){
        flowDefService.add(flowDefAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改流程定义
     * @param flowDefAddOrEditDTO
     * @return
     */
    @ApiOperation(value = "修改流程定义")
    @ApiOperationSupport(order = 4)
    @CommonLog("修改流程定义")
    @SaPcCheckPermission("/workflowDef/edit")
    @PostMapping("/workflowDef/edit")
    public CommonResp<String> edit(@RequestBody @Validated(CommonEditGroup.class) FlowDefAddOrEditDTO flowDefAddOrEditDTO){
        flowDefService.edit(flowDefAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除流程定义
     * @param idDTO
     * @return
     */
    @ApiOperation(value = "删除流程定义")
    @ApiOperationSupport(order = 5)
    @CommonLog("删除流程定义")
    @SaPcCheckPermission("/workflowDef/delete")
    @PostMapping("/workflowDef/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdDTO idDTO){
        flowDefService.delete(idDTO);
        return CommonResp.success("删除成功");
    }

    /**
     * 列出被@FlowEntity标注的实体类名
     * @return
     */
    @ApiOperation(value = "列出被@FlowEntity标注的实体类名")
    @ApiOperationSupport(order = 6)
    @SaPcCheckPermission("/workflowDef/listFlowEnitityClassNames")
    @PostMapping("/workflowDef/listFlowEnitityClassNames")
    public CommonResp<Set<String>> listFlowEnitityClassNames(){
        return CommonResp.data(flowDefService.listFlowEnitityClassNames());
    }

}
