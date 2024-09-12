package com.ohohmiao.core;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.ohohmiao.BaseTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author ohohmiao
 * @date 2023-04-04 14:01
 */
@Slf4j
public class HutoolTestCase extends BaseTestCase {

    @Test
    public void exception(){
        try{
            int i = 1/0;
        }catch (Exception e){
            log.info(ExceptionUtil.stacktraceToString(e));;
        }
    }

    @Test
    public void strUtil(){
        log.info(StrUtil.format("{}爱{}{}万年", "我", "你", 1));
    }

    @Test
    public void sm2() throws UnsupportedEncodingException {
//        私钥加00
//        因为这个是java的锅,java中BigInteger转换byte[]占用最低高位来表示符号,
//        所以私钥一单没法用32Byte表示就要出现33Byte现象.谨记私钥d是一个大正整数

//        密文加0x04
//        这个是标识,一般会在工具类封装

//        公钥加0x04,小部分是0x02等
//        主要是因为公钥有很多分类,0x04代表未压缩的,也就是64Byte,对接某些C类语言不用,具体见实现

        String srcString = "hello world";
        byte[] privateKey = HexUtil.decodeHex("2ac01b3e5780e9fb239e5c2c3d138aea2cd838badcf6ceda8b6f81297dd9d3d8");
        byte[] publicKey = HexUtil.decodeHex("04e208d21116fbbdbcfadd5d75fd16163693d0b387e3ceb54bedbe4332bc002b913bde5d025b01055db4109b0708aef06396e9b1fb4cd79004afe171d741227ed7");
        SM2 sm2 = SmUtil.sm2(privateKey, publicKey);

        byte[] encryptedData = sm2.encrypt(srcString.getBytes(), KeyType.PublicKey);
        log.info(HexUtil.encodeHexStr(encryptedData));
        byte[] decyptedData = sm2.decrypt(encryptedData, KeyType.PrivateKey);
        log.info(new String(decyptedData, StandardCharsets.UTF_8));

        String encryptedString2 = "04086b6fec0a7884456a14bdfa88a6350597c94262a8334e0669654d18c5722601aadc790798bfc36a61ed53602c395eef9db8796bf055df7ff613e5eabbf256f7841c0847a950b872e49878142670a093e5eca1883c3c9a48a35acee6ce8c48f826df77acde81";
        String decyptedString2 = sm2.decryptStr(encryptedString2, KeyType.PrivateKey);
        log.info(decyptedString2);
    }

    @Test
    public void sm3(){
        String srcString = "hello world";
        String sm3Hash = SmUtil.sm3(srcString);
        log.info(sm3Hash);
    }

    @Test
    public void sm4(){
        String srcString = "hello world";
        byte[] secretKey = HexUtil.decodeHex("0123456789abcdeffedcba9876543210");
        byte[] iv = HexUtil.decodeHex("fedcba98765432100123456789abcdef");

        SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, secretKey, iv);
        sm4.setIv(iv);
        String encryptedString = sm4.encryptHex(srcString, StandardCharsets.UTF_8);
        log.info(encryptedString);

        String decryptedString = sm4.decryptStr(encryptedString, StandardCharsets.UTF_8);
        log.info(decryptedString);
    }

}
