package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统用户分页查询条件
 *
 * @author ohohmiao
 * @date 2023/4/16 15:10
 */
@ApiModel("系统用户分页查询条件")
@Getter
@Setter
public class SysUserPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "机构id")
    private String orgId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "手机号码")
    private String userMobile;

    @ApiModelProperty(value = "性别")
    private Integer userGender;

    @ApiModelProperty(value = "状态")
    private Integer userStatus;

}
