package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 流程定义绑定信息
 *
 * @author ohohmiao
 * @date 2025-05-27 17:04
 */
@Getter
@Setter
@TableName(value = "workflow_defbind")
public class FlowDefBind implements Serializable {

    @TableId
    private String bindId;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private Integer bindType;

    @TableField
    private String referRestype;

    @TableField
    private String referResid;

    @TableField
    private String referResname;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String bindObjid;

    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private Integer bindSort;

}
