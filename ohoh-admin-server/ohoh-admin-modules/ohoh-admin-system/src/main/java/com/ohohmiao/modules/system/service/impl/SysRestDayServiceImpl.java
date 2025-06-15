package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.modules.system.mapper.SysRestDayMapper;
import com.ohohmiao.modules.system.model.dto.SysRestDayListDTO;
import com.ohohmiao.modules.system.model.entity.SysRestDay;
import com.ohohmiao.modules.system.model.vo.FullCalendarEventVO;
import com.ohohmiao.modules.system.service.SysRestDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 节假日Service实现
 *
 * @author ohohmiao
 * @date 2024-12-18 14:26
 */
@Service
public class SysRestDayServiceImpl extends ServiceImpl<SysRestDayMapper, SysRestDay> implements SysRestDayService {

    @Override
    public List<FullCalendarEventVO> list4FullCalendar(SysRestDayListDTO sysRestDayListDTO){
        LambdaQueryWrapper<SysRestDay> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(SysRestDay::getRestdayDate,
                sysRestDayListDTO.getStart(), sysRestDayListDTO.getEnd());
        queryWrapper.orderByAsc(SysRestDay::getRestdayDate);
        return this.list(queryWrapper).stream().map(m ->
                new FullCalendarEventVO(m.getRestdayDate())).collect(Collectors.toList());
    }

    @Override
    public void add(LocalDate date){
        LambdaQueryWrapper<SysRestDay> getOneWrapper = new LambdaQueryWrapper<>();
        getOneWrapper.eq(SysRestDay::getRestdayDate, date);
        SysRestDay sysRestDay = getOne(getOneWrapper);
        if(ObjectUtil.isNull(sysRestDay)){
            sysRestDay = new SysRestDay();
            sysRestDay.setRestdayDate(date);
            this.save(sysRestDay);
        }
    }

    @Override
    public void delete(LocalDate date){
        LambdaUpdateWrapper<SysRestDay> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(SysRestDay::getRestdayDate, date);
        this.remove(deleteWrapper);
    }

    @Override
    public LocalDateTime calcWorkday(LocalDateTime dateTime, int offsetDays){
        if(offsetDays == 0) return dateTime;
        LocalDate current = dateTime.toLocalDate();
        // 牛马的假期不会超过15天。。。
        int maxRange = Math.abs(offsetDays) * 15;
        LocalDate start = offsetDays > 0 ? current : current.minusDays(maxRange);
        LocalDate end = offsetDays > 0 ? current.plusDays(maxRange) : current;
        // 查出这个范围内的休息日数据
        LambdaQueryWrapper<SysRestDay> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.between(SysRestDay::getRestdayDate, start, end);
        Set<LocalDate> restdays = this.list(listWrapper).stream().map(SysRestDay::getRestdayDate).collect(Collectors.toSet());
        // 计算
        int moved = 0;
        while(moved != offsetDays){
            current = offsetDays > 0? current.plusDays(1): current.minusDays(1);
            if (!restdays.contains(current)) {
                moved += offsetDays > 0? 1: -1;
            }
        }
        if(restdays.contains(dateTime.toLocalDate())){
            // 指定日期是休息日，不计算当天的时间
            current = offsetDays > 0? current.plusDays(1): current.minusDays(1);
            return current.atStartOfDay();
        }else{
            return current.atTime(dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond(), dateTime.getNano());
        }
    }

}
