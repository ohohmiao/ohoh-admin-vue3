package com.ohohmiao.framework.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用表格分页传参
 *
 * @author ohohmiao
 * @date 2023-04-13 15:08
 */
@Getter
@Setter
public class CommonPageDTO {

    @ApiModelProperty(value = "当前页")
    private Integer current;

    @ApiModelProperty(value = "每页大小")
    private Integer size;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序顺序")
    private String sortOrder;

}
