package com.ohohmiao.framework.common.exception;

import cn.hutool.core.util.StrUtil;
import com.ohohmiao.framework.common.enums.CommonRespCodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 通用异常
 *
 * @author ohohmiao
 * @date 2023/4/1 18:10
 */
@Getter
@Setter
public class CommonException extends RuntimeException {

    private int code;

    private String msg;

    public CommonException(){
        super(CommonRespCodeEnum.ERROR500.getMsg());
        this.code = CommonRespCodeEnum.ERROR500.getCode();
        this.msg = CommonRespCodeEnum.ERROR500.getMsg();
    }

    public CommonException(String msg, Object... args){
        super(StrUtil.format(msg, args));
        this.code = CommonRespCodeEnum.ERROR500.getCode();
        this.msg = StrUtil.format(msg, args);
    }

    public CommonException(int code, String msg, Object... args){
        super(StrUtil.format(msg, args));
        this.code = code;
        this.msg = msg;
    }

}
