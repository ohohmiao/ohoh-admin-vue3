package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 审批过程日志列表查询条件
 *
 * @author ohohmiao
 * @date 2026-02-24 14:11
 */
@ApiModel("审批过程日志列表查询条件")
@Getter
@Setter
public class FlowTaskLogListDTO {

    @ApiModelProperty(value = "流程实例id")
    @NotBlank(message = "流程实例id不能为空")
    private String processId;

}
