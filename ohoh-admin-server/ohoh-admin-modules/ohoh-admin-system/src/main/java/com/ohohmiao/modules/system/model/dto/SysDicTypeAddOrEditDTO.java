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
 * 系统字典类别新增或编辑
 *
 * @author ohohmiao
 * @date 2023-05-25 14:34
 */
@ApiModel("系统字典类别新增或编辑")
@Getter
@Setter
public class SysDicTypeAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String dictypeId;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String dictypeName;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank(message = "编码不能为空")
    private String dictypeCode;

    @ApiModelProperty(value = "父节点id", required = true)
    @NotBlank(message = "父节点id不能为空")
    private String parentId;

    @ApiModelProperty(value = "排序，编辑时必传")
    @NotNull(message = "排序不能为空", groups = {CommonEditGroup.class})
    private Integer treeSort;

    @ApiModelProperty(value = "备注")
    private String dictypeRemark;

}
