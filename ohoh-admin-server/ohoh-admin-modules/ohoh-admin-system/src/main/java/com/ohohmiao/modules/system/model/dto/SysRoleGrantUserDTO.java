package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 给系统角色分配用户
 *
 * @author ohohmiao
 * @date 2023/4/19 21:44
 */
@ApiModel("给系统角色分配用户")
@Getter
@Setter
public class SysRoleGrantUserDTO {

    @ApiModelProperty(value = "角色id", required = true)
    @NotBlank(message = "角色id不能为空")
    private String roleId;

    @ApiModelProperty(value = "用户id")
    private List<String> userIdList;

}
