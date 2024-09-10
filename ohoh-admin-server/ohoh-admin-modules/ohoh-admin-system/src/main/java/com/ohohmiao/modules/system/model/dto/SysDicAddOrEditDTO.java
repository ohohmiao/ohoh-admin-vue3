package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 系统字典新增或编辑
 *
 * @author ohohmiao
 * @date 2023-05-25 14:40
 */
@ApiModel("系统字典新增或编辑")
@Getter
@Setter
public class SysDicAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String dicId;

    @ApiModelProperty(value = "类别id", required = true)
    @NotBlank(message = "类别id不能为空")
    private String dictypeId;

    @ApiModelProperty(value = "字典名称", required = true)
    @NotBlank(message = "字典名称不能为空")
    private String dicName;

    @ApiModelProperty(value = "字典值", required = true)
    @NotBlank(message = "字典值不能为空")
    private String dicCode;

    @ApiModelProperty(value = "排序，编辑时必传")
    @NotNull(message = "排序不能为空", groups = {CommonEditGroup.class})
    private Integer dicSort;

    @ApiModelProperty(value = "扩展字段")
    private String extendField;

}
