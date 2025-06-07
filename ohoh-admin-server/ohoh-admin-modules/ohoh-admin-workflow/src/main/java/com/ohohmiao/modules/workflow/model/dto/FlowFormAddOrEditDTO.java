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
 * 流程表单新增或编辑
 *
 * @author ohohmiao
 * @date 2025-06-07 16:38
 */
@ApiModel("流程表单新增或编辑")
@Getter
@Setter
public class FlowFormAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String formId;

    @ApiModelProperty(value = "类别id", required = true)
    @NotBlank(message = "类别id不能为空")
    private String deftypeId;

    @ApiModelProperty(value = "表单名称", required = true)
    @NotBlank(message = " 表单名称不能为空")
    private String formName;

    @ApiModelProperty(value = "表单路径", required = true)
    @NotBlank(message = " 表单路径不能为空")
    private String formPath;

}
