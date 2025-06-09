package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowBtn;
import com.ohohmiao.modules.workflow.model.vo.FlowBtnBindVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 流程按钮mapper
 *
 * @author ohohmiao
 * @date 2025-06-08 09:44
 */
@Mapper
public interface FlowBtnMapper extends BaseMapper<FlowBtn> {

    /**
     * 环节绑定关系分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    Page<FlowBtnBindVO> listBindByPage(Page<FlowBtnBindVO> page, @Param("bind") FlowNodeBindQueryDTO queryDTO);

}
