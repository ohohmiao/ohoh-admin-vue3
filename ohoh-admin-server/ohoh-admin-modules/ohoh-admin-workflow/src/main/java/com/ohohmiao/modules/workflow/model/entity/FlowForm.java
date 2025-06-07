package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程表单
 *
 * @author ohohmiao
 * @date 2025-06-07 14:48
 */
@Getter
@Setter
@TableName(value = "workflow_form")
public class FlowForm extends CommonEntity {

    @TableId
    private String formId;

    @TableField
    private String deftypeId;

    @TableField
    private String formName;

    @TableField
    private String formPath;

}
