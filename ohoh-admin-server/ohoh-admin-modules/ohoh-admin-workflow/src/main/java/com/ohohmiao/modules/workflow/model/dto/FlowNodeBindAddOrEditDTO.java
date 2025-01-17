package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 环节绑定信息新增或编辑
 *
 * @author ohohmiao
 * @date 2025-01-17 21:25
 */
@ApiModel("环节绑定信息新增或编辑")
@Getter
@Setter
public class FlowNodeBindAddOrEditDTO {

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "绑定类别")
    private Integer bindType;

    @ApiModelProperty(value = "绑定对象id")
    private String bindObjid;

    @ApiModelProperty(value = "环节id")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "环节名称")
    private List<String> bindNodeNames;

}
