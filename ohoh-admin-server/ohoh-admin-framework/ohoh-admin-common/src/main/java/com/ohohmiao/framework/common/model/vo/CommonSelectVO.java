package com.ohohmiao.framework.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用下拉框数据结构
 *
 * @author ohohmiao
 * @date 2025-06-01 12:39
 */
@ApiModel("通用下拉框数据结构")
@Getter
@Setter
public class CommonSelectVO {

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String label;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String value;

}
