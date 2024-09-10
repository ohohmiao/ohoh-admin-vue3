package com.ohohmiao.framework.web.handler;

import cn.hutool.core.codec.Base64;
import com.ohohmiao.framework.web.annotation.RespBodyEncryptSkip;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.prop.PlatProps;
import com.ohohmiao.framework.common.util.PlatSmCryptoUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 响应报文加密处理器
 *
 * @author ohohmiao
 * @date 2023-08-30 11:01
 */
@ControllerAdvice
public class PlatRespBodyEncryptHandler implements ResponseBodyAdvice {

    @Resource
    private PlatSmCryptoUtil platSmCryptoUtil;

    @Resource
    private PlatProps platProps;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return platProps.getEncryptTransportSwitch() && !returnType.hasMethodAnnotation(RespBodyEncryptSkip.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(body instanceof CommonResp){
            String encryptBodyString = platSmCryptoUtil.getSm2ServerEncrypt(body.toString());
            return Base64.encode(encryptBodyString);
        }else{
            return body;
        }
    }

}
