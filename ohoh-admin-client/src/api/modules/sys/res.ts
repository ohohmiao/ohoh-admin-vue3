import http from "@/api";

/**
 * @name 系统资源管理模块
 */
// * 数据定义
export namespace SysRes {
	export interface ReqParams {
		resName?: string;
	}
	export interface Form {
		resId?: string;
		resName?: string;
		resCode?: string;
		resType?: number;
		resIcon?: string;
		menuType?: number;
		resPath?: string;
		hideFlag?: number;
		fullscreenFlag?: number;
		parentId?: string;
		treeLevel?: number;
		treePath?: string;
		treeSort?: number;
		children?: TreeNode[];
		urlList?: string[];
	}
	export interface TreeNode extends Form {
		id: string;
		name: string;
		weight?: number;
		disabled?: boolean;
	}
}

// * 获取系统资源树
export const getSysResTreeApi = (params: SysRes.ReqParams) => {
	return http.post<SysRes.TreeNode[]>("/sysRes/tree", params);
};

// * 获取授权的系统资源树
export const getAuthSysResTreeApi = () => {
	return http.post("/sysRes/authTree");
};

// * 新增系统资源
export const addSysResApi = (params: SysRes.Form) => {
	return http.post("/sysRes/add", params);
};

// * 编辑系统资源
export const editSysResApi = (params: SysRes.Form) => {
	return http.post("/sysRes/edit", params);
};

// * 删除系统资源
export const deleteSysResApi = (params: { id: string }) => {
	return http.post("/sysRes/delete", params);
};

// * 根据资源id，列出资源url
export const listSysResUrlByResIdApi = (params: { id: string }) => {
	return http.post<string[]>("/sysResUrl/listUrlByResId", params);
};

// * 列出SaPcCheckPermission注解的权限url
export const listSaPcPermissionUrlApi = () => {
	return http.post<string[]>("/sysResUrl/listSaPcPermissionUrl");
};
