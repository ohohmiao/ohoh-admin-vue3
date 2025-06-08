package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployDTO;
import com.ohohmiao.modules.workflow.model.dto.FlowHisDeployListDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowHisDeploy;
import com.ohohmiao.modules.workflow.model.vo.FlowDefVO;

import java.util.List;
import java.util.Map;

/**
 * 流程历史部署Service
 *
 * @author ohohmiao
 * @date 2024-12-08 19:55
 */
public interface FlowHisDeployService extends CommonService<FlowHisDeploy> {

    /**
     * 根据流程编码和版本号获取流程定义
     * @param defCode
     * @param defVersion
     * @param includeExtraInfo 是否包含附加信息
     * @return
     */
    FlowDefVO get(String defCode, Integer defVersion, boolean includeExtraInfo);

    /**
     * 修改流程历史定义
     * @param hisDTO
     */
    void edit(FlowHisDeployDTO hisDTO);

    /**
     * 获取历史版本列表
     * @param listDTO
     * @return
     */
    List<FlowDefVO> list(FlowHisDeployListDTO listDTO);

    /**
     * 根据流程编码和版本号获取
     * @param defCode
     * @param defVersion
     * @return
     */
    FlowHisDeploy getByDefCodeAndDefVersion(String defCode, Integer defVersion);

    /**
     * 根据流程类别，获取可发起的流程列表
     * @param deftypeId
     * @return
     */
    Map<String, List<FlowDefVO>> listInitiableByDeftype(String deftypeId);

}
