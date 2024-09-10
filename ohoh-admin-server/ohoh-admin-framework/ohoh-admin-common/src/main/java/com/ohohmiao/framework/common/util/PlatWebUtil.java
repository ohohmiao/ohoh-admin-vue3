package com.ohohmiao.framework.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络相关工具类
 *
 * @author ohohmiao
 * @date 2023-08-04 15:08
 */
@Slf4j
public class PlatWebUtil {

    /**
     * 获取客户端ip
     * @param request HttpServletRequest
     * @return 客户端ip
     */
    public static String getClientIp(HttpServletRequest request){
        if(ObjectUtil.isNull(request)){
            return Ipv4Util.LOCAL_IP;
        }else{
            try{
                String clientIp = ServletUtil.getClientIP(request);
                return "0:0:0:0:0:0:0:1".equals(clientIp)? Ipv4Util.LOCAL_IP: clientIp;
            }catch (Exception e){
                log.error(ExceptionUtil.stacktraceToString(e));
                return Ipv4Util.LOCAL_IP;
            }
        }
    }

    /**
     * 获取客户端浏览器
     * @param request HttpServletRequest
     * @return 客户端浏览器
     */
    public static String getClientBrowser(HttpServletRequest request){
        UserAgent userAgent = getUserAgent(request);
        if(ObjectUtil.isNull(userAgent)){
            return StrUtil.DASHED;
        }else{
            String browser = userAgent.getBrowser().toString();
            return "Unknown".equalsIgnoreCase(browser)? StrUtil.DASHED: browser;
        }
    }

    /**
     * 获取客户端操作系统
     * @param request HttpServletRequest
     * @return 客户端操作系统
     */
    public static String getClientOs(HttpServletRequest request){
        UserAgent userAgent = getUserAgent(request);
        if(ObjectUtil.isNull(userAgent)){
            return StrUtil.DASHED;
        }else{
            String os = userAgent.getOs().toString();
            return "Unknown".equalsIgnoreCase(os)? StrUtil.DASHED: os;
        }
    }

    /**
     * 获取请求代理头
     * @param request HttpServletRequest
     * @return 请求代理头
     */
    private static UserAgent getUserAgent(HttpServletRequest request){
        String userAgentStr = ServletUtil.getHeaderIgnoreCase(request, "User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        if(ObjectUtil.isNotEmpty(userAgentStr)){
            if("Unknown".equalsIgnoreCase(userAgent.getBrowser().getName())){
                userAgent.setBrowser(new Browser(userAgentStr, null, ""));
            }
        }
        return userAgent;
    }

}
