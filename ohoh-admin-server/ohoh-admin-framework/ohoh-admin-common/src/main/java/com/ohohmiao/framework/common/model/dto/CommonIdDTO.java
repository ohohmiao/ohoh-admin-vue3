package com.ohohmiao.framework.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 通用id传参
 *
 * @author ohohmiao
 * @date 2023-04-17 10:36
 */
@ApiModel("id传参")
@Getter
@Setter
public class CommonIdDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

}
