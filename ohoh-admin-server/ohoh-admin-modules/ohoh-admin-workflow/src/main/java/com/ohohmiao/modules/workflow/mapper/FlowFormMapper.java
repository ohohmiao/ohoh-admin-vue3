package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormBindVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 流程表单mapper
 *
 * @author ohohmiao
 * @date 2025-06-07 14:52
 */
@Mapper
public interface FlowFormMapper extends BaseMapper<FlowForm> {

    /**
     * 绑定关系分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    Page<FlowFormBindVO> listBindByPage(Page<FlowFormBindVO> page, @Param("bind") FlowNodeBindQueryDTO queryDTO);

}
