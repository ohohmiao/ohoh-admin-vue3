package com.ohohmiao.framework.security.listener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ohohmiao.framework.common.enums.CommonLogTypeEnum;
import com.ohohmiao.framework.common.model.event.AuthLogEvent;
import com.ohohmiao.framework.common.util.PlatAppUtil;
import com.ohohmiao.framework.common.util.PlatWebUtil;
import com.ohohmiao.framework.security.api.StpLoginUserApi;
import com.ohohmiao.framework.security.enums.AuthDeviceEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录监听器
 *
 * @author ohohmiao
 * @date 2023-08-04 17:14
 */
@Component
public class AuthListener implements SaTokenListener {

    @Resource(name = "loginUserApi")
    private StpLoginUserApi loginUserApi;

    /**
     * 登录
     * @param loginType 登录类型
     * @param loginId 用户id
     * @param tokenValue token值
     * @param loginModel 登录用户信息
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel){
        if(loginType.equals(AuthDeviceEnum.PC.getType())){
            AuthLogEvent logEvent = new AuthLogEvent();
            logEvent.setLogType(CommonLogTypeEnum.LOGIN.getType());
            logEvent.setLogName("用户登录");
            // 请求信息
            HttpServletRequest request = PlatAppUtil.getRequest();
            logEvent.setOperateIp(PlatWebUtil.getClientIp(request));
            logEvent.setOperateBrowser(PlatWebUtil.getClientBrowser(request));
            logEvent.setOperateOs(PlatWebUtil.getClientOs(request));
            logEvent.setRequestUri(StrUtil.sub(request.getRequestURI(), 0, 255));
            // 登录用户信息
            logEvent.setOperateUserid(Convert.toStr(loginId));
            logEvent.setOperateUser(Convert.toStr(loginModel.getExtra("name")));
            logEvent.setOperateOrgname(Convert.toStr(loginModel.getExtra("mainOrgName")));
            // 发布
            SpringUtil.getApplicationContext().publishEvent(logEvent);
        }else{
            //TODO 其他登录体系待实现
        }
    }

    /**
     * 登出
     * @param loginType 登录类型
     * @param loginId 用户id
     * @param tokenValue token值
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        if(loginType.equals(AuthDeviceEnum.PC.getType())){
            StpLoginUser sysLoginUser = loginUserApi.getById(Convert.toStr(loginId));
            if(ObjectUtil.isNotNull(sysLoginUser)){
                AuthLogEvent logEvent = new AuthLogEvent();
                logEvent.setLogType(CommonLogTypeEnum.LOGOUT.getType());
                logEvent.setLogName("用户登出");
                // 请求信息
                HttpServletRequest request = PlatAppUtil.getRequest();
                logEvent.setOperateIp(PlatWebUtil.getClientIp(request));
                logEvent.setOperateBrowser(PlatWebUtil.getClientBrowser(request));
                logEvent.setOperateOs(PlatWebUtil.getClientOs(request));
                logEvent.setRequestUri(StrUtil.sub(request.getRequestURI(), 0, 255));
                // 登录用户信息
                logEvent.setOperateUserid(Convert.toStr(loginId));
                logEvent.setOperateUser(sysLoginUser.getUserName());
                logEvent.setOperateOrgname(sysLoginUser.getMainOrgName());
                // 发布
                SpringUtil.getApplicationContext().publishEvent(logEvent);
            }
        }else{
            //TODO 其他登录体系待实现
        }
    }

    /**
     * 账号被踢下线
     * @param loginType 登录类型
     * @param loginId 用户id
     * @param tokenValue token值
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {

    }

    /**
     * 账号被顶下线
     * @param loginType 登录类型
     * @param loginId 用户id
     * @param tokenValue token值
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    /**
     * 账号禁用
     * @param loginType 登录类型
     * @param loginId 用户id
     * @param tokenValue token值
     * @param level
     * @param disableTime
     */
    @Override
    public void doDisable(String loginType, Object loginId, String tokenValue, int level, long disableTime) {

    }

    /**
     * 账号解封
     * @param loginType 登录类型
     * @param loginId 用户id
     * @param service
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {

    }

    @Override
    public void doOpenSafe(String s, String s1, String s2, long l) {

    }

    @Override
    public void doCloseSafe(String s, String s1, String s2) {

    }

    /**
     * 创建session
     * @param id
     */
    @Override
    public void doCreateSession(String id) {

    }

    /**
     * 注销session
     * @param id
     */
    @Override
    public void doLogoutSession(String id) {

    }

    /**
     * token续签
     * @param tokenValue
     * @param loginId
     * @param timeout
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }

}
