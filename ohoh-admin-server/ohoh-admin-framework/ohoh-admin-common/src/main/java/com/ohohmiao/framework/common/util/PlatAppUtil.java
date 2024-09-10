package com.ohohmiao.framework.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ohohmiao.framework.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用工具类
 *
 * @author ohohmiao
 * @date 2023-04-04 13:53
 */
@Slf4j
public class PlatAppUtil {

    /**
     * 获取request
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return servletRequestAttributes.getRequest();
        } catch (Exception e) {
            ExceptionUtil.stacktraceToString(e);
            throw new CommonException("当前非Web上下文，无法获取HttpServletRequest");
        }
    }

    /**
     * 获取response
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse(){
        try {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return servletRequestAttributes.getResponse();
        } catch (Exception e) {
            ExceptionUtil.stacktraceToString(e);
            throw new CommonException("当前非Web上下文，无法获取HttpServletResponse");
        }
    }

    /**
     * 获得所有请求参数
     * @param request
     * @return Map
     */
    public static Map<String, String[]> getParams(HttpServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     * 获得所有请求参数
     *
     * @param request
     * @return Map
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
            params.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
        }
        return params;
    }

    /**
     * 从请求中中获取参数
     * @param paramName 参数名
     * @return 参数值
     */
    public static String getParamFromRequest(String paramName) {
        HttpServletRequest request = getRequest();

        // 1. 尝试从请求体里面读取
        String paramValue = request.getParameter(paramName);

        // 2. 尝试从header里读取
        if (ObjectUtil.isEmpty(paramValue)) {
            paramValue = request.getHeader(paramName);
        }
        // 3. 尝试从cookie里读取
        if (ObjectUtil.isEmpty(paramValue)) {
            Cookie[] cookies = request.getCookies();
            if(ObjectUtil.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if (cookieName.equals(paramName)) {
                        return cookie.getValue();
                    }
                }
            }
        }
        // 4. 返回
        return paramValue;
    }

}
