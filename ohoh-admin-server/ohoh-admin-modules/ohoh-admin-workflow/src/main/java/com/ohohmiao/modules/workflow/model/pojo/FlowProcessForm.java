package com.ohohmiao.modules.workflow.model.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程审核表单
 *
 * @author ohohmiao
 * @date 2025-06-14 13:59
 */
@ApiModel("流程审核表单")
@Getter
@Setter
public class FlowProcessForm {

    @ApiModelProperty(value = "审核结果0-不通过1-通过")
    private Integer appovalResult;

    @ApiModelProperty(value = "指定办理期限")
    private String handleDeadline;

    @ApiModelProperty(value = "审核意见")
    private String handleOpinion;

}
