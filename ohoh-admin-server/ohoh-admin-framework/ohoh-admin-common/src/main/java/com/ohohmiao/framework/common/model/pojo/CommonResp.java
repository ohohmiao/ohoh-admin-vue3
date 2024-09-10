package com.ohohmiao.framework.common.model.pojo;

import com.alibaba.fastjson2.JSON;
import com.ohohmiao.framework.common.enums.CommonRespCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 返回数据
 *
 * @author ohohmiao
 * @date 2023-03-30 14:57
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResp<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public static <T> CommonResp<T> success(){
        return new CommonResp<T>(CommonRespCodeEnum.SUCCESS.getCode(), CommonRespCodeEnum.SUCCESS.getMsg(), null);
    }

    public static <T> CommonResp<T> success(String msg){
        return new CommonResp<T>(CommonRespCodeEnum.SUCCESS.getCode(), msg, null);
    }

    public static <T> CommonResp<T> data(T data){
        return new CommonResp<T>(CommonRespCodeEnum.SUCCESS.getCode(), CommonRespCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <T> CommonResp<T> success(String msg, T data){
        return new CommonResp<T>(CommonRespCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> CommonResp<T> error(){
        return new CommonResp<T>(CommonRespCodeEnum.ERROR500.getCode(), CommonRespCodeEnum.ERROR500.getMsg(), null);
    }

    public static<T> CommonResp<T> error(String msg){
        return new CommonResp<T>(CommonRespCodeEnum.ERROR500.getCode(), msg, null);
    }

    public static<T> CommonResp<T> get(int code, String msg, T data){
        return new CommonResp<T>(code, msg, data);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
