package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程环节节点
 *
 * @author ohohmiao
 * @date 2025-06-10 14:15
 */
@ApiModel("流程环节节点")
@Getter
@Setter
public class FlowTaskNodeVO {

    @ApiModelProperty(value = "节点id")
    private String nodeId;

    @ApiModelProperty(value = "节点名称")
    private String nodeName;

    @ApiModelProperty(value = "节点类别")
    private String nodeType;

    @ApiModelProperty(value = "环节办理人")
    private List<FlowNextHandlerVO> handlers;

}
