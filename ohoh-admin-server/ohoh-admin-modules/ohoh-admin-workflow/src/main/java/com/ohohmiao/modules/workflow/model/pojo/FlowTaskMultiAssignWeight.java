package com.ohohmiao.modules.workflow.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程环节多人决策权重
 *
 * @author ohohmiao
 * @date 2025-05-31 17:12
 */
@ApiModel("流程环节多人决策权重")
@Getter
@Setter
public class FlowTaskMultiAssignWeight {

    @ApiModelProperty(value = "办理人id")
    private String handlerId;

    @ApiModelProperty(value = "办理人名称")
    private String handlerName;

    @ApiModelProperty(value = "权重分")
    private Integer weight;

}
