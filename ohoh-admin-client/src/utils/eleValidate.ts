// * Element 常用表单校验规则

/**
 * 字母或数字
 * @param rule
 * @param value
 */
export function checkLetterOrNum(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[A-Za-z0-9]+$/;
	return regExp.test(value);
}

/**
 * 字母
 * @param rule
 * @param value
 */
export function checkLetter(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[A-Za-z]+$/;
	return regExp.test(value);
}

/**
 * 字母、数字或下划线
 * @param rule
 * @param value
 */
export function checkLetterOrNumOrUnderline(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[0-9a-zA-Z-_]+$/;
	return regExp.test(value);
}

/**
 * 字母、数字或小数点
 * @param rule
 * @param value
 */
export function checkLetterOrNumOrDot(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[0-9a-zA-Z-.]+$/;
	return regExp.test(value);
}

/**
 * 正整数
 * @param rule
 * @param value
 */
export function checkPositiveInt(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^\+?[1-9][0-9]*$/;
	return regExp.test(value);
}

/**
 * 座机号码
 * @param rule
 * @param value
 */
export function checkTelPhoneNum(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
	//const regExp = /^(\d{3,4}-)\d{7,8}$/;
	return regExp.test(value);
}

/**
 * 手机号码
 * @param rule
 * @param value
 */
export function checkMobileNum(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[1][3-9]\d{9}$/;
	return regExp.test(value);
}

/**
 * 座机号码或手机号码
 * @param rule
 * @param value
 */
export function checkTelPhoneOrMobileNum(rule: any, value: any) {
	if (!value) return true;
	const telPhoneRegExp = /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
	const mobileRegExp = /^[1][3-9]\d{9}$/;
	return mobileRegExp.test(value) || telPhoneRegExp.test(value);
}

/**
 * 价格
 * @param rule
 * @param value
 */
export function checkPrice(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^\d{1,11}(\.\d{1,3})?$/;
	return regExp.test(value);
}

/**
 * 电子邮箱
 * @param rule
 * @param value
 */
export function checkEmail(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	return regExp.test(value);
}

/**
 * 网址
 * @param rule
 * @param value
 */
export function checkUrl(rule: any, value: any) {
	if (!value) return true;
	const regExp = /http(s)?:\/\/[^\\s]*/;
	return regExp.test(value);
}

/**
 * IP
 * @param rule
 * @param value
 */
export function checkIp(rule: any, value: any) {
	if (!value) return true;
	const regExp =
		/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
	return regExp.test(value);
}

/**
 * 域名
 * @param rule
 * @param value
 */
export function checkDomainName(rule: any, value: any) {
	if (!value) return true;
	const regExp = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/;
	return regExp.test(value);
}

/**
 * 中文
 * @param rule
 * @param value
 */
export function checkChinese(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[\u4E00-\u9FA5]+$/;
	return regExp.test(value);
}

/**
 * 复杂密码
 * 密码必须包含字符大小写字母和数字，且要求8-30位
 * @param rule
 * @param value
 */
export function checkComplexPassword(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,30}?$/;
	return regExp.test(value);
}

/**
 * 邮政编码
 * @param rule
 * @param value
 */
export function checkPostalCode(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^[0-9]{6}$/;
	return regExp.test(value);
}

/**
 * 传真
 * @param rule
 * @param value
 */
export function checkFax(rule: any, value: any) {
	if (!value) return true;
	const regExp = /^(?:\d{3,4}-)?\d{7,8}(?:-\d{1,6})?$/;
	return regExp.test(value);
}

/**
 * 身份证号码，15位/18位
 * @param rule
 * @param value
 */
export function checkIdNum(rule: any, value: any) {
	if (!value) return true;

	value = value.toUpperCase();
	let len, re, arrSplit, dtmBirth, bGoodDay;
	len = value.length;

	// 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。
	if (!/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(value)) {
		return false;
	}

	// 验证前两位地区是否有效
	const aCity: { [index: string]: any } = {
		11: "北京",
		12: "天津",
		13: "河北",
		14: "山西",
		15: "内蒙古",
		21: "辽宁",
		22: "吉林",
		23: "黑龙江",
		31: "上海",
		32: "江苏",
		33: "浙江",
		34: "安徽",
		35: "福建",
		36: "江西",
		37: "山东",
		41: "河南",
		42: "湖北",
		43: "湖南",
		44: "广东",
		45: "广西",
		46: "海南",
		50: "重庆",
		51: "四川",
		52: "贵州",
		53: "云南",
		54: "西藏",
		61: "陕西",
		62: "甘肃",
		63: "青海",
		64: "宁夏",
		65: "新疆",
		71: "台湾",
		81: "香港",
		82: "澳门",
		91: "国外"
	};

	if (aCity[parseInt(value.substring(0, 2))] == null) {
		return false;
	}

	// 当身份证为15位时的验证出生日期。
	if (len == 15) {
		re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
		arrSplit = value.match(re);

		// 检查生日日期是否正确
		dtmBirth = new Date("19" + arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
		bGoodDay =
			dtmBirth.getFullYear() == Number(arrSplit[2]) &&
			dtmBirth.getMonth() + 1 == Number(arrSplit[3]) &&
			dtmBirth.getDate() == Number(arrSplit[4]);
		if (!bGoodDay) {
			return false;
		}
	}

	// 当身份证号为18位时，校验出生日期和校验位。
	if (len == 18) {
		re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
		arrSplit = value.match(re);
		// 检查生日日期是否正确
		dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
		bGoodDay =
			dtmBirth.getFullYear() == Number(arrSplit[2]) &&
			dtmBirth.getMonth() + 1 == Number(arrSplit[3]) &&
			dtmBirth.getDate() == Number(arrSplit[4]);
		if (!bGoodDay) {
			return false;
		} else {
			// 检验18位身份证的校验码是否正确。
			// 校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
			let valnum;
			let arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
			let arrCh = ["1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"];
			let nTemp = 0,
				i;
			for (i = 0; i < 17; i++) {
				nTemp += value.substring(i, i + 1) * arrInt[i];
			}
			valnum = arrCh[nTemp % 11];
			if (valnum != value.substring(17, 18)) {
				return false;
			}
		}
	}
	return true;
}
