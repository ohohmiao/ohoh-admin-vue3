package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 系统角色
 *
 * @author ohohmiao
 * @date 2023-04-06 11:32
 */
@ApiModel("系统角色")
@Getter
@Setter
public class SysRoleVO extends CommonVO {

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "机构id")
    private String orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "是否公共角色")
    private Integer publicroleFlag;

    @ApiModelProperty(value = "数据范围类别")
    private Integer datascopeType;

    @ApiModelProperty(value = "数据范围id集合")
    private Set<String> datascopeOrgIds;

    @ApiModelProperty(value = "角色备注")
    private String roleRemark;

    @ApiModelProperty(value = "角色排序")
    private Integer roleSort;

}
