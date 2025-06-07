package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormVO;
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
     * 根据Wrapper，分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<FlowFormVO> pageByWrapper(Page<FlowFormVO> page, @Param(Constants.WRAPPER)Wrapper<FlowFormVO> queryWrapper);

}
