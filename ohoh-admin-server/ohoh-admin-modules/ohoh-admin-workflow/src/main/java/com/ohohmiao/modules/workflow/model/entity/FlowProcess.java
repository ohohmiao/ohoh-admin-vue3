package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流程实例
 *
 * @author ohohmiao
 * @date 2025-06-14 13:50
 */
@Getter
@Setter
@TableName(value = "workflow_process")
public class FlowProcess extends CommonEntity {

    @TableId
    private String processId;

    @TableField
    private String defCode;

    @TableField
    private Integer defVersion;

    @TableField
    private String processNum;

    @TableField
    private Integer processState;

    @TableField
    private String busTablename;

    @TableField
    private String busRecordid;

    @TableField
    private String processSubject;

    @TableField
    private Integer creatorType;

    @TableField
    private String creatorId;

    @TableField
    private String creatorName;

    @TableField
    private String currunningNodenames;

    @TableField
    private String currunningNodeids;

    @TableField
    private String curHandlerids;

    @TableField
    private String curHandlernames;

    @TableField
    private LocalDateTime processStarttime;

    @TableField
    private LocalDateTime endDeadline;

    @TableField
    private String finalOpinion;

    @TableField
    private LocalDateTime processEndtime;

    @TableField
    private Integer overtimeFlag;

    @TableField
    private Long consumeSeconds;

    @TableField
    private Long leftSeconds;

    @TableField
    private Long exceedSeconds;

}
