import http from "@/api";
import { ReqPage } from "@/api/interface";
import { SysRole } from "@/api/modules/sys/role";

/**
 * @name 系统用户管理模块
 */
// * 数据定义
export namespace SysUser {
	export interface ReqParams extends ReqPage {
		orgId: string;
		userName: string;
		userAccount: string;
		userMobile: string;
		userGender: number;
		userStatus: number;
	}
	export interface userOrg {
		propRecordid: string;
		propTablename?: string;
		propSort?: number;
		propExtendid?: string;
		defaultFlag?: number;
		propRecordname?: string;
	}
	export interface Form {
		userId: string;
		userName: string;
		userAccount: string;
		userMobile: string;
		userGender: number;
		userEmail: string;
		userStatus: number;
		orgList: userOrg[];
		orgPropid?: string;
		orgId?: string;
		orgName?: string;
		orgCode?: string;
		orgShortname?: string;
		orgPath?: string;
	}
	export interface grantRoleForm {
		userId: string;
		roleIdList: string[];
	}
	export interface grantRoleToUserForm {
		roleId: string;
		userIdList: string[];
	}
	export interface grantDataScopeForm {
		userId: string;
		datascopeOrgIds: string[];
	}
}

// * 获取授权的系统用户列表
export const getAuthSysUserPageApi = (params: Partial<SysUser.ReqParams>) => {
	return http.post("/sysUser/authPage", params);
};

// * 获取用户所在的组织列表
export const listOwnSysOrgsApi = (params: { id: string }) => {
	return http.post<SysUser.userOrg[]>("/sysUser/ownSysOrg", params);
};

// * 新增系统用户
export const addSysUserApi = (params: SysUser.Form) => {
	return http.post("/sysUser/add", params);
};

// * 编辑系统用户
export const editSysUserApi = (params: SysUser.Form) => {
	return http.post("/sysUser/edit", params);
};

// * 删除系统用户
export const deleteSysUserApi = (params: { id: string }) => {
	return http.post("/sysUser/delete", params);
};

// * 启用系统用户
export const enableSysUserApi = (params: { id: string }) => {
	return http.post("/sysUser/enable", params);
};

// * 禁用系统用户
export const disableSysUserApi = (params: { id: string }) => {
	return http.post("/sysUser/disable", params);
};

// * 重置密码
export const resetPwdSysUserApi = (params: { id: string }) => {
	return http.post("/sysUser/resetPassword", params);
};

// * 获取用户所授权的角色列表
export const listOwnSysRolesApi = (params: { id: string }) => {
	return http.post<SysRole.Form[]>("/sysUser/ownSysRole", params);
};

// * 给系统用户授权角色
export const grantRoleApi = (params: SysUser.grantRoleForm) => {
	return http.post("/sysUser/grantRole", params);
};

// * 获取所授权的机构用户树
export const getAuthOrgUserTreeApi = () => {
	return http.post("/sysUser/authOrgUserTree");
};

// * 列出某角色所授予的用户id集合
export const listAuthUsersByRoleIdApi = (params: { id: string }) => {
	return http.post<SysUser.Form[]>("/sysUser/listAuthSysUsersByRoleId", params);
};

// * 将角色授权给用户
export const grantRoleToUserApi = (params: SysUser.grantRoleToUserForm) => {
	return http.post("/sysUser/grantRoleToUser", params);
};

// * 列出某用户所授予的数据范围
export const listOwnDataScopesApi = (params: { id: string }) => {
	return http.post<SysUser.Form[]>("/sysUser/ownDataScope", params);
};

// * 给用户授权数据范围
export const grantDataScopeApi = (params: SysUser.grantDataScopeForm) => {
	return http.post("/sysUser/grantDataScope", params);
};

// * 获取岗位用户树
export const getPositionUserTreeApi = () => {
	return http.post("/sysUser/positionUserTree");
};
