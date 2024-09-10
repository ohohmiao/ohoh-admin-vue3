package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统日志查询条件
 *
 * @author ohohmiao
 * @date 2023-08-07 16:18
 */
@ApiModel("系统日志查询条件")
@Getter
@Setter
public class SysLogPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "日志类别")
    private Integer logType;

    @ApiModelProperty(value = "日志名称")
    private String logName;

}
