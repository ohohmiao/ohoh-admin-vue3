package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.common.util.MapstructUtils;
import com.ohohmiao.framework.common.model.event.AuthLogEvent;
import com.ohohmiao.framework.common.model.event.OperateLogEvent;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.modules.system.model.dto.SysLogAddOrEditDTO;
import com.ohohmiao.modules.system.model.entity.SysLog;
import com.ohohmiao.modules.system.mapper.SysLogMapper;
import com.ohohmiao.modules.system.model.dto.SysLogPageDTO;
import com.ohohmiao.modules.system.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 系统日志Service实现类
 *
 * @author ohohmiao
 * @date 2023-08-04 11:53
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    public Page<SysLog> listByPage(SysLogPageDTO sysLogPageDTO){
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotNull(sysLogPageDTO.getLogType()), SysLog::getLogType, sysLogPageDTO.getLogType());
        queryWrapper.like(StrUtil.isNotEmpty(sysLogPageDTO.getLogName()), SysLog::getLogName, sysLogPageDTO.getLogName());
        queryWrapper.orderByDesc(CollectionUtil.newArrayList(SysLog::getOperateTime, SysLog::getOperateUserid));
        return this.page(CommonPageRequest.constructPage(sysLogPageDTO.getCurrent(), sysLogPageDTO.getSize()), queryWrapper);
    }

    @Async
    @EventListener
    public void recordOperateLog(OperateLogEvent logEvent){
        SysLogAddOrEditDTO sysLogAddOrEditDTO = MapstructUtils.convert(logEvent, SysLogAddOrEditDTO.class);
        insertSysLog(sysLogAddOrEditDTO);
    }

    @Async
    @EventListener
    public void recordAuthLog(AuthLogEvent logEvent){
        SysLogAddOrEditDTO sysLogAddOrEditDTO = MapstructUtils.convert(logEvent, SysLogAddOrEditDTO.class);
        insertSysLog(sysLogAddOrEditDTO);
    }

    private void insertSysLog(SysLogAddOrEditDTO sysLogAddOrEditDTO){
        SysLog sysLog = MapstructUtils.convert(sysLogAddOrEditDTO, SysLog.class);
        sysLog.setLogId(null);
        sysLog.setOperateTime(new Date());
        this.save(sysLog);
    }

}
