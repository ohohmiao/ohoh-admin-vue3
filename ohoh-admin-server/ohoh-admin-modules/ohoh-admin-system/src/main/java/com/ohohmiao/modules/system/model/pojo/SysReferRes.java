package com.ohohmiao.modules.system.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 关联系统资源
 *
 * @author ohohmiao
 * @date 2025-06-03 17:00
 */
@ApiModel("关联系统资源")
@Getter
@Setter
public class SysReferRes {

    @ApiModelProperty(value = "资源类别")
    private String referRestype;

    @ApiModelProperty(value = "资源id")
    private String referResid;

    @ApiModelProperty(value = "资源名称")
    private String referResname;

}
