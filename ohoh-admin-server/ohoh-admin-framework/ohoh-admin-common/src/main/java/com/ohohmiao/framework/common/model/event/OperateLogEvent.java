package com.ohohmiao.framework.common.model.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 操作日志事件
 *
 * @author ohohmiao
 * @date 2024-09-07 15:51
 */
@Data
public class OperateLogEvent implements Serializable {

    private Integer logType;

    private String logName;

    private String operateIp;

    private String operateBrowser;

    private String operateOs;

    private String requestUri;

    private String paramJson;

    private String resultJson;

    private String errorMsg;

    private String operateUserid;

    private String operateUser;

    private String operateOrgname;

    private Long costTime;

}
