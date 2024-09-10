package com.ohohmiao.framework.security.util;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.*;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.enums.CommonRespCodeEnum;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import lombok.extern.slf4j.Slf4j;

/**
 * sa-token相关异常处理
 *
 * @author ohohmiao
 * @date 2023-04-04 15:17
 */
@Slf4j
public class AuthExceptionUtil {

    /**
     * 获取sa-token异常
     * @param e 异常
     * @return
     */
    public static CommonResp<String> getCommonResult(Exception e) {
        CommonResp<String> commonResp = null;

        if (e instanceof NotLoginException) {
            // 未登录
            NotLoginException notLoginException = (NotLoginException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR401.getCode(), notLoginException.getMessage(), null);
        } else if (e instanceof NotRoleException) {
            // 角色异常
            NotRoleException notRoleException = (NotRoleException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR403.getCode(),
                    StrUtil.format("禁止访问！缺少角色：{}", notRoleException.getRole()), null);
        } else if (e instanceof NotPermissionException) {
            // 权限异常
            NotPermissionException notPermissionException = (NotPermissionException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR403.getCode(),
                    StrUtil.format("禁止访问！缺少权限：{}", notPermissionException.getPermission()), null);
        } else if (e instanceof DisableServiceException) {
            // 账号被冻结
            DisableServiceException disableServiceException = (DisableServiceException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR403.getCode(),
                    StrUtil.format("账号已冻结！{}秒后解冻", disableServiceException.getDisableTime()), null);
        } else if (e instanceof SaTokenException) {
            // SaToken异常
            SaTokenException saTokenException = (SaTokenException) e;
            commonResp = CommonResp.error(saTokenException.getMessage());
        } else {
            //其他
            //Never Happen
            log.error(ExceptionUtil.stacktraceToString(e));
            commonResp = CommonResp.error(CommonRespCodeEnum.ERROR500.getMsg());
        }
        log.error(">>> 请求地址：{}，{}", SaHolder.getRequest().getUrl(), commonResp.getMsg());

        return commonResp;
    }

}
