package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 给系统用户授权数据范围
 *
 * @author ohohmiao
 * @date 2023-05-23 14:28
 */
@ApiModel("给系统用户授权数据范围")
@Getter
@Setter
public class SysUserGrantDataDTO {

    @ApiModelProperty(value = "用户id", required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "数据范围")
    private List<String> datascopeOrgIds;

}
