package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 流程表单绑定
 *
 * @author ohohmiao
 * @date 2025-06-07 22:31
 */
@ApiModel("流程表单绑定")
@Getter
@Setter
public class FlowFormBindVO {

    @ApiModelProperty(value = "表单id")
    private String formId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty(value = "表单名称")
    private String formName;

    @ApiModelProperty(value = "表单路径")
    private String formPath;

    @ApiModelProperty(value = "绑定环节id")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "绑定环节名称")
    private List<String> bindNodeNames;

}
