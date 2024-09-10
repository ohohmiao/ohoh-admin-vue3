package com.ohohmiao.framework.web.handler;

import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.web.util.PlatExceptionUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author ohohmiao
 * @date 2023-04-04 12:52
 */
@ControllerAdvice
public class PlatExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResp<String> handleException(Exception e){
        return PlatExceptionUtil.getCommonResult(e);
    }

}
