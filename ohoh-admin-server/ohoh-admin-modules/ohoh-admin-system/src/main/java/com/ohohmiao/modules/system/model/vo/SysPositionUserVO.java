package com.ohohmiao.modules.system.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位用户
 *
 * @author ohohmiao
 * @date 2023-07-12 17:01
 */
@ApiModel("岗位用户")
@Getter
@Setter
public class SysPositionUserVO {

    @ApiModelProperty(value = "岗位id")
    private String positionId;

    @ApiModelProperty(value = "岗位名称")
    private String positionName;

    @ApiModelProperty(value = "岗位树路径")
    private String positionPath;

    @ApiModelProperty(value = "组织机构id")
    private String orgId;

    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    @ApiModelProperty(value = "属性id")
    private String propId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "用户状态")
    private Integer userStatus;

}
