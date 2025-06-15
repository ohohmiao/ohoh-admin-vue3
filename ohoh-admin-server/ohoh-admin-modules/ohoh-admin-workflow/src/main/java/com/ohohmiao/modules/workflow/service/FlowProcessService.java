package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.entity.FlowProcess;
import com.ohohmiao.modules.workflow.model.pojo.FlowTaskHandler;
import com.ohohmiao.modules.workflow.model.vo.FlowInfoVO;

import java.util.List;

/**
 * 流程实例Service
 *
 * @author ohohmiao
 * @date 2025-06-11 10:22
 */
public interface FlowProcessService extends CommonService<FlowProcess> {

    /**
     * 获取流程发起人
     * @param flowInfoVO
     * @return
     */
    List<FlowTaskHandler> getCreator(FlowInfoVO flowInfoVO);

    /**
     * 保存或更新流程实例
     * @param flowInfoVO
     * @param isTempSave
     */
    void saveOrUpdate(FlowInfoVO flowInfoVO, boolean isTempSave);

}
