package com.ohohmiao.modules.demo.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import com.ohohmiao.modules.demo.model.dto.LeaveAddOrEditDTO;
import com.ohohmiao.modules.demo.model.vo.LeaveVO;
import com.ohohmiao.modules.workflow.annotation.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 请假业务
 *
 * @author ohohmiao
 * @date 2025-06-12 14:25
 */
@FlowEntity(value = LeaveVO.class, dto = LeaveAddOrEditDTO.class)
@Getter
@Setter
@TableName(value = "demo_leave")
public class Leave extends CommonEntity {

    @TableId
    private String leaveId;

    @TableField
    private String applyUser;

    @TableField
    private String applyuserOrgid;

    @TableField
    private String applyuserOrgname;

    @TableField
    private Integer leaveType;

    @TableField
    private Date leaveStartdate;

    @TableField
    private Date leaveEnddate;

    @TableField
    private Integer leaveDays;

    @TableField
    private String applyReason;

    @TableField
    private String agentUserid;

    @TableField
    private String agentUser;

}
