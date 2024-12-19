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

import java.util.List;
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
    public void add(String date){
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
    public void delete(String date){
        LambdaUpdateWrapper<SysRestDay> deleteWrapper = new LambdaUpdateWrapper<>();
        deleteWrapper.eq(SysRestDay::getRestdayDate, date);
        this.remove(deleteWrapper);
    }

}
