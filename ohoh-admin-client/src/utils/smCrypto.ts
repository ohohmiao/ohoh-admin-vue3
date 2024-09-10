import smCrypto from "sm-crypto";

const sm2 = smCrypto.sm2;
const sm3 = smCrypto.sm3;
const sm4 = smCrypto.sm4;

const cipherMode = 1; // 1 - C1C3C2，0 - C1C2C3，默认为1
const clientPublicKey =
	"04bd279bf67d457dc28c7054dcddcc25d3ce864bf3494fca1c3b9d0ed22e822869b615c9b2fc115c16968e090a294a717ac95b2cd8e1f811f9990ede0529051f00";
const serverPrivateKey = "f922f0046c2141fffc693846e458d5ac8889b6838ba39bdd5dd6d8e3aec9a3a8";

const secretKey = "bc47520a5093d70878457be86aeaefce";
const iv = "a07ec0cb1531a7fc2661a72276eade8c";

export default {
	/**
	 * sm2加密-客户端
	 * @param msgString
	 */
	doSm2ClientEncrypt(msgString: string) {
		return "04" + sm2.doEncrypt(msgString, clientPublicKey, cipherMode);
	},
	/**
	 * sm2解密-服务端
	 * @param encryptData
	 */
	doSm2ServerDecrypt(encryptData: string) {
		if (encryptData.startsWith("04")) encryptData = encryptData.substring(2);
		return sm2.doDecrypt(encryptData, serverPrivateKey, cipherMode);
	},
	/**
	 * sm2加密
	 * @param msgString
	 * @param publicKey
	 */
	doSm2Encrypt(msgString: string, publicKey: string) {
		return "04" + sm2.doEncrypt(msgString, publicKey, cipherMode);
	},
	/**
	 * sm2解密
	 * @param encryptData
	 * @param privateKey
	 */
	doSm2Decrypt(encryptData: string, privateKey: string) {
		if (encryptData.startsWith("04")) encryptData = encryptData.substring(2);
		return sm2.doDecrypt(encryptData, privateKey, cipherMode);
	},
	/**
	 * sm3杂凑
	 * @param msgString
	 */
	doSm3(msgString: string) {
		return sm3(msgString);
	},
	/**
	 * sm4加密，默认输出 16 进制字符串，默认使用 pkcs#7 填充
	 * @param msgString
	 */
	doSm4Encrypt(msgString: string) {
		return sm4.encrypt(msgString, secretKey);
	},
	/**
	 * sm4加密，不使用 padding
	 * @param msgString
	 */
	doSm4NoPaddingEncrypt(msgString: string) {
		return sm4.encrypt(msgString, secretKey, { padding: "none" });
	},
	/**
	 * sm4加密，cbc 模式
	 * @param msgString
	 */
	doSm4CbcEncrypt(msgString: string) {
		return sm4.encrypt(msgString, secretKey, { mode: "cbc", iv: iv });
	},
	/**
	 * sm4解密，默认输出 utf8 字符串，默认使用 pkcs#7 填充
	 * @param encryptData
	 */
	doSm4Decrypt(encryptData: string) {
		return sm4.decrypt(encryptData, secretKey);
	},
	/**
	 * sm4解密，不使用 padding
	 * @param encryptData
	 */
	doSm4NoPaddingDecrypt(encryptData: string) {
		return sm4.decrypt(encryptData, secretKey, { padding: "none" });
	},
	/**
	 * sm4解密，cbc 模式
	 * @param encryptData
	 */
	doSm4CbcDecrypt(encryptData: string) {
		return sm4.decrypt(encryptData, secretKey, { mode: "cbc", iv: iv });
	}
};
