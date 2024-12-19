package com.ohohmiao.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户属性
 *
 * @author ohohmiao
 * @date 2023-04-13 16:32
 */
@ApiModel("FullCalendar事件")
@Getter
@Setter
public class SysUserPropVO {

    @ApiModelProperty(value = "属性id")
    private String propId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "关联业务表")
    private String propTablename;

    @ApiModelProperty(value = "关联业务记录id")
    private String propRecordid;

    @ApiModelProperty(value = "关联业务记录名称")
    private String propRecordname;

    @ApiModelProperty(value = "排序")
    private Integer propSort;

    @ApiModelProperty(value = "扩展关联id")
    private String propExtendid;

    @ApiModelProperty(value = "是否默认")
    private Integer defaultFlag;

}
