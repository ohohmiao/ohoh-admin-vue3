package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowHandler;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程环节办理人配置mapper
 *
 * @author ohohmiao
 * @date 2025-06-01 10:58
 */
@Mapper
public interface FlowHandlerMapper extends BaseMapper<FlowHandler> {
}
