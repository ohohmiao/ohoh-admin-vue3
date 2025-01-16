package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowEvent;
import com.ohohmiao.modules.workflow.model.vo.FlowEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 流程事件mapper
 *
 * @author ohohmiao
 * @date 2025-01-16 09:53
 */
@Mapper
public interface FlowEventMapper extends BaseMapper<FlowEvent> {

    /**
     * 分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    Page<FlowEventVO> page(Page<FlowEventVO> page, @Param("bind") FlowNodeBindQueryDTO queryDTO);

}
