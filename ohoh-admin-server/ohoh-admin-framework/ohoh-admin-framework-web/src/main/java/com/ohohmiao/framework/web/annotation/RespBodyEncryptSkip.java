package com.ohohmiao.framework.web.annotation;

import java.lang.annotation.*;

/**
 * 跳过响应报文加密
 *
 * @author Bryce Zhang
 * @date 2023-09-12 9:41
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RespBodyEncryptSkip {
}
