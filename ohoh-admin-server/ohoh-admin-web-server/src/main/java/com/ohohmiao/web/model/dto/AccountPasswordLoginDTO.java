package com.ohohmiao.web.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 账号密码登录
 *
 * @author ohohmiao
 * @date 2023/4/3 22:42
 */
@ApiModel("账号密码登录传参")
@Getter
@Setter
public class AccountPasswordLoginDTO {

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "唯一码", required = true)
    @NotBlank(message = "唯一码不能为空")
    private String uuid;

}
