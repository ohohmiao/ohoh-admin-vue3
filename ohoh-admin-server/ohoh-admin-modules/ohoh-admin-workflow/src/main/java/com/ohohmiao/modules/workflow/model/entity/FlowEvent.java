package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 流程事件
 *
 * @author ohohmiao
 * @date 2025-01-15 20:25
 */
@Getter
@Setter
@TableName(value = "workflow_event")
public class FlowEvent {

    @TableId
    private String eventId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String eventName;

    @TableField
    private Integer eventType;

    @TableField
    private Integer implType;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String implLocalservice;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String implScript;

}
