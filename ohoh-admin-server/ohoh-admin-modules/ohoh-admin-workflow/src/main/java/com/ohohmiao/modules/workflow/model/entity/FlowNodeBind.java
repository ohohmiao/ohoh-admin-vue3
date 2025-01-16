package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程环节绑定信息
 *
 * @author ohohmiao
 * @date 2025-01-15 22:13
 */
@Getter
@Setter
@TableName(value = "workflow_nodebind")
public class FlowNodeBind {

    @TableId
    private String bindId;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String nodeId;

    @TableField
    private String nodeName;

    @TableField
    private Integer bindType;

    @TableField
    private String bindObjid;

}
