package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.event.AuthLogEvent;
import com.ohohmiao.framework.common.model.event.OperateLogEvent;
import com.ohohmiao.modules.system.model.entity.SysLog;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

/**
 * @author ohohmiao
 * @date 2024-09-07 17:44
 */
@Data
@AutoMappers({
    @AutoMapper(target = SysLog.class, reverseConvertGenerate = false),
    @AutoMapper(target = OperateLogEvent.class),
    @AutoMapper(target = AuthLogEvent.class)
})
public class SysLogAddOrEditDTO {

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
