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
 * 组织机构新增或编辑
 *
 * @author ohohmiao
 * @date 2023/4/13 21:21
 */
@ApiModel("组织机构新增或编辑")
@Getter
@Setter
public class SysOrgAddOrEditDTO {

    @ApiModelProperty(value = "id，编辑时必传")
    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String orgId;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String orgName;

    @ApiModelProperty(value = "简称")
    private String orgShortname;

    @ApiModelProperty(value = "编码")
    private String orgCode;

    @ApiModelProperty(value = "组织级别", required = true)
    @NotNull(message = "组织级别不能为空")
    private Integer orgLevel;

    @ApiModelProperty(value = "父节点id", required = true)
    @NotBlank(message = "父节点id不能为空")
    private String parentId;

    @ApiModelProperty(value = "排序，编辑时必传")
    @NotNull(message = "排序不能为空", groups = {CommonEditGroup.class})
    private Integer treeSort;

}
