package com.ohohmiao.framework.web.util;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ohohmiao.framework.common.enums.CommonRespCodeEnum;
import com.ohohmiao.framework.common.exception.CommonException;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.util.PlatAppUtil;
import com.ohohmiao.framework.security.util.AuthExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 异常处理工具
 *
 * @author ohohmiao
 * @date 2023-04-04 12:55
 */
@Slf4j
public class PlatExceptionUtil {

    /**
     * 将异常转换成本平台响应对象
     * @param e
     * @return
     */
    public static CommonResp<String> getCommonResult(Exception e){
        CommonResp<String> commonResp = null;

        if (e instanceof HttpRequestMethodNotSupportedException) {
            // 请求方法不支持异常
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR405.getCode(), CommonRespCodeEnum.ERROR405.getMsg(), null);
        } else if (e instanceof HttpMessageNotReadableException || e instanceof HttpMediaTypeNotSupportedException) {
            // 入参格式异常
            log.error(ExceptionUtil.stacktraceToString(e));
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR415.getCode(), CommonRespCodeEnum.ERROR415.getMsg(), null);
        } else if (e instanceof MethodArgumentNotValidException) {
            // 参数校验异常MethodArgumentNotValidException
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR415.getCode(),
                    getArgNotValidMessage(methodArgumentNotValidException.getBindingResult()), null);
        } else if (e instanceof BindException) {
            // 参数校验异常BindException
            BindException bindException = (BindException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR415.getCode(),
                    getArgNotValidMessage(bindException.getBindingResult()), null);
        } else if (e instanceof ConstraintViolationException) {
            // 参数校验异常ConstraintViolationException
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR415.getCode(),
                    getArgNotValidMessage(constraintViolationException.getConstraintViolations()), null);
        } else if (e instanceof MissingServletRequestParameterException) {
            // 参数校验异常MissingServletRequestParameterException
            MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) e;
            commonResp = CommonResp.get(CommonRespCodeEnum.ERROR415.getCode(),
                    missingServletRequestParameterException.getMessage(), null);
        } else if (e instanceof MultipartException) {
            // 文件上传表单
            commonResp = CommonResp.error("请使用multipart/form-data方式上传文件");
        } else if (e instanceof MissingServletRequestPartException) {
            // 文件上传错误
            commonResp = CommonResp.error("请选择要上传的文件，并检查文件参数名称是否正确");
        } else if(e instanceof MyBatisSystemException) {
            Throwable cause = e.getCause();
            if (cause instanceof PersistenceException) {
                Throwable secondCause = cause.getCause();
                if (secondCause instanceof CommonException) {
                    CommonException commonException = (CommonException) secondCause;
                    commonResp = CommonResp.get(commonException.getCode(), commonException.getMsg(), null);
                } else {
                    log.error(ExceptionUtil.stacktraceToString(e));
                    commonResp = CommonResp.error("数据操作异常");
                }
            } else {
                log.error(ExceptionUtil.stacktraceToString(e));
                commonResp = CommonResp.error("数据操作异常");
            }
        } else if (e instanceof SaTokenException) {
            // Sa-token相关异常
            return AuthExceptionUtil.getCommonResult(e);
        } else if (e instanceof CommonException) {
            // 通用业务异常，直接返回给前端
            CommonException commonException = (CommonException) e;
            commonResp = CommonResp.get(commonException.getCode(), commonException.getMsg(), null);
        }  else {
            // 其他
            log.error(ExceptionUtil.stacktraceToString(e));
            commonResp = CommonResp.error();
        }
        log.error(">>> 请求地址：{}，错误提示：{}", PlatAppUtil.getRequest().getRequestURL(), commonResp.getMsg());

        return commonResp;
    }

    /**
     * 获取请求参数不正确的提示信息
     * @param constraintViolationSet
     * @return
     */
    public static String getArgNotValidMessage(Set<ConstraintViolation<?>> constraintViolationSet) {
        if (ObjectUtil.isEmpty(constraintViolationSet)) {
            return "";
        }

        List<String> list = constraintViolationSet.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return String.join("，", list);
    }

    /**
     * 获取请求参数不正确的提示信息
     * @param bindingResult
     * @return
     */
    public static String getArgNotValidMessage(BindingResult bindingResult) {
        if (ObjectUtil.isNull(bindingResult)) {
            return "";
        }

        List<String> list = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return String.join("，", list);
    }

}
