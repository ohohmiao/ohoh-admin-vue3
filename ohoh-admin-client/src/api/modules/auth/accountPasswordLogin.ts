import { Login, ResultData } from "@/api/interface/index";
import http from "@/api";
//import AuthButtons from "@/assets/json/authButtons.json";
//import DynamicRouter from "@/assets/json/dynamicRouter.json";

/**
 * @name 登录模块
 */
// * 用户登录
export const loginApi = (params: Login.ReqLoginForm) => {
	return http.post<string>("/auth/login", params);
};

// * 获取登录用户信息
export const getLoginUserApi = () => {
	return http.get<ResultData<Login.LoginUser>>("/auth/getLoginUser");
};

// * 获取菜单列表
export const getAuthMenuListApi = () => {
	return http.get<Menu.MenuOptions[]>("/sysRes/listAuthMenu", {}, { headers: { noLoading: true } });
	// 如果想让菜单变为本地数据，注释上一行代码，并引入本地 dynamicRouter.json 数据
	//return DynamicRouter;
};

// * 获取按钮权限
export const getAuthButtonListApi = () => {
	return http.get<Button.ButtonOptions>("/sysRes/listAuthButton", {}, { headers: { noLoading: true } });
	// 如果想让按钮权限变为本地数据，注释上一行代码，并引入本地 authButtons.json 数据
	//return AuthButtons;
};

// * 用户退出登录
export const logoutApi = () => {
	return http.post("/auth/logout");
};
