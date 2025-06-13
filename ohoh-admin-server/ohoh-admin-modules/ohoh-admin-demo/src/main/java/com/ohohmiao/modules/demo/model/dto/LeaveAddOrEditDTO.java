package com.ohohmiao.modules.demo.model.dto;

import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * 请假业务新增或编辑
 *
 * @author ohohmiao
 * @date 2025-06-12 14:42
 */
@Getter
@Setter
public class LeaveAddOrEditDTO {

    @Null(message = "id必须为空", groups = {CommonAddGroup.class})
    @NotBlank(message = "id不能为空", groups = {CommonEditGroup.class})
    private String leaveId;

    @NotBlank(message = "申请人不能为空")
    private String applyUser;

    private String applyuserOrgid;

    private String applyuserOrgname;

    @NotNull(message = "请假类别不能为空")
    private Integer leaveType;

    @NotNull(message = "请假开始日期不能为空")
    private Date leaveStartdate;

    @NotNull(message = "请假结束日期不能为空")
    private Date leaveEnddate;

    @NotNull(message = "请假天数不能为空")
    private Integer leaveDays;

    @NotBlank(message = "申请理由不能为空")
    private String applyReason;

    private String agentUserid;

    private String agentUser;

}
