package com.ohohmiao.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统角色属性
 *
 * @author ohohmiao
 * @date 2023-04-13 16:32
 */
@ApiModel("系统角色属性")
@Getter
@Setter
public class SysRolePropVO {

    @ApiModelProperty(value = "属性id")
    private String propId;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "关联业务表")
    private String propTablename;

    @ApiModelProperty(value = "关联业务记录id")
    private String propRecordid;

}
