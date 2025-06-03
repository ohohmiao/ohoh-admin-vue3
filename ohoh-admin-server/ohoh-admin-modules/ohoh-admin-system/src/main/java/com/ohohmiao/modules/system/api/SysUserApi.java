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

}
