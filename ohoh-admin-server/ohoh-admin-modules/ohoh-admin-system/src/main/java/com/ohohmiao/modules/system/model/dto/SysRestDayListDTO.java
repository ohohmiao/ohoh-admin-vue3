package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 节假日列表查询条件
 *
 * @author ohohmiao
 * @date 2024-12-19 11:24
 */
@ApiModel("节假日列表查询条件")
@Getter
@Setter
public class SysRestDayListDTO {

    @NotBlank(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期，格式YYYY-MM-DD", required = true)
    private String start;

    @NotBlank(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期，格式YYYY-MM-DD", required = true)
    private String end;

}
