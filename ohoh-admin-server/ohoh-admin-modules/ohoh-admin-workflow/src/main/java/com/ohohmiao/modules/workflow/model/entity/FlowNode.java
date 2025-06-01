package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskMultiAssignWeight;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 流程环节
 *
 * @author ohohmiao
 * @date 2025-05-30 15:15
 */
@Getter
@Setter
@TableName(value = "workflow_node", autoResultMap = true)
public class FlowNode {

    @TableId
    private String propId;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String nodeId;

    @TableField
    private String nodeName;

    @TableField
    private Integer taskAssigntype;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer multiassignRule;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer multiassignRatio;

    @TableField(insertStrategy = FieldStrategy.ALWAYS,
            updateStrategy = FieldStrategy.ALWAYS,
            typeHandler = Fastjson2TypeHandler.class)
    private List<FlowTaskMultiAssignWeight> multiassignWeightjson;

    @TableField
    private Integer taskReturntype;

    @TableField
    private Integer processlimitPermit;

    @TableField
    private Integer approvalPermit;

}
