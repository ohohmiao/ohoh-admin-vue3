package com.ohohmiao.modules.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohohmiao.framework.common.model.pojo.CommonReferRes;
import com.ohohmiao.modules.workflow.model.entity.FlowDefBind;

import java.util.List;

/**
 * 流程定义绑定信息Service
 *
 * @author ohohmiao
 * @date 2025-05-27 17:14
 */
public interface FlowDefBindService extends IService<FlowDefBind> {

    /**
     * 根据绑定类别、流程定义编码、版本号，删除
     * @param bindType
     * @param defCode
     * @param defVersion
     */
    void delete(Integer bindType, String defCode, Integer defVersion);

    /**
     * 更新绑定信息
     * @param bindType
     * @param defCode
     * @param defVersion
     * @param referResList
     * @param bindObjid
     */
    void saveOrUpdate(Integer bindType, String defCode, Integer defVersion,
                      List<CommonReferRes> referResList, String bindObjid);

    /**
     * 查询绑定信息
     * @param bindType
     * @param defCode
     * @param defVersion
     * @param bindObjid
     * @return
     */
    List<CommonReferRes> list(Integer bindType, String defCode, Integer defVersion, String bindObjid);

    /**
     * 根据绑定类别和关联主键id删除
     * @param bindType
     * @param bindObjid
     */
    void deleteByBindTypeAndBindObjid(Integer bindType, String... bindObjid);

}
