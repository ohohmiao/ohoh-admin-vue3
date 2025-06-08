package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 流程表单
 *
 * @author ohohmiao
 * @date 2025-06-07 14:48
 */
@Getter
@Setter
@TableName(value = "workflow_form")
public class FlowForm {

    @TableId
    private String formId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String formName;

    @TableField
    private String formPath;

}
