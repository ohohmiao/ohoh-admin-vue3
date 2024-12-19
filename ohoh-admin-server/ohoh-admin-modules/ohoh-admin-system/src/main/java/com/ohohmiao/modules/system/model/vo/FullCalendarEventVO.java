package com.ohohmiao.modules.system.model.vo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * FullCalendar事件VO
 *
 * @author ohohmiao
 * @date 2024-12-19 10:32
 */
@ApiModel("FullCalendar事件")
@Getter
@Setter
public class FullCalendarEventVO {

    @ApiModelProperty(value = "事件id")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "是否整天")
    private Boolean allDay;

    @ApiModelProperty(value = "开始日期")
    private String start;

    @ApiModelProperty(value = "结束日期")
    private String end;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "背景色")
    private String backgroundColor;

    @ApiModelProperty(value = "字体颜色")
    private String textColor;

    @ApiModelProperty(value = "字体对齐")
    private String textAlign;

    public FullCalendarEventVO(Date date){
        String formatDate = DateUtil.format(date, "yyyy-MM-dd");
        this.id = formatDate;
        this.title = "休";
        this.allDay = true;
        this.start = formatDate;
        this.color = "#6aba49";
        this.backgroundColor = this.color;
        this.textColor = "#fff";
        this.textAlign = "left";
    }

}
