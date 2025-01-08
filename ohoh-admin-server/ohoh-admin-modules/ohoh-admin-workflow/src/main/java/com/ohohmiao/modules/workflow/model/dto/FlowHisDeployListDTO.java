package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 流程定义历史版本列表查询条件
 *
 * @author ohohmiao
 * @date 2025-01-08 15:07
 */
@ApiModel("流程定义历史版本列表查询条件")
@Getter
@Setter
public class FlowHisDeployListDTO {

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "流程版本")
    private Integer defVersion;

}
