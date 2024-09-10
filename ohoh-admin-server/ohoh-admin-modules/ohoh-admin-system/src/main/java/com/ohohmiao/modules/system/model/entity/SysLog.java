package com.ohohmiao.modules.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统日志
 *
 * @author ohohmiao
 * @date 2023-08-04 11:38
 */
@Getter
@Setter
@TableName(value = "sys_log")
public class SysLog {

    @TableId
    private String logId;

    @TableField
    private Integer logType;

    @TableField
    private String logName;

    @TableField
    private String operateIp;

    @TableField
    private String operateBrowser;

    @TableField
    private String operateOs;

    @TableField
    private String requestUri;

    @TableField
    private String paramJson;

    @TableField
    private String resultJson;

    @TableField
    private String errorMsg;

    @TableField
    private String operateUserid;

    @TableField
    private String operateUser;

    @TableField
    private String operateOrgname;

    @TableField
    private Date operateTime;

    @TableField
    private Long costTime;

}
