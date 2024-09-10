package com.ohohmiao.web.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.UUID;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.code.kaptcha.Producer;
import com.ohohmiao.framework.common.enums.CommonCacheKeyEnum;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.util.PlatRedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 验证码controller
 *
 * @author ohohmiao
 * @date 2024-09-04 18:29
 */
@Api(tags = "验证码")
@ApiSupport(order = 2)
@Slf4j
@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "mathCaptchaProducer")
    private Producer mathCaptchaProducer;

    @Resource
    private PlatRedisUtil platRedisUtil;

    /**
     * 获取验证码Base64数据
     * @return
     */
    @ApiOperation(value = "获取验证码Base64数据，不含前缀data:image/jpg;base64,")
    @GetMapping("/captchaImage")
    public CommonResp<Map<String, Object>> getCaptchaImage(){
        String uuid = UUID.randomUUID().toString(true);
        String verifyKey = CommonCacheKeyEnum.KAPTCHA_CODE.getKey() + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;
        if(System.currentTimeMillis() % 2 == 0){
            // 算术
            String capText = mathCaptchaProducer.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = mathCaptchaProducer.createImage(capStr);
        }else{
            // 字符
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        platRedisUtil.setCacheObject(verifyKey, code, CommonCacheKeyEnum.KAPTCHA_CODE.getTtl(), TimeUnit.SECONDS);

        FastByteArrayOutputStream os = null;
        String img = null;
        try {
            os = new FastByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            img = Base64.encode(os.toByteArray());
        } catch (IOException e){
            log.error(ExceptionUtil.stacktraceToString(e));
        } finally {
            if (os != null) {
                os.close();
            }
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("uuid", uuid);
        result.put("img", img);
        return CommonResp.data(result);
    }

}
