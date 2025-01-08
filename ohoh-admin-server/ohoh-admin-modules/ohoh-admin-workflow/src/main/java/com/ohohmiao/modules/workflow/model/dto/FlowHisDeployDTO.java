package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程定义历史部署修改
 *
 * @author ohohmiao
 * @date 2025-01-08 10:31
 */
@ApiModel("流程定义历史部署修改")
@Getter
@Setter
public class FlowHisDeployDTO {

    @ApiModelProperty(value = "类别id", required = true)
    @NotBlank(message = "类别id不能为空")
    private String deftypeId;

    @ApiModelProperty(value = "流程名称", required = true)
    @NotBlank(message = "流程名称不能为空")
    private String defName;

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空")
    private Integer defSort;

}
