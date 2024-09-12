package com.ohohmiao.core;

import com.ohohmiao.BaseTestCase;
import com.ohohmiao.framework.common.util.PlatSmCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.annotation.Resource;

/**
 * @author ohohmiao
 * @date 2023-04-03 16:17
 */
@Slf4j
public class PlatSmCryptoTestCase extends BaseTestCase {

    @Resource
    private PlatSmCryptoUtil platSmCryptoUtil;

    @Test
    public void getSm2Encrypt(){
        log.info(platSmCryptoUtil.getSm2ServerEncrypt("哈哈"));
    }

    @Test
    public void getSm2Decrypt(){
        log.info(platSmCryptoUtil.getSm2ServerDecrypt(
                "0425805b5a4bd700246213f9dea38bd47887cecd2f0d912e0c836f2cfd1f9ba0206e6efffe4ef53762d16ebf2a49219c4e4c86f5a4f35e2bd788dd5b49725d5c0f5f43aac4bbca6e64d9357decd459a941b984336fdb12658da0c090bbe0a7c9bae0663f3483127125b2384a7e8847923da2f185e60cae1db7189792a4f07939013a6ec1e4f0f2e3"));
    }

    @Test
    public void getSm3Hash(){
        log.info(platSmCryptoUtil.getSm3Hash("123456"));
    }

    @Test
    public void getHMacSm3(){
        log.info(platSmCryptoUtil.getHMacSm3("测试123"));
    }

    @Test
    public void sm4Cbc(){
        //String msgString = "abcdef";
        String msgString = "嘻嘻ab12";
        String encryptData = platSmCryptoUtil.getSm4CbcEncrypt(msgString);
        log.info(String.format("encrypt string: %s", encryptData));
        String decryptStr = platSmCryptoUtil.getSm4CbcDecrypt(encryptData);
        log.info(String.format("decrypt string: %s", decryptStr));
    }

}
