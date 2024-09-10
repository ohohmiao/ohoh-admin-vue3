package com.ohohmiao.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用响应码枚举
 *
 * @author ohohmiao
 * @date 2023-03-30 15:39
 */
@Getter
@AllArgsConstructor
public enum CommonRespCodeEnum {

    SUCCESS(200, "请求成功"),

    ERROR400(400, "请求参数错误"),

    ERROR401(401, "未登录"),

    ERROR403(403, "禁止访问"),

    ERROR404(404, "资源不存在"),

    ERROR405(405, "禁止的请求方法"),

    ERROR415(415, "媒体类型不支持"),

    ERROR500(500, "程序内部出错");

    /**
     * 状态码
     */
    private int code;

    /**
     * 信息
     */
    private String msg;

}
