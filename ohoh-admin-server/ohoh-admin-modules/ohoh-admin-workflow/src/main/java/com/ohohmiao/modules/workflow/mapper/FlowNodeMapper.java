package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程环节mapper
 *
 * @author ohohmiao
 * @date 2025-05-30 16:50
 */
@Mapper
public interface FlowNodeMapper extends BaseMapper<FlowNode> {
}
