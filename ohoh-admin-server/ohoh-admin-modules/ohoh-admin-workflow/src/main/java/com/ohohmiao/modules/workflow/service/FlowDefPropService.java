package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import com.ohohmiao.modules.workflow.model.entity.FlowDefProp;

import java.util.List;

/**
 * 流程定义属性Service
 *
 * @author ohohmiao
 * @date 2025-05-27 17:14
 */
public interface FlowDefPropService extends IService<FlowDefProp> {

    /**
     * 根据属性类别、流程定义编码、版本号，删除
     * @param propType
     * @param defCode
     * @param defVersion
     */
    void delete(Integer propType, String defCode, Integer defVersion);

    /**
     * 更新属性
     * @param propType
     * @param defCode
     * @param defVersion
     * @param referResList
     */
    void saveOrUpdate(Integer propType, String defCode, Integer defVersion, List<CommonReferRes> referResList);

    /**
     * 查询属性
     * @param propType
     * @param defCode
     * @param defVersion
     * @return
     */
    List<CommonReferRes> list(Integer propType, String defCode, Integer defVersion);

}
