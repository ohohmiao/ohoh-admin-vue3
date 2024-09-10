package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统资源查询条件
 *
 * @author ohohmiao
 * @date 2023-04-12 11:54
 */
@ApiModel("系统资源查询条件")
@Getter
@Setter
public class SysResQueryDTO {

    @ApiModelProperty(value = "资源名称")
    private String resName;

    @ApiModelProperty(value = "资源类别")
    private Integer resType;

}
