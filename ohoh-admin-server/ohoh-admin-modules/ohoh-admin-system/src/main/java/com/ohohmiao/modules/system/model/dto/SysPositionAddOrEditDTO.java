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
 * 岗位新增或编辑
 *
 * @author ohohmiao
 * @date 2023/7/11 21:55
 */
@ApiModel("岗位新增或编辑")
@Getter
@Setter
public class SysPositionAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String positionId;

    @ApiModelProperty(value = "岗位名称", required = true)
    @NotBlank(message = "岗位名称不能为空")
    private String positionName;

    @ApiModelProperty(value = "岗位编码")
    private String positionCode;

    @ApiModelProperty(value = "岗位级别", required = true)
    @NotNull(message = "岗位级别不能为空")
    private Integer positionLevel;

    @ApiModelProperty(value = "备注")
    private String positionRemark;

    @ApiModelProperty(value = "父节点id", required = true)
    @NotBlank(message = "父节点id不能为空")
    private String parentId;

    @ApiModelProperty(value = "排序，编辑时必传")
    @NotNull(message = "排序不能为空", groups = {CommonEditGroup.class})
    private Integer treeSort;

}
