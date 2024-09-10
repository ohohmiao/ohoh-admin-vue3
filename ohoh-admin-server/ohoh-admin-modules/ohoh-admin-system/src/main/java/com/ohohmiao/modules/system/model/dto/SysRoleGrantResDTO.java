package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 系统角色授权资源
 *
 * @author ohohmiao
 * @date 2023/4/18 21:53
 */
@ApiModel("系统角色授权资源")
@Getter
@Setter
public class SysRoleGrantResDTO {

    @ApiModelProperty(value = "角色id", required = true)
    @NotBlank(message = "角色id不能为空")
    private String roleId;

    @ApiModelProperty(value = "资源id", required = true)
    @NotEmpty(message = "资源id不能为空")
    private List<String> resIdList;

}
