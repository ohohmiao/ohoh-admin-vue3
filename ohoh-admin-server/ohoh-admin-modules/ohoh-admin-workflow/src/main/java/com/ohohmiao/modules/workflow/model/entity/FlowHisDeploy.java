package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程历史部署
 *
 * @author ohohmiao
 * @date 2024-12-08 11:29
 */
@Getter
@Setter
@TableName(value = "workflow_hisdeploy")
public class FlowHisDeploy extends CommonEntity {

    @TableId
    private String hisdeployId;

    @TableField
    private String deftypeId;

    @TableField
    private String defName;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String flowentityClassname;

    @TableField
    private Integer defSort;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer initiatorScope;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer processLimittype;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer processLimitvalue;

    @TableField
    private String defXml;

    @TableField
    private String defJson;

    @TableField
    private String defSvg;

}
