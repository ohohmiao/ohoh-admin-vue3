package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeGetDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowNode;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskMultiAssignWeight;
import com.ohohmiao.modules.workflow.model.vo.FlowNodeVO;

import java.util.List;

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

    /**
     * 获取流程某环节多人决策权重配置列表
     * @param listDTO
     * @return
     */
    List<FlowTaskMultiAssignWeight> listMultiAssignWeight(FlowNodeGetDTO listDTO);

    /**
     * 重置流程某环节多人决策权重配置列表
     * @param getDTO
     * @return
     */
    List<FlowTaskMultiAssignWeight> resetMultiAssignWeight(FlowNodeGetDTO getDTO);

    /**
     * 获取流程下一任务环节基本信息
     * @param getDTO
     * @return
     */
    List<CommonSelectVO> listNextTaskNodeInfo(FlowNodeGetDTO getDTO);

}
