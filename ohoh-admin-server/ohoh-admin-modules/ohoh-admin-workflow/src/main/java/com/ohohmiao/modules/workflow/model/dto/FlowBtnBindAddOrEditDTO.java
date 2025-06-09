package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程按钮绑定新增或编辑
 *
 * @author ohohmiao
 * @date 2025-06-09 10:37
 */
@ApiModel("流程按钮绑定新增或编辑")
@Getter
@Setter
public class FlowBtnBindAddOrEditDTO {

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "流程按钮")
    @NotBlank(message = "流程按钮不能为空")
    private String btnId;

    @ApiModelProperty(value = "绑定环节id", required = true)
    @NotEmpty(message = "绑定环节id不能为空")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "绑定环节名称", required = true)
    @NotEmpty(message = "绑定环节名称不能为空")
    private List<String> bindNodeNames;

}
