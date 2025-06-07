package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.entity.FlowDef;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程定义mapper
 *
 * @author ohohmiao
 * @date 2024-12-08 19:49
 */
@Mapper
public interface FlowDefMapper extends BaseMapper<FlowDef> {

    /**
     * 根据Wrapper，分页查询
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<FlowDefVO> pageByWrapper(Page<FlowDefVO> page, @Param(Constants.WRAPPER)Wrapper<FlowDefVO> queryWrapper);

    /**
     * 获取某流程类别下的最大排序
     * @param deftypeId
     * @return
     */
    Integer getMaxSortByDeftypeId(@Param("deftypeId") String deftypeId);

    /**
     * 获取可发起的流程列表
     * @param deftypeId
     * @param userId
     * @return
     */
    List<FlowDefVO> listInitiable(@Param("deftypeId") String deftypeId,
                        @Param("userId") String userId, @Param("isSuperAdmin") Boolean isSuperAdmin);

}
