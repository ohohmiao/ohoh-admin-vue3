package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程环节绑定信息查询条件
 *
 * @author ohohmiao
 * @date 2025-01-16 14:55
 */
@ApiModel("流程环节绑定信息查询条件")
@Getter
@Setter
public class FlowNodeBindQueryDTO {

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "绑定类别")
    private Integer bindType;

}
