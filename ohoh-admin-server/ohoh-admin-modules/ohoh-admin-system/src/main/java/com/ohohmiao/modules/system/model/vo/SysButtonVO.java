package com.ohohmiao.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 按钮
 *
 * @author ohohmiao
 * @date 2023-08-23 10:44
 */
@ApiModel("按钮")
@Getter
@Setter
public class SysButtonVO extends SysMenuVO {

    @ApiModelProperty(value = "按钮名称")
    private String menuName;

    @ApiModelProperty(value = "按钮编码")
    private String menuCode;

}
