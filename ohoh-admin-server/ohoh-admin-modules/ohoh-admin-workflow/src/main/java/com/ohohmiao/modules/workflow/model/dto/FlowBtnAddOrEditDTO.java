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
 * 流程按钮新增或编辑
 *
 * @author ohohmiao
 * @date 2025-06-07 16:38
 */
@ApiModel("流程按钮新增或编辑")
@Getter
@Setter
public class FlowBtnAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String btnId;

    @ApiModelProperty(value = "按钮文本", required = true)
    @NotBlank(message = "按钮文本不能为空")
    private String btnText;

    @ApiModelProperty(value = "按钮颜色", required = true)
    @NotBlank(message = "按钮颜色不能为空")
    private String btnColor;

    @ApiModelProperty(value = "执行方法", required = true)
    @NotBlank(message = "执行方法不能为空")
    private String btnFun;

}
