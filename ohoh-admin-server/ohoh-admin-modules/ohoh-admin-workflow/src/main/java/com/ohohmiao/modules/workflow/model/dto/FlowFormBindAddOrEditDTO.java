package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 流程表单绑定新增或编辑
 *
 * @author ohohmiao
 * @date 2025-06-07 23:04
 */
@ApiModel("流程表单绑定新增或编辑")
@Getter
@Setter
public class FlowFormBindAddOrEditDTO {

    @ApiModelProperty(value = "流程编码", required = true)
    @NotBlank(message = "流程编码不能为空")
    private String defCode;

    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空")
    private Integer defVersion;

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String formId;

    @ApiModelProperty(value = "表单名称", required = true)
    @NotBlank(message = "表单名称不能为空")
    private String formName;

    @ApiModelProperty(value = "表单路径", required = true)
    @NotBlank(message = "表单路径不能为空")
    private String formPath;

    @ApiModelProperty(value = "绑定环节id", required = true)
    @NotEmpty(message = "绑定环节id不能为空")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "绑定环节名称", required = true)
    @NotEmpty(message = "绑定环节名称不能为空")
    private List<String> bindNodeNames;

}
