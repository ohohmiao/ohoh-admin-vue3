package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 流程表单
 *
 * @author ohohmiao
 * @date 2025-06-08 11:56
 */
@ApiModel("流程表单")
@Getter
@Setter
public class FlowFormVO {

    @ApiModelProperty(value = "表单id")
    private String formId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty(value = "表单名称")
    private String formName;

    @ApiModelProperty(value = "表单路径")
    private String formPath;

}
