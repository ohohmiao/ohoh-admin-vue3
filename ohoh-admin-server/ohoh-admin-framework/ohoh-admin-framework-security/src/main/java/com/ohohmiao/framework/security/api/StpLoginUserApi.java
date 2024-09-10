package com.ohohmiao.framework.security.api;

import com.ohohmiao.framework.security.model.pojo.StpLoginUser;

/**
 * 登录用户api
 *
 * @author ohohmiao
 * @date 2023-05-31 11:08
 */
public interface StpLoginUserApi {

    /**
     * 组装组织信息和授权信息
     * @param loginUser 用户
     * @param includeGrantInfo 是否包含授权信息
     */
    void assembleOrgAndGrantInfo(StpLoginUser loginUser, boolean includeGrantInfo);

    /**
     * 根据id，获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    StpLoginUser getById(String userId);

}
