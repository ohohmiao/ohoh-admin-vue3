package com.ohohmiao.framework.common.util;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.ohohmiao.framework.common.prop.PlatProps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 国密算法工具类
 *
 * https://www.cnblogs.com/lylhqy/p/15693757.html
 * 私钥加00
 * 因为这个是java的锅,java中BigInteger转换byte[]占用最低高位来表示符号,
 * 所以私钥一单没法用32Byte表示就要出现33Byte现象.谨记私钥d是一个大正整数
 * 密文加0x04
 * 这个是标识,一般会在工具类封装
 * 公钥加0x04,小部分是0x02等
 * 主要是因为公钥有很多分类,0x04代表未压缩的,也就是64Byte,对接某些C类语言不用,具体见实现
 *
 * @author ohohmiao
 * @date 2023-04-03 15:01
 */
@Component
public class PlatSmCryptoUtil {

    @Resource
    private PlatProps platProps;

    /**
     * sm2加密-服务端
     * @param msgString 原文串
     * @return 加密串
     */
    public String getSm2ServerEncrypt(String msgString){
        SM2 sm2 = SmUtil.sm2(platProps.getSmServerPrivatekey(), platProps.getSmServerPublickey());
        return sm2.encryptHex(msgString.getBytes(), KeyType.PublicKey);
    }

    /**
     * sm2解密-服务端
     * @param encryptData 加密串
     * @return 原文串
     */
    public String getSm2ServerDecrypt(String encryptData){
        SM2 sm2 = SmUtil.sm2(platProps.getSmServerPrivatekey(), platProps.getSmServerPublickey());
        return sm2.decryptStr(encryptData, KeyType.PrivateKey);
    }

    /**
     * sm2加密-客户端
     * @param msgString 原文串
     * @return 加密串
     */
    public String getSm2ClientEncrypt(String msgString){
        SM2 sm2 = SmUtil.sm2(platProps.getSmClientPrivatekey(), platProps.getSmClientPublickey());
        return sm2.encryptHex(msgString.getBytes(), KeyType.PublicKey);
    }

    /**
     * sm2解密-客户端
     * @param encryptData 加密串
     * @return 原文串
     */
    public String getSm2ClientDecrypt(String encryptData){
        SM2 sm2 = SmUtil.sm2(platProps.getSmClientPrivatekey(), platProps.getSmClientPublickey());
        return sm2.decryptStr(encryptData, KeyType.PrivateKey);
    }

    /**
     * sm2加密
     * @param msgString 原文串
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return 加密串
     */
    public String getSm2Encrypt(String msgString, String publicKey, String privateKey){
        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        return sm2.encryptHex(msgString.getBytes(), KeyType.PublicKey);
    }

    /**
     * sm2解密
     * @param encryptData 加密串
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return 原文串
     */
    public String getSm2Decrypt(String encryptData, String publicKey, String privateKey){
        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        return sm2.decryptStr(encryptData, KeyType.PrivateKey);
    }

    /**
     * sm3摘要
     * @param msgString 原文串
     * @return 摘要
     */
    public String getSm3Hash(String msgString){
        return SmUtil.sm3(msgString);
    }

    /**
     * Hmac-Sm3摘要
     * @param msgString 原文串
     * @return 摘要
     */
    public String getHMacSm3(String msgString) {
        HMac hMac = new HMac(HmacAlgorithm.HmacSM3, platProps.getSmSecretKey().getBytes());
        return hMac.digestHex(msgString);
    }

    /**
     * 加密，cbc 模式，输出16进制字符串
     * @param msgString 原文串
     * @return 加密串
     */
    public String getSm4CbcEncrypt(String msgString){
        byte[] secretKey = HexUtil.decodeHex(platProps.getSmSecretKey());
        byte[] iv = HexUtil.decodeHex(platProps.getSmIv());
        SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, secretKey, iv);
        sm4.setIv(iv);
        return sm4.encryptHex(msgString, StandardCharsets.UTF_8);
    }

    /**
     * 解密，cbc 模式，输出 utf8 字符串
     * @param encryptData 加密串
     * @return 原文串
     */
    public String getSm4CbcDecrypt(String encryptData){
        byte[] secretKey = HexUtil.decodeHex(platProps.getSmSecretKey());
        byte[] iv = HexUtil.decodeHex(platProps.getSmIv());
        SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, secretKey, iv);
        sm4.setIv(iv);
        return sm4.decryptStr(encryptData, StandardCharsets.UTF_8);
    }

}
