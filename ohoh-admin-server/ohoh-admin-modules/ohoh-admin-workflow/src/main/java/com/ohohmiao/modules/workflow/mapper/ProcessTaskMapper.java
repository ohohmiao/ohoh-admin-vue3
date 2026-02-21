package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.workflow.model.entity.ProcessTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程任务mapper
 *
 * @author ohohmiao
 * @date 2026-02-14 15:18
 */
@Mapper
public interface ProcessTaskMapper extends BaseMapper<ProcessTask> {
}
