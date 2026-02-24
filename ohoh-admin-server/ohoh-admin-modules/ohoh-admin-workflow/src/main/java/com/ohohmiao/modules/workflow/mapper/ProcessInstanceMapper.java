package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.entity.ProcessInstance;
import com.ohohmiao.modules.workflow.model.vo.ProcessInstanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 流程实例mapper
 *
 * @author ohohmiao
 * @date 2025-06-14 14:30
 */
@Mapper
public interface ProcessInstanceMapper extends BaseMapper<ProcessInstance> {

    /**
     * 获取待我审批流程列表
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<ProcessInstanceVO> pageProcessInstanceVO(Page<ProcessInstanceVO> page, @Param(Constants.WRAPPER) Wrapper<ProcessInstanceVO> queryWrapper);

}
