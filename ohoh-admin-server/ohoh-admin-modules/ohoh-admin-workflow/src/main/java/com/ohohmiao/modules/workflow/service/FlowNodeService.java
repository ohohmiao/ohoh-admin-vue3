package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowNode;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;

/**
 * 流程环节Service
 *
 * @author ohohmiao
 * @date 2025-05-30 16:52
 */
public interface FlowNodeService extends IService<FlowNode> {

    /**
     * 根据流程编码、版本号和环节id，获取流程环节属性
     * @param defCode
     * @param defVersion
     * @param nodeId
     * @return
     */
    FlowNodeVO get(String defCode, Integer defVersion, String nodeId);

    /**
     * 保存或更新流程某环节属性
     * @param addOrEditDTO
     */
    void saveOrUpdate(FlowNodeAddOrEditDTO addOrEditDTO);

}
