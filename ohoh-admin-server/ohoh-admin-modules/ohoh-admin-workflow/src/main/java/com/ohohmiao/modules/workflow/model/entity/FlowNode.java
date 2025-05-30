package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程环节
 *
 * @author ohohmiao
 * @date 2025-05-30 15:15
 */
@Getter
@Setter
@TableName(value = "workflow_node")
public class FlowNode {

    @TableId
    private String propId;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String nodeId;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String nodeName;

    @TableField
    private Integer taskAssigntype;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer multiassignRule;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private Integer multiassignRatio;

    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String multiassignWeightjson;

    @TableField
    private Integer taskReturntype;

    @TableField
    private Integer processlimitPermit;

    @TableField
    private Integer approvalPermit;

}
