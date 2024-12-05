import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 角色管理模块
 */
// * 数据定义
export namespace SysRole {
	export interface ReqParams extends ReqPage {
		orgId?: string;
		roleName?: string;
	}
	export interface Form {
		orgId: string;
		orgName?: string;
		roleId: string;
		roleName: string;
		publicroleFlag: number;
		roleRemark: string;
		datascopeType: number;
		datascopeOrgIds: string[];
		roleSort?: number;
	}
	export interface grantDataScopeForm {
		roleId: string;
		orgId: string;
		datascopeType: number;
		datascopeOrgIds: string[];
	}
	export interface grantResForm {
		roleId: string;
		resIdList: string[];
	}
}

// * 获取授权的角色列表
export const getAuthSysRolePageApi = (params?: SysRole.ReqParams) => {
	return http.post("/sysRole/authPage", params);
};

// * 新增角色
export const addSysRoleApi = (params: SysRole.Form) => {
	return http.post("/sysRole/add", params);
};

// * 编辑角色
export const editSysRoleApi = (params: SysRole.Form) => {
	return http.post("/sysRole/edit", params);
};

// * 删除角色
export const deleteSysRoleApi = (params: { id: string }) => {
	return http.post("/sysRole/delete", params);
};

// * 给角色授权数据范围
export const grantDataScopeSysRoleApi = (params: SysRole.grantDataScopeForm) => {
	return http.post("/sysRole/grantDataScope", params);
};

// * 列出某角色所授予的资源id集合
export const listResIdSysRoleApi = (params: { id: string }) => {
	return http.post("/sysRole/ownSysRes", params);
};

// * 给角色授权资源
export const grantResSysRoleApi = (params: SysRole.grantResForm) => {
	return http.post("/sysRole/grantRes", params);
};
