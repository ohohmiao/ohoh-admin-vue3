import { defineStore } from "pinia";
import { AuthState } from "@/stores/interface";
import { getFlatArr } from "@/utils/util";
import { getAuthMenuListApi, getAuthButtonListApi } from "@/api/modules/auth/accountPasswordLogin";
import { getShowMenuList, getAllBreadcrumbList } from "@/utils/util";

// AuthStore
export const AuthStore = defineStore({
	id: "AuthState",
	state: (): AuthState => ({
		// 当前页面的 router name，用来做按钮权限筛选
		routeName: "",
		// 菜单权限列表
		authMenuList: [],
		// 按钮权限列表
		authButtonList: {}
	}),
	getters: {
		// 当前页面router name
		routeNameGet: state => state.routeName,
		// 后端返回的菜单列表 ==> 这里没有经过任何处理
		authMenuListGet: state => state.authMenuList,
		// 后端返回的菜单列表 ==> 左侧菜单栏渲染，需要去除 isHide == true
		showMenuListGet: state => getShowMenuList(state.authMenuList),
		// 扁平化之后的一维数组路由，主要用来添加动态路由
		flatMenuListGet: state => getFlatArr(state.authMenuList),
		// 扁平化之后的一维数组路由，不包含目录
		flatLeafMenuListGet: state => getFlatArr(state.authMenuList).filter(item => item.meta.isLeaf),
		// 按钮权限列表
		authButtonListGet: state => state.authButtonList,
		// 所有面包屑导航列表
		breadcrumbListGet: state => getAllBreadcrumbList(state.authMenuList)
	},
	actions: {
		// getAuthMenuList
		async getAuthMenuList() {
			({ data: this.authMenuList } = await getAuthMenuListApi());
		},
		// getAuthButtonList
		async getAuthButtonList() {
			({ data: this.authButtonList } = await getAuthButtonListApi());
		},
		// setRouteName
		async setRouteName(name: string) {
			this.routeName = name;
		}
	}
});
