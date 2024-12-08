package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程定义
 *
 * @author ohohmiao
 * @date 2024-12-08 11:16
 */
@Getter
@Setter
@TableName(value = "workflow_def")
public class FlowDef extends CommonEntity {

    @TableId
    private String defId;

    @TableField
    private String deftypeId;

    @TableField
    private String defName;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private Integer defSort;

    @TableField
    private String defXml;

    @TableField
    private String defJson;

    @TableField
    private String defSvg;

}
