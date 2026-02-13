package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.workflow.model.entity.ProcessInstance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程实例mapper
 *
 * @author ohohmiao
 * @date 2025-06-14 14:30
 */
@Mapper
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstance> {
}
