package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程下一步环节信息
 *
 * @author ohohmiao
 * @date 2025-06-10 10:11
 */
@ApiModel("流程下一步环节信息")
@Getter
@Setter
public class FlowNextNodeVO {

    private FlowNodeVO curNodeInfo;

    private List<FlowNextHandlerVO> nextHandlerList;

}
