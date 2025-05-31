package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.modules.workflow.model.pojo.FlowTaskMultiAssignWeight;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程环节属性
 *
 * @author ohohmiao
 * @date 2025-05-30 16:58
 */
@ApiModel("流程环节属性")
@Getter
@Setter
public class FlowNodeVO {

    @ApiModelProperty(value = "主键id")
    private String propId;

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "环节id")
    private String nodeId;

    @ApiModelProperty(value = "环节名称")
    private String nodeName;

    @ApiModelProperty(value = "任务指派类别")
    private Integer taskAssigntype;

    @ApiModelProperty(value = "多人决策方式")
    private Integer multiassignRule;

    @ApiModelProperty(value = "多人决策比例值")
    private Integer multiassignRatio;

    @ApiModelProperty(value = "多人决策权重json")
    private List<FlowTaskMultiAssignWeight> multiassignWeightjson;

    @ApiModelProperty(value = "退回执行方式")
    private Integer taskReturntype;

    @ApiModelProperty(value = "允许指定办理期限")
    private Integer processlimitPermit;

    @ApiModelProperty(value = "添加审批结果控件")
    private Integer approvalPermit;

}
