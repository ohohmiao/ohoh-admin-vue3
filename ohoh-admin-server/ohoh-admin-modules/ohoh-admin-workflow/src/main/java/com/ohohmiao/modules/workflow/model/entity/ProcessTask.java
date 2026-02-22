package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流程任务
 *
 * @author ohohmiao
 * @date 2025-06-15 17:48
 */
@Getter
@Setter
@TableName(value = "process_task")
public class ProcessTask extends CommonEntity {

    @TableId
    private String taskId;

    @TableField
    private String processId;

    @TableField
    private Integer taskState;

    @TableField
    private String taskNodeid;

    @TableField
    private String taskNodename;

    @TableField
    private String incomingNodeid;

    @TableField
    private String incomingNodename;

    @TableField
    private String parentTaskid;

    @TableField
    private String outgoingTaskids;

    @TableField
    private Integer multiHandletype;

    @TableField
    private String taskGroupid;

    @TableField
    private String assignHandlerids;

    @TableField
    private String assignHandlernames;

    @TableField
    private String assignHandlerorgids;

    @TableField
    private String assignHandlerorgnames;

    @TableField
    private String handlerId;

    @TableField
    private String handlerName;

    @TableField
    private String handlerOrgid;

    @TableField
    private String handlerOrgname;

    @TableField
    private LocalDateTime taskStarttime;

    @TableField
    private Integer approvalResult;

    @TableField
    private String handleOpinion;

    @TableField
    private LocalDateTime taskDeadline;

    @TableField
    private LocalDateTime taskEndtime;

    @TableField
    private Long consumeSeconds;

    @TableField
    private Integer overtimeFlag;

    @TableField
    private Long exceedSeconds;

}
