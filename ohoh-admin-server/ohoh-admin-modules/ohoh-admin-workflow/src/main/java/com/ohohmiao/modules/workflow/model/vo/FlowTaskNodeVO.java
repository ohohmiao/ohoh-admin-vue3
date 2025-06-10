package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程环节节点配置
 *
 * @author ohohmiao
 * @date 2025-06-10 10:37
 */
@ApiModel("流程环节节点配置")
@Getter
@Setter
public class FlowTaskNodeVO {

    @ApiModelProperty(value = "环节id")
    private String nodeId;

    @ApiModelProperty(value = "环节名称")
    private String nodeName;

    @ApiModelProperty(value = "节点类别")
    private String nodeType;

    @ApiModelProperty(value = "办理人")
    private List<FlowTaskHandler> handlers;

    private Integer multiHandletype;

    @ApiModelProperty(value = "允许重选办理人")
    private Integer reselectPermit;

    //private String taskId;

    //private String toTaskIds;

    private String inclusiveGateWayId;

    @ApiModelProperty(value = "流程环节")
    private List<FlowTaskNodeVO> nodeList;

}
