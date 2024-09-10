package com.ohohmiao.web.service;

import com.ohohmiao.web.model.dto.AccountPasswordLoginDTO;

/**
 * 登录相关Service
 *
 * @author ohohmiao
 * @date 2023-04-04 12:47
 */
public interface AuthService {

    /**
     * 账号密码登录逻辑
     * @param accountPasswordLoginDTO 账号+密码
     * @param device 设备
     * @return token
     */
    String doLogin(AccountPasswordLoginDTO accountPasswordLoginDTO, String device);

}
