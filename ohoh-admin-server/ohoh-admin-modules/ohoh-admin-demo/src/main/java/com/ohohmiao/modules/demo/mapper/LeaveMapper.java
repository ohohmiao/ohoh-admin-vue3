package com.ohohmiao.modules.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.demo.model.entity.Leave;
import org.apache.ibatis.annotations.Mapper;

/**
 * 请假业务mapper
 *
 * @author ohohmiao
 * @date 2025-06-12 14:36
 */
@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {
}
