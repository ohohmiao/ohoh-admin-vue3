package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 根据流程类别获取流程列表
 *
 * @author ohohmiao
 * @date 2025-06-07 09:46
 */
@ApiModel("根据流程类别获取流程列表")
@Getter
@Setter
public class FlowDefListByTypeDTO {

    @ApiModelProperty(value = "类别id", required = true)
    @NotBlank(message = "类别id不能为空")
    private String deftypeId;

}
