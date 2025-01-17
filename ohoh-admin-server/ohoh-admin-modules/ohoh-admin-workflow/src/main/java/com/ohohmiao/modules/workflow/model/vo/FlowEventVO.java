package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 流程事件
 *
 * @author ohohmiao
 * @date 2025-01-16 13:34
 */
@ApiModel("流程事件")
@Getter
@Setter
public class FlowEventVO {

    @ApiModelProperty(value = "事件id")
    private String eventId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty(value = "事件名称")
    private String eventName;

    @ApiModelProperty(value = "事件类别")
    private Integer eventType;

    @ApiModelProperty(value = "绑定环节id")
    private List<String> bindNodeIds;

    @ApiModelProperty(value = "绑定环节名称")
    private List<String> bindNodeNames;

    @ApiModelProperty(value = "实现类别")
    private Integer implType;

    @ApiModelProperty(value = "本地服务")
    private String implLocalservice;

    @ApiModelProperty(value = "实现脚本")
    private String implScript;

}
