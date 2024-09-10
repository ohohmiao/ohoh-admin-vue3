package com.ohohmiao.web.controller;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.enums.CommonCacheKeyEnum;
import com.ohohmiao.framework.common.enums.CommonRespCodeEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import com.ohohmiao.framework.security.annotation.SaPcCheckLogin;
import com.ohohmiao.framework.security.enums.AuthDeviceEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.web.model.dto.AccountPasswordLoginDTO;
import com.ohohmiao.web.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * pc端登录相关controller
 *
 * @author ohohmiao
 * @date 2023-04-04 11:35
 */
@Api(tags = "登录授权")
@ApiSupport(order = 1)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @Resource
    private PlatRedisUtil platRedisUtil;

    /**
     * PC登录
     * @param accountPasswordLoginDTO 登录dto
     * @return
     */
    @ApiOperation(value = "PC登录")
    @ApiOperationSupport(order = 1)
    @PostMapping("/login")
    public CommonResp<String> login(@RequestBody @Validated AccountPasswordLoginDTO accountPasswordLoginDTO){
        // 判断验证码是否通过
        String uuid = accountPasswordLoginDTO.getUuid();
        String code = accountPasswordLoginDTO.getCode();
        String verifyKey = CommonCacheKeyEnum.KAPTCHA_CODE.getKey() + uuid;
        String cachedCode = platRedisUtil.getCacheObject(verifyKey);
        if(StrUtil.isBlank(cachedCode)){
            throw new CommonException(CommonRespCodeEnum.ERROR400.getCode(), "验证码已过期");
        }
        // 防止验证码重试
        platRedisUtil.deleteCacheObject(verifyKey);
        if(!code.equalsIgnoreCase(cachedCode)){
            throw new CommonException(CommonRespCodeEnum.ERROR400.getCode(), "验证码输入有误");
        }
        return CommonResp.data(authService.doLogin(accountPasswordLoginDTO, AuthDeviceEnum.PC.getType()));
    }

    /**
     * 获取登录用户信息
     * @return
     */
    @ApiOperation(value = "获取登录用户信息")
    @ApiOperationSupport(order = 2)
    @GetMapping("/getLoginUser")
    public CommonResp<StpLoginUser> getLoginUser(){
        return CommonResp.data(StpPCUtil.getLoginUser());
    }

    /**
     * 注销登录
     * @return
     */
    @ApiOperation(value = "注销PC登录")
    @ApiOperationSupport(order = 3)
    @SaPcCheckLogin
    @PostMapping("/logout")
    public CommonResp<String> logout() {
        StpPCUtil.logout();
        return CommonResp.success();
    }

}
