package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 给岗位授权用户
 *
 * @author ohohmiao
 * @date 2023-07-12 18:10
 */
@ApiModel("给岗位授权用户")
@Getter
@Setter
public class SysPositionGrantUserDTO {

    @ApiModelProperty(value = "岗位id", required = true)
    @NotBlank(message = "岗位id不能为空")
    private String positionId;

    @ApiModelProperty(value = "用户id")
    private List<String> propIds;

}
