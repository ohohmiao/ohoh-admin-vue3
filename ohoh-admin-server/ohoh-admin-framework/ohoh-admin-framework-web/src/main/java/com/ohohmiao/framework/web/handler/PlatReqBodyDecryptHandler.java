package com.ohohmiao.framework.web.handler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.ohohmiao.framework.common.prop.PlatProps;
import com.ohohmiao.framework.common.util.PlatSmCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 请求报文解密处理器
 *
 * @author ohohmiao
 * @date 2023-08-31 9:45
 */
@Slf4j
@ControllerAdvice
public class PlatReqBodyDecryptHandler extends RequestBodyAdviceAdapter {

    @Resource
    private PlatSmCryptoUtil platSmCryptoUtil;

    @Resource
    private PlatProps platProps;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return platProps.getEncryptTransportSwitch();
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try (InputStream inputStream = inputMessage.getBody()) {
            byte[] inputMessageBody = new byte[inputStream.available()];
            inputStream.read(inputMessageBody);
            String encryptString = new String(Base64.decode(inputMessageBody));
            String decryptString = platSmCryptoUtil.getSm2ClientDecrypt(encryptString);
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() {
                    return new ByteArrayInputStream(decryptString.getBytes());
                }
                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }

}
