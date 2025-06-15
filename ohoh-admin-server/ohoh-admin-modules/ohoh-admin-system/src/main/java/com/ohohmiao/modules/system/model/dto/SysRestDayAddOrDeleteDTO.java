package com.ohohmiao.modules.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 节假日列表新增或删除
 *
 * @author ohohmiao
 * @date 2024-12-19 16:37
 */
@ApiModel("节假日列表新增或删除")
@Getter
@Setter
public class SysRestDayAddOrDeleteDTO {

    @NotNull(message = "日期不能为空")
    @ApiModelProperty(value = "日期，格式yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
