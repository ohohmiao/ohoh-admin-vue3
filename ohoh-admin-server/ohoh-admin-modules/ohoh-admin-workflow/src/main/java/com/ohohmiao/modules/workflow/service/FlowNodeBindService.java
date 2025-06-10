package com.ohohmiao.modules.workflow.service;

import com.ohohmiao.framework.mybatis.service.CommonService;
import com.ohohmiao.modules.workflow.model.dto.FlowNodeBindAddOrEditDTO;
import com.ohohmiao.modules.workflow.model.entity.FlowNodeBind;

/**
 * 流程环节绑定信息Service
 *
 * @author ohohmiao
 * @date 2025-01-16 10:01
 */
public interface FlowNodeBindService extends CommonService<FlowNodeBind> {

    /**
     * 新增环节绑定信息
     * @param addOrEditDTO
     */
    void save(FlowNodeBindAddOrEditDTO addOrEditDTO);

    /**
     * 删除环节绑定信息
     * @param bindType
     * @param defCode
     * @param defVersion
     * @param bindObjid
     */
    void delete(Integer bindType, String defCode, Integer defVersion, String bindObjid);

    /**
     * 删除环节绑定信息
     * @param bindType
     * @param bindObjid
     */
    void deleteByBindTypeAndBindObjid(Integer bindType, String... bindObjid);

}
