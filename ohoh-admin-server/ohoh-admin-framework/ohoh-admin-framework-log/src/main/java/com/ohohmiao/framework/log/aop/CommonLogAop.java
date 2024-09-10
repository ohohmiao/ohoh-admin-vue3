package com.ohohmiao.framework.log.aop;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ohohmiao.framework.common.util.PlatAppUtil;
import com.ohohmiao.framework.common.util.PlatWebUtil;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.enums.CommonLogTypeEnum;
import com.ohohmiao.framework.common.model.event.OperateLogEvent;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 系统日志切面
 *
 * @author ohohmiao
 * @date 2023-08-04 11:16
 */
@Slf4j
@Aspect
@Component
public class CommonLogAop {

    /**
     * 需排除的敏感字段
     */
    public static final String[] EXCLUDE_FIELDS = {"password"};

    /**
     * 计时key
     */
    private static final ThreadLocal<StopWatch> KEY_CACHE = new ThreadLocal<StopWatch>();

    /**
     * 处理请求前执行
     * @param joinPoint 切点
     * @param commonLog 日志注解
     */
    @Before("@annotation(commonLog)")
    public void doBeforeAdvice(JoinPoint joinPoint, CommonLog commonLog) {
        StopWatch stopWatch = new StopWatch();
        KEY_CACHE.set(stopWatch);
        stopWatch.start();
    }

    /**
     * 处理完请求执行
     * @param joinPoint 切点
     * @param commonLog 日志注解
     * @param jsonResult 结果对象
     */
    @AfterReturning(pointcut = "@annotation(commonLog)", returning = "jsonResult")
    public void doAfterReturningAdvice(JoinPoint joinPoint, CommonLog commonLog, Object jsonResult) {
        handleLog(joinPoint, commonLog, null, jsonResult);
    }

    /**
     * 执行异常拦截
     * @param joinPoint 切点
     * @param commonLog 日志注解
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(commonLog)", throwing = "e")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, CommonLog commonLog, Exception e) {
        handleLog(joinPoint, commonLog, e, null);
    }

    /**
     * 处理日志
     * @param joinPoint 切点
     * @param commonLog 日志注解
     * @param e 异常
     * @param jsonResult 结果对象
     */
    protected void handleLog(JoinPoint joinPoint, CommonLog commonLog, Exception e, Object jsonResult) {
        try {
            OperateLogEvent logEvent = new OperateLogEvent();
            // 日志类型
            if(e != null){
                logEvent.setLogType(CommonLogTypeEnum.EXCEPTION.getType());
                logEvent.setErrorMsg(ExceptionUtil.stacktraceToString(e, Integer.MAX_VALUE));
            }else{
                logEvent.setLogType(CommonLogTypeEnum.OPERATION.getType());
            }
            // 日志名称
            logEvent.setLogName(commonLog.value());
            // 请求信息
            HttpServletRequest request = PlatAppUtil.getRequest();
            logEvent.setOperateIp(PlatWebUtil.getClientIp(request));
            logEvent.setOperateBrowser(PlatWebUtil.getClientBrowser(request));
            logEvent.setOperateOs(PlatWebUtil.getClientOs(request));
            logEvent.setRequestUri(StrUtil.sub(request.getRequestURI(), 0, 255));
            // 请求及响应
            if (commonLog.isSaveRequestData()) {
                setRequestValue(joinPoint, logEvent, commonLog.excludeParamNames());
            }
            if (commonLog.isSaveResponseData()) {
                logEvent.setResultJson(JSON.toJSONString(jsonResult));
            }
            // 登录用户信息
            StpLoginUser loginUser = StpPCUtil.getLoginUser();
            if(ObjectUtil.isNotNull(loginUser)){
                logEvent.setOperateUserid(loginUser.getUserId());
                logEvent.setOperateUser(loginUser.getUserName());
                logEvent.setOperateOrgname(loginUser.getMainOrgName());
            }
            // 耗时
            StopWatch stopWatch = KEY_CACHE.get();
            stopWatch.stop();
            logEvent.setCostTime(stopWatch.getTime());
            // 发布
            SpringUtil.getApplicationContext().publishEvent(logEvent);
        } catch (Exception ex) {
            log.error(ExceptionUtil.stacktraceToString(ex));
        } finally {
            KEY_CACHE.remove();
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param logEvent 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, OperateLogEvent logEvent, String[] excludeParamNames) throws Exception {
        HttpServletRequest request = PlatAppUtil.getRequest();
        Map<String, String> paramsMap = PlatAppUtil.getParamMap(request);

        String requestMethod = request.getMethod();
        if (MapUtil.isEmpty(paramsMap)
                && HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            logEvent.setParamJson(params);
        } else {
            MapUtil.removeAny(paramsMap, EXCLUDE_FIELDS);
            MapUtil.removeAny(paramsMap, excludeParamNames);
            logEvent.setParamJson(JSON.toJSONString(paramsMap));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames) {
        StringJoiner params = new StringJoiner(" ");
        if (ArrayUtil.isEmpty(paramsArray)) {
            return params.toString();
        }
        for (Object o : paramsArray) {
            if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                String str = JSON.toJSONString(o);
                Dict dict = parseDict(str);
                if (MapUtil.isNotEmpty(dict)) {
                    MapUtil.removeAny(dict, EXCLUDE_FIELDS);
                    MapUtil.removeAny(dict, excludeParamNames);
                    str = JSON.toJSONString(dict);
                }
                params.add(str);
            }
        }
        return params.toString();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return MultipartFile.class.isAssignableFrom(clazz.getComponentType());
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.values()) {
                return value instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest
                || o instanceof HttpServletResponse || o instanceof BindingResult;
    }

    /**
     * 将JSON格式的字符串转换为Dict对象
     *
     * @param text JSON格式的字符串
     * @return 转换后的Dict对象，如果字符串为空或者不是JSON格式则返回null
     * @throws RuntimeException 如果转换过程中发生IO异常，则抛出运行时异常
     */
    public static Dict parseDict(String text) {
        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
        if (StrUtil.isBlank(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, objectMapper.getTypeFactory().constructType(Dict.class));
        } catch (MismatchedInputException e) {
            // 类型不匹配说明不是json
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
