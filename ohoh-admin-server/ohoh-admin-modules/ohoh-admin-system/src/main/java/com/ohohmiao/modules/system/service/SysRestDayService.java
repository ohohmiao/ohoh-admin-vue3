package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.system.model.dto.SysRestDayListDTO;
import com.ohohmiao.modules.system.model.entity.SysRestDay;
import com.ohohmiao.modules.system.model.vo.FullCalendarEventVO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 节假日Service
 *
 * @author ohohmiao
 * @date 2024-12-18 14:25
 */
public interface SysRestDayService extends IService<SysRestDay> {

    /**
     * 列出某日期范围内的休假日-FullCalendar
     * @param sysRestDayListDTO
     * @return
     */
    List<FullCalendarEventVO> list4FullCalendar(SysRestDayListDTO sysRestDayListDTO);

    /**
     * 新增节假日
     * @param date
     */
    void add(LocalDate date);

    /**
     * 删除节假日
     * @param date
     */
    void delete(LocalDate date);

    /**
     * 计算某个日期指定偏移天数后的工作日
     * @param datetime
     * @param offsetDays
     * @return
     */
    LocalDateTime calcWorkday(LocalDateTime datetime, int offsetDays);

}
