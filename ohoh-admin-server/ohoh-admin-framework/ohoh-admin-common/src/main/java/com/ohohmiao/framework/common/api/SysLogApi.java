package com.ohohmiao.framework.common.api;

/**
 * 系统日志api
 *
 * @author ohohmiao
 * @date 2023-08-04 17:01
 */
public interface SysLogApi {

    /**
     * 保存登录日志
     * @param userId 用户id
     * @param userName 用户姓名
     */
    void saveLoginLog(String userId, String userName);

    /**
     * 保存登出日志
     * @param userId 用户id
     * @param userName 用户姓名
     */
    void saveLogoutLog(String userId, String userName);

}
