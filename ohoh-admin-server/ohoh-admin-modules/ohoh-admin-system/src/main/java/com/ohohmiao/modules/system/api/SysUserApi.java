package com.ohohmiao.modules.system.api;

import com.ohohmiao.modules.system.model.pojo.SysReferRes;
import com.ohohmiao.modules.system.model.vo.SysUserVO;

import java.util.List;

/**
 * 系统用户api
 *
 * @author ohohmiao
 * @date 2025-06-02 10:45
 */
public interface SysUserApi {

    /**
     * 根据关联资源，查找用户列表
     * @param referResList
     * @return
     */
    List<SysUserVO> listByReferRes(List<SysReferRes> referResList);

    /**
     * 从源用户中过滤出与目标组织同一组织的用户列表
     * @param sourceUserIdList
     * @param targetOrgId
     * @return
     */
    List<SysUserVO> doFilterBySameOrg(List<String> sourceUserIdList, String targetOrgId);

    /**
     * 从源用户中过滤出是目标组织上一级组织的用户列表
     * @param sourceUserIdList
     * @param targetOrgId
     * @return
     */
    List<SysUserVO> doFilterByOneLevelUpOrg(List<String> sourceUserIdList, String targetOrgId);

    /**
     * 从源用户中过滤出与目标组织同一或孩子组织的用户列表
     * @param sourceUserIdList
     * @param targetOrgId
     * @return
     */
    List<SysUserVO> doFilterBySameAndChildOrg(List<String> sourceUserIdList, String targetOrgId);

}
