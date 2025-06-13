package com.ohohmiao.modules.demo.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 请假业务
 *
 * @author ohohmiao
 * @date 2025-06-12 15:31
 */
@Getter
@Setter
public class LeaveVO extends CommonVO {

    private String leaveId;

    private String applyUser;

    private String applyuserOrgid;

    private String applyuserOrgname;

    private Integer leaveType;

    private Date leaveStartdate;

    private Date leaveEnddate;

    private Integer leaveDays;

    private String applyReason;

    private String agentUserid;

    private String agentUser;

}
