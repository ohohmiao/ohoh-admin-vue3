package com.ohohmiao.modules.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 节假日列表查询条件
 *
 * @author ohohmiao
 * @date 2024-12-19 11:24
 */
@ApiModel("节假日列表查询条件")
@Getter
@Setter
public class SysRestDayListDTO {

    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "开始日期，格式yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "结束日期，格式yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date end;

}
