package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonTreeEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程定义类别
 *
 * @author ohohmiao
 * @date 2024-12-01 21:34
 */
@Getter
@Setter
@TableName(value = "workflow_deftype")
public class WorkflowDefType extends CommonTreeEntity {

    @TableId
    private String deftypeId;

    @TableField
    private String deftypeName;

    @TableField
    private String deftypeCode;

    @TableField
    private String deftypeRemark;

}
