package com.ohohmiao.modules.workflow.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程任务办理人
 *
 * @author ohohmiao
 * @date 2025-06-02 09:44
 */
@ApiModel("流程任务办理人")
@Getter
@Setter
public class FlowTaskHandler {

    @ApiModelProperty(value = "办理人id")
    private String handlerId;

    @ApiModelProperty(value = "办理人名称")
    private String handlerName;

}
