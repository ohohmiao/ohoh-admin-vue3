package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.workflow.model.entity.FlowNodeBind;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程环节绑定信息mapper
 *
 * @author ohohmiao
 * @date 2025-01-16 09:54
 */
@Mapper
public interface FlowNodeBindMapper extends BaseMapper<FlowNodeBind> {
}
