package com.ohohmiao.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.system.model.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志mapper
 *
 * @author ohohmiao
 * @date 2023-08-04 11:48
 */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
}
