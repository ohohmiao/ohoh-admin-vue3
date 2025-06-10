package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程下一步环节办理人
 *
 * @author ohohmiao
 * @date 2025-06-10 10:37
 */
@ApiModel("流程下一步环节办理人")
@Getter
@Setter
public class FlowNextHandlerVO {

    // label/select
    private String displayType;

    private String nodeId;

    private String nodeName;

    private String nodeType;

//    private String[] nodeIds;
//
//    private String[] nodeNames;

    @ApiModelProperty(value = "办理人")
    private List<FlowTaskHandler> handlers;

    @ApiModelProperty(value = "流程环节")
    private List<FlowTaskNodeVO> nodes;

    private Integer reselectPermit;

    //private String toTaskIds;

    private Integer multiHandletype;

    //private String taskId;

    private String inclusiveGateWayId;

    //private Map<String, List<FlowNextHandlerVO>> subFlowHandlers;

}
