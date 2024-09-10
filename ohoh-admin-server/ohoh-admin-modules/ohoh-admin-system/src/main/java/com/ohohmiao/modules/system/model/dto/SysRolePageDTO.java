package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色分页查询条件
 *
 * @author ohohmiao
 * @date 2023-04-14 17:19
 */
@ApiModel("系统角色分页查询条件")
@Getter
@Setter
public class SysRolePageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "组织id")
    private String orgId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
