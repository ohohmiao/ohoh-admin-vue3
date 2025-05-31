package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程环节属性获取
 *
 * @author ohohmiao
 * @date 2025-05-30 16:56
 */
@ApiModel("流程环节属性获取")
@Getter
@Setter
public class FlowNodeGetDTO {

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "环节id", required = true)
    @NotBlank(message = "环节id不能为空")
    private String nodeId;

}
