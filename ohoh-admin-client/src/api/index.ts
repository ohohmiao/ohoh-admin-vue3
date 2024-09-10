import axios, { AxiosInstance, AxiosError, AxiosRequestConfig, InternalAxiosRequestConfig, AxiosResponse } from "axios";
import { showFullScreenLoading, tryHideFullScreenLoading } from "@/config/serviceLoading";
import { ResultData } from "@/api/interface";
import { ResultEnum } from "@/enums/httpEnum";
import { checkStatus } from "./helper/checkStatus";
import { ElMessage } from "element-plus";
import { GlobalStore } from "@/stores";
import { LOGIN_URL } from "@/config/config";
import router from "@/routers";
import smCrypto from "@/utils/smCrypto";

// 请求配置
const config = {
	// 默认地址请求地址，可在 .env.*** 文件中修改
	baseURL: import.meta.env.VITE_API_PREFIX,
	// 设置超时时间（30s）
	timeout: ResultEnum.TIMEOUT as number,
	// 跨域时候允许携带凭证
	withCredentials: true
};
// 请求加密/响应解密开关
const encryptSwitch = import.meta.env.VITE_TRANSPORT_ENCRYPTSWITCH === "true" ? true : false;

class RequestHttp {
	service: AxiosInstance;
	public constructor(config: AxiosRequestConfig) {
		// 实例化axios
		this.service = axios.create(config);

		/**
		 * @description 请求拦截器
		 * 客户端发送请求 -> [请求拦截器] -> 服务器
		 * token校验(JWT) : 接受服务器返回的token,存储到vuex/pinia/本地储存当中
		 */
		this.service.interceptors.request.use(
			(config: InternalAxiosRequestConfig) => {
				const globalStore = GlobalStore();
				// * 如果当前请求不需要显示 loading,在 api 服务中通过指定的第三个参数: { headers: { noLoading: true } }来控制不显示loading，参见loginApi
				config.headers!.noLoading || showFullScreenLoading();
				// 组装token进请求头
				const token = globalStore.token;
				if (config.headers && typeof config.headers?.set === "function") config.headers.set("x-access-token", token);
				// 请求加密
				if (encryptSwitch) {
					if (config.data && typeof config.data === "object") {
						config.data = window.btoa(smCrypto.doSm2ClientEncrypt(JSON.stringify(config.data)));
						config.headers.set("content-type", "application/json");
					}
				}
				return config;
			},
			(error: AxiosError) => {
				return Promise.reject(error);
			}
		);

		/**
		 * @description 响应拦截器
		 *  服务器换返回信息 -> [拦截统一处理] -> 客户端JS获取到信息
		 */
		this.service.interceptors.response.use(
			(response: AxiosResponse) => {
				const { data } = response;
				let thizEncryptData = data;
				try {
					if (encryptSwitch) {
						thizEncryptData = JSON.parse(smCrypto.doSm2ServerDecrypt(window.atob(thizEncryptData)));
					}
				} catch (e) {
					// console.error(e);
				}
				const globalStore = GlobalStore();
				// * 在请求结束后，并关闭请求 loading
				tryHideFullScreenLoading();
				// * 登录失效（code == 401）
				if (thizEncryptData.code == ResultEnum.OVERDUE) {
					ElMessage.error(thizEncryptData.msg);
					globalStore.setToken("");
					router.replace(LOGIN_URL);
					return Promise.reject(thizEncryptData);
				}
				// * 全局错误信息拦截（防止下载文件得时候返回数据流，没有code，直接报错）
				if (thizEncryptData.code && thizEncryptData.code !== ResultEnum.SUCCESS) {
					ElMessage.error(thizEncryptData.msg);
					return Promise.reject(thizEncryptData);
				}
				// * 成功请求（在页面上除非特殊情况，否则不用在页面处理失败逻辑）
				return encryptSwitch ? response : data;
			},
			async (error: AxiosError) => {
				const { response } = error;
				tryHideFullScreenLoading();
				// 请求超时 && 网络错误单独判断，没有 response
				if (error.message.indexOf("timeout") !== -1) ElMessage.error("请求超时！请您稍后重试");
				if (error.message.indexOf("Network Error") !== -1) ElMessage.error("网络错误！请您稍后重试");
				// 根据响应的错误状态码，做不同的处理
				if (response) checkStatus(response.status);
				// 服务器结果都没有返回(可能服务器错误可能客户端断网)，断网处理:可以跳转到断网页面
				if (!window.navigator.onLine) router.replace("/500");
				return Promise.reject(error);
			}
		);
	}

	// * 常用请求方法封装
	async get<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
		if (encryptSwitch) {
			// 响应解密
			const { data } = await this.service.get(url, { params, ..._object });
			if (typeof data == "string") {
				return JSON.parse(smCrypto.doSm2ServerDecrypt(window.atob(data)));
			} else {
				return data;
			}
		} else {
			return this.service.get(url, { params, ..._object });
		}
	}
	async post<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
		if (encryptSwitch) {
			// 响应解密
			const { data } = await this.service.post(url, params, _object);
			if (typeof data == "string") {
				return JSON.parse(smCrypto.doSm2ServerDecrypt(window.atob(data)));
			} else {
				return data;
			}
		} else {
			return this.service.post(url, params, _object);
		}
	}
	async put<T>(url: string, params?: object, _object = {}): Promise<ResultData<T>> {
		if (encryptSwitch) {
			// 响应解密
			const { data } = await this.service.put(url, params, _object);
			if (typeof data == "string") {
				return JSON.parse(smCrypto.doSm4CbcDecrypt(window.atob(data)));
			} else {
				return data;
			}
		} else {
			return this.service.put(url, params, _object);
		}
	}
	async delete<T>(url: string, params?: any, _object = {}): Promise<ResultData<T>> {
		if (encryptSwitch) {
			// 响应解密
			const { data } = await this.service.delete(url, { params, ..._object });
			if (typeof data == "string") {
				return JSON.parse(smCrypto.doSm4CbcDecrypt(window.atob(data)));
			} else {
				return data;
			}
		} else {
			return this.service.delete(url, { params, ..._object });
		}
	}
	download(url: string, params?: object, _object = {}): Promise<BlobPart> {
		return this.service.post(url, params, { ..._object, responseType: "blob" });
	}
}

export default new RequestHttp(config);
