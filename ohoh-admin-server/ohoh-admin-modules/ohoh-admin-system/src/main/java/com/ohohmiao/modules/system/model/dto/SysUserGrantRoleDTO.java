package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 描述 给系统用户授权角色
 *
 * @author ohohmiao
 * @date 2023-05-17 14:16
 */
@ApiModel("给系统用户授权角色")
@Getter
@Setter
public class SysUserGrantRoleDTO {

    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "角色id")
    private List<String> roleIdList;

}
