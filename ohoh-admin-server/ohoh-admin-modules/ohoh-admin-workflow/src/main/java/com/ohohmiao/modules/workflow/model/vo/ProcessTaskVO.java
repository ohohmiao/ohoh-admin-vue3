package com.ohohmiao.modules.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohohmiao.framework.common.model.vo.CommonVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流程任务
 *
 * @author ohohmiao
 * @date 2026-02-22 14:24
 */
@ApiModel("流程任务")
@Getter
@Setter
public class ProcessTaskVO extends CommonVO {

    @ApiModelProperty(value = "流程任务id")
    private String taskId;

    @ApiModelProperty(value = "流程实例id")
    private String processId;

    @ApiModelProperty(value = "任务状态")
    private Integer taskState;

    @ApiModelProperty(value = "任务环节id")
    private String taskNodeid;

    @ApiModelProperty(value = "任务环节名称")
    private String taskNodename;

    @ApiModelProperty(value = "上一环节节点id")
    private String incomingNodeid;

    @ApiModelProperty(value = "上一环节节点名称")
    private String incomingNodename;

    @ApiModelProperty(value = "父任务id")
    private String parentTaskid;

    @ApiModelProperty(value = "去往任务id串")
    private String outgoingTaskids;

    @ApiModelProperty(value = "多人任务审核方式")
    private Integer multiHandletype;

    @ApiModelProperty(value = "任务组id")
    private String taskGroupid;

    @ApiModelProperty(value = "指定办理人id串")
    private String assignHandlerids;

    @ApiModelProperty(value = "指定办理人姓名串")
    private String assignHandlernames;

    @ApiModelProperty(value = "指定办理人部门id串")
    private String assignHandlerorgids;

    @ApiModelProperty(value = "指定办理人部门名称串")
    private String assignHandlerorgnames;

    @ApiModelProperty(value = "办理人id")
    private String handlerId;

    @ApiModelProperty(value = "办理人姓名")
    private String handlerName;

    @ApiModelProperty(value = "办理人部门id")
    private String handlerOrgid;

    @ApiModelProperty(value = "办理人部门名称")
    private String handlerOrgname;

    @ApiModelProperty(value = "任务开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskStarttime;

    @ApiModelProperty(value = "是否审核通过")
    private Integer approvalResult;

    @ApiModelProperty(value = "办理意见")
    private String handleOpinion;

    @ApiModelProperty(value = "任务办理截止时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskDeadline;

    @ApiModelProperty(value = "任务办理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskEndtime;

    @ApiModelProperty(value = "办理消耗秒数")
    private Long consumeSeconds;

    @ApiModelProperty(value = "是否办理超时")
    private Integer overtimeFlag;

    @ApiModelProperty(value = "超期秒数")
    private Long exceedSeconds;

}
