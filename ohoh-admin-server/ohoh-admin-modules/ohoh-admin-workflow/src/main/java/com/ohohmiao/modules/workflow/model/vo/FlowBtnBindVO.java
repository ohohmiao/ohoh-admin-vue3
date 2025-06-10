package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程按钮绑定
 *
 * @author ohohmiao
 * @date 2025-06-09 10:41
 */
@ApiModel("流程按钮绑定")
@Getter
@Setter
public class FlowBtnBindVO {

    @ApiModelProperty(value = "按钮id")
    private String btnId;

    @ApiModelProperty("按钮文本")
    private String btnText;

    @ApiModelProperty("按钮颜色")
    private String btnColor;

    @ApiModelProperty("执行方法")
    private String btnFun;

    @ApiModelProperty("排序")
    private Integer btnSort;

    @ApiModelProperty(value = "绑定环节id")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "绑定环节名称")
    private List<String> bindNodeNames;

}
