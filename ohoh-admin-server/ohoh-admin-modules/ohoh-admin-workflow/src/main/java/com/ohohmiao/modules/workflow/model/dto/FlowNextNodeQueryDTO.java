package com.ohohmiao.modules.workflow.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 流程下一步环节信息查询
 *
 * @author ohohmiao
 * @date 2025-06-10 10:19
 */
@ApiModel("流程下一步环节信息查询")
@Getter
@Setter
public class FlowNextNodeQueryDTO extends FlowInfoQueryDTO {

    @ApiModelProperty(value = "执行动作", required = true)
    @NotNull(message = "执行动作不能为空")
    private Integer actType;

    @ApiModelProperty(value = "流程表单业务数据")
    private Object businessForm;

}
