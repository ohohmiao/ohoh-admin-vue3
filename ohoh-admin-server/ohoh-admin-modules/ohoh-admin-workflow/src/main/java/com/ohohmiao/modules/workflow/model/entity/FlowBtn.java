package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程按钮
 *
 * @author ohohmiao
 * @date 2025-06-08 09:41
 */
@Getter
@Setter
@TableName(value = "workflow_btn")
public class FlowBtn extends CommonEntity {

    @TableId
    private String btnId;

    @TableField
    private String btnText;

    @TableField
    private String btnColor;

    @TableField
    private String btnFun;

}
