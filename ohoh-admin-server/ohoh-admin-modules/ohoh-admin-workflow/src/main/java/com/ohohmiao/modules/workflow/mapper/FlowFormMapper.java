package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindQueryDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowForm;
import com.ohohmiao.modules.workflow.model.vo.FlowFormBindVO;
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
     * 绑定关系分页查询
     * @param page
     * @param queryDTO
     * @return
     */
    Page<FlowFormBindVO> listBindByPage(Page<FlowFormBindVO> page, @Param("bind") FlowNodeBindQueryDTO queryDTO);

    /**
     * 查询流程某环节绑定的表单
     * @param defCode
     * @param defVersion
     * @param nodeId
     * @return
     */
    FlowFormVO getBindForm(@Param("defCode") String defCode,
                  @Param("defVersion") Integer defVersion, @Param("nodeId") String nodeId);

}
