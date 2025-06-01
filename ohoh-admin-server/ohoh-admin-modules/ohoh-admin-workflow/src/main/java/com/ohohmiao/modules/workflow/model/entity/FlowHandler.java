package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程环节办理人配置
 *
 * @author ohohmiao
 * @date 2025-06-01 10:39
 */
@Getter
@Setter
@TableName(value = "workflow_handler")
public class FlowHandler {

    @TableId
    private String handlerId;

    @TableField
    private String createTime;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String nodeId;

    @TableField
    private String nextnodeId;

    @TableField
    private String nextnodeName;

    @TableField
    private Integer handlerType;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String interfaceCode;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String filterRule;

    @TableField
    private Integer multiHandletype;

    @TableField
    private Integer reselectPermit;

}
