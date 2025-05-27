package com.ohohmiao.framework.common.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 关联资源
 *
 * @author ohohmiao
 * @date 2025-05-27 16:57
 */
@ApiModel("关联资源")
@Getter
@Setter
public class CommonReferRes {

    @ApiModelProperty(value = "资源类别")
    private String referRestype;

    @ApiModelProperty(value = "资源id")
    private String referResid;

    @ApiModelProperty(value = "资源名称")
    private String referResname;

}
