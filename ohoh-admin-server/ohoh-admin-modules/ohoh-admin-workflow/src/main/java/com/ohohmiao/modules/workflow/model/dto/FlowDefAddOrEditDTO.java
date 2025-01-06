package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

/**
 * 流程定义新增或编辑
 *
 * @author ohohmiao
 * @date 2024-12-08 20:50
 */
@ApiModel("流程定义新增或编辑")
@Getter
@Setter
public class FlowDefAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String defId;

    @ApiModelProperty(value = "类别id", required = true)
    @NotBlank(message = "类别id不能为空")
    private String deftypeId;

    @ApiModelProperty(value = "流程名称", required = true)
    @NotBlank(message = "流程名称不能为空")
    private String defName;

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "流程定义xml", required = true)
    @NotBlank(message = "流程定义xml不能为空")
    private String defXml;

    @ApiModelProperty(value = "流程定义json", required = true)
    @NotBlank(message = "流程定义json不能为空")
    private String defJson;

    @ApiModelProperty(value = "流程定义svg", required = true)
    @NotBlank(message = "流程定义svg不能为空")
    private String defSvg;

}
