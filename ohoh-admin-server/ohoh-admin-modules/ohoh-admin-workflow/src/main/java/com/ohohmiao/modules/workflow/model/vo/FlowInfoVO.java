package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程核心信息
 *
 * @author ohohmiao
 * @date 2025-06-08 11:10
 */
@ApiModel("流程核心信息")
@Getter
@Setter
public class FlowInfoVO {

    @ApiModelProperty(value = "是否发起流程阶段")
    private Boolean startFlowFlag;

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "流程定义名称")
    private String defName;

    @ApiModelProperty(value = "定义XML")
    private String defXml;

    @ApiModelProperty(value = "定义JSON")
    private String defJson;

    @ApiModelProperty(value = "绑定的表单id")
    private String formId;

    @ApiModelProperty(value = "绑定的表单路径")
    private String formPath;

    @ApiModelProperty(value = "当前操作环节名称")
    private String curNodeName;

    @ApiModelProperty(value = "当前操作环节id")
    private String curNodeId;

    @ApiModelProperty(value = "当前流程正在运行环节id")
    private String curRunningNodeIds;

    @ApiModelProperty(value = "是否处于查阅状态")
    private Boolean doQueryFlag;

}
