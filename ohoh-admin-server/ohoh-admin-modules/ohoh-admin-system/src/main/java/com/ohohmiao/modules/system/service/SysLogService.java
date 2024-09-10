package com.ohohmiao.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.system.model.entity.SysLog;
import com.ohohmiao.modules.system.model.dto.SysLogPageDTO;

/**
 * 系统日志Service
 *
 * @author ohohmiao
 * @date 2023-08-04 11:51
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 获取系统日志分页列表
     * @param sysLogPageDTO
     * @return
     */
    Page<SysLog> listByPage(SysLogPageDTO sysLogPageDTO);

}
