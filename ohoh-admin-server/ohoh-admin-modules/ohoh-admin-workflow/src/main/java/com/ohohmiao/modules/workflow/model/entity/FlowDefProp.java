package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 流程定义属性
 *
 * @author ohohmiao
 * @date 2025-05-27 17:04
 */
@Getter
@Setter
@TableName(value = "workflow_defprop")
public class FlowDefProp implements Serializable {

    @TableId
    private String propId;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private Integer propType;

    @TableField
    private String referRestype;

    @TableField
    private String referResid;

    @TableField
    private String referResname;

}
