package com.ohohmiao.modules.workflow.model.dto;

import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程下一步环节办理人
 *
 * @author ohohmiao
 * @date 2025-06-14 14:09
 */
@ApiModel("流程下一步环节办理人")
@Getter
@Setter
public class FlowNextHandlerDTO {

    private String nodeId;

    private String nodeName;

    private String nodeType;

    private List<FlowTaskHandler> handlers;

}
