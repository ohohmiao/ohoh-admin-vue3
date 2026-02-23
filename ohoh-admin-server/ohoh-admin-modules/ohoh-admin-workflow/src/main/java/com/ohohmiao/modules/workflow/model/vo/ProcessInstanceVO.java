package com.ohohmiao.modules.workflow.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流程实例
 *
 * @author ohohmiao
 * @date 2026-02-23 21:08
 */
@ApiModel("流程实例")
@Getter
@Setter
public class ProcessInstanceVO {

    @ApiModelProperty(value = "流程实例id")
    private String processId;

    @ApiModelProperty(value = "流程编码")
    private String defCode;

    @ApiModelProperty(value = "版本号")
    private Integer defVersion;

    @ApiModelProperty(value = "流水号")
    private String processNum;

    @ApiModelProperty(value = "流程实例状态")
    private Integer processState;

    @ApiModelProperty(value = "流程标题")
    private String processSubject;

    @ApiModelProperty(value = "发起人id")
    private String creatorId;

    @ApiModelProperty(value = "发起人")
    private String creatorName;

    @ApiModelProperty(value = "发起人部门id")
    private String creatorOrgid;

    @ApiModelProperty(value = "发起人部门")
    private String creatorOrgname;

    @ApiModelProperty(value = "流程实例创建时间")
    private LocalDateTime processStarttime;

    @ApiModelProperty(value = "流程任务id")
    private String taskId;

    @ApiModelProperty(value = "任务状态")
    private Integer taskState;

    @ApiModelProperty(value = "任务环节id")
    private String taskNodeid;

    @ApiModelProperty(value = "任务环节名称")
    private String taskNodename;

    @ApiModelProperty(value = "任务开始时间")
    private LocalDateTime taskStarttime;

    @ApiModelProperty(value = "任务办理截止时间")
    private LocalDateTime taskDeadline;

}
