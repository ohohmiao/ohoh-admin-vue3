package com.ohohmiao.web.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ohohmiao.framework.common.enums.CommonRespCodeEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.util.PlatAppUtil;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.common.util.PlatSmCryptoUtil;
import com.ohohmiao.framework.security.api.StpLoginUserApi;
import com.ohohmiao.framework.security.enums.AuthCacheKeyEnum;
import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.security.enums.AuthDeviceEnum;
import com.ohohmiao.framework.security.enums.AuthExceptionEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.modules.system.enums.SysUserStatusEnum;
import com.ohohmiao.modules.system.model.entity.SysUser;
import com.ohohmiao.modules.system.model.vo.SysLoginUser;
import com.ohohmiao.modules.system.service.SysUserService;
import com.ohohmiao.web.model.dto.AccountPasswordLoginDTO;
import com.ohohmiao.web.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关Service实现类
 *
 * @author ohohmiao
 * @date 2023-04-04 12:47
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Resource(name = "loginUserApi")
    private StpLoginUserApi loginUserApi;

    @Resource
    private PlatSmCryptoUtil platSmCryptoUtil;

    @Resource
    private PlatRedisUtil platRedisUtil;

    @Resource
    private SysUserService sysUserService;

    @Override
    public String doLogin(AccountPasswordLoginDTO accountPasswordLoginDTO, String device){
        String account = accountPasswordLoginDTO.getUsername();
        String password = accountPasswordLoginDTO.getPassword();
        AuthDeviceEnum authDeviceEnum = AuthDeviceEnum.assemble(device);
        if(ObjectUtil.isNull(authDeviceEnum)){
            authDeviceEnum = AuthDeviceEnum.PC;
        }

        // 密码sm2加密传输，sm3摘要存储
        String hashedPassword;
        try{
            String decryptPassword = platSmCryptoUtil.getSm2ClientDecrypt(password);
            hashedPassword = platSmCryptoUtil.getSm3Hash(decryptPassword);
        }catch (Exception e){
            log.error(ExceptionUtil.stacktraceToString(e));
            throw new CommonException(CommonRespCodeEnum.ERROR401.getCode(),
                    AuthExceptionEnum.ACCOUNTORPASS_ERROR.getMsg());
        }

        switch (authDeviceEnum){
        case PC:
            SysUser sysUser = sysUserService.getOne(
                    new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserAccount, account));
            if(ObjectUtil.isNull(sysUser)){
                throw new CommonException(CommonRespCodeEnum.ERROR401.getCode(),
                        AuthExceptionEnum.ACCOUNTORPASS_ERROR.getMsg());
            }
            // 多次密码输入错误，冻结账号
            String loginFailCacheKey = StrUtil.format("{}:{}", AuthCacheKeyEnum.USER_LOGIN_FAILTIMES.getKey(), account);
            Integer loginFailCount = platRedisUtil.getCacheObject(loginFailCacheKey);
            if(ObjectUtil.isNull(loginFailCount)){
                loginFailCount = 0;
            }
            if(loginFailCount >= 3){
                throw new CommonException(CommonRespCodeEnum.ERROR401.getCode(),
                        StrUtil.format("账号已冻结！{}秒后解冻", platRedisUtil.getExpire(loginFailCacheKey)));
            }
            // 密码输入错误
            if(!sysUser.getUserPassword().equals(hashedPassword)){
                platRedisUtil.setCacheObject(loginFailCacheKey, loginFailCount+1,
                        AuthCacheKeyEnum.USER_LOGIN_FAILTIMES.getTtl(), TimeUnit.SECONDS);
                throw new CommonException(CommonRespCodeEnum.ERROR401.getCode(),
                        AuthExceptionEnum.ACCOUNTORPASS_ERROR.getMsg());
            }
            platRedisUtil.deleteCacheObject(loginFailCacheKey);
            // 账号禁用情况
            if(sysUser.getUserStatus() != SysUserStatusEnum.ENABLE.getStatus()){
                throw new CommonException(CommonRespCodeEnum.ERROR401.getCode(),
                        AuthExceptionEnum.ACCOUNT_DISBALED.getMsg());
            }
            return this.doPcLogin(sysUser);
        default:
            //TODO 其他登录体系待实现
            return null;
        }
    }

    /**
     * PC端登录逻辑
     * @param sysUser 系统用户
     * @return token
     */
    private String doPcLogin(SysUser sysUser){
        // 记录账号登录信息
        LambdaUpdateWrapper<SysUser> updateLoginInfoWrapper = new LambdaUpdateWrapper<>();
        updateLoginInfoWrapper.eq(SysUser::getUserId, sysUser.getUserId());
        String requestIp = ServletUtil.getClientIP(PlatAppUtil.getRequest(), null);
        updateLoginInfoWrapper.set(SysUser::getLastLoginip, requestIp);
        updateLoginInfoWrapper.set(SysUser::getLastLogintime, new Date());
        sysUserService.update(updateLoginInfoWrapper);
        // 组装组织和授权信息
        StpLoginUser loginUser = BeanUtil.copyProperties(sysUser, SysLoginUser.class);
        loginUserApi.assembleOrgAndGrantInfo(loginUser, true);

        // 创建指定账号id的登录会话，此方法不会将 Token 注入到上下文
        StpPCUtil.login(loginUser.getUserId(),
            new SaLoginModel()
                .setDevice(AuthDeviceEnum.PC.getType())
                .setExtra("name", loginUser.getUserName())
                .setExtra("mainOrgName", loginUser.getMainOrgName()));

        // 获取当前会话的Session，写入登录用户信息
        StpPCUtil.getTokenSession().set(AuthConstEnum.LOGINUSER_SESSIONKEY.getValue(), loginUser);

        // 返回token
        return StpPCUtil.getTokenInfo().tokenValue;
    }

}
