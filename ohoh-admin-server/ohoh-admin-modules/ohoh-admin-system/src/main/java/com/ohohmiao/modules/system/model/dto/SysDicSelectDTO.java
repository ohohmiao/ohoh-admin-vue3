package com.ohohmiao.modules.system.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 系统字典下拉框
 *
 * @author ohohmiao
 * @date 2025-06-01 16:51
 */
@ApiModel("系统字典下拉框")
@Getter
@Setter
public class SysDicSelectDTO {

    @ApiModelProperty(value = "字典类别编码", required = true)
    @NotBlank(message = "字典类别编码不能为空")
    private String dictypeCode;

}
