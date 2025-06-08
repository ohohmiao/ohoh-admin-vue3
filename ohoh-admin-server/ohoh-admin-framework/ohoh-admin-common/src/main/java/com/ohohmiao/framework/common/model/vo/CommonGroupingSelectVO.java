package com.ohohmiao.framework.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 通用分组型下拉框数据结构
 *
 * @author ohohmiao
 * @date 2025-06-07 23:30
 */
@ApiModel("通用分组型下拉框数据结构")
@Getter
@Setter
public class CommonGroupingSelectVO {

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String label;

    /**
     * 选项组
     */
    @ApiModelProperty("选项组")
    private List<CommonSelectVO> options;

}
