package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 节假日列表新增或删除
 *
 * @author ohohmiao
 * @date 2024-12-19 16:37
 */
@ApiModel("节假日列表新增或删除")
@Getter
@Setter
public class SysRestDayAddOrDeleteDTO {

    @NotBlank(message = "日期不能为空")
    @ApiModelProperty(value = "日期，格式YYYY-MM-DD", required = true)
    private String date;

}
