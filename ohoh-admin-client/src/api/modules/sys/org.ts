import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 组织机构管理模块
 */
// * 数据定义
export namespace SysOrg {
	export interface ReqParams extends ReqPage {
		orgName?: string;
	}
	export interface Form {
		orgId?: string;
		orgName?: string;
		orgShortname?: string;
		orgCode?: string;
		orgLevel?: number;
		parentId?: string;
		treeLevel?: number;
		treePath?: string;
		treeSort?: number;
		children?: TreeNode[];
	}
	export interface TreeNode extends Form {
		id: string;
		name: string;
		weight?: number;
		disabled?: boolean;
	}
}

// * 获取授权的组织机构树
export const getAuthSysOrgTreeApi = () => {
	return http.post<SysOrg.TreeNode[]>("/sysOrg/authTree");
};

// * 获取全部组织机构树
export const getAllSysOrgTreeApi = () => {
	return http.post<SysOrg.TreeNode[]>("/sysOrg/tree");
};

// * 获取授权的组织机构列表
export const getAuthSysOrgPageApi = (params?: SysOrg.ReqParams) => {
	return http.post("/sysOrg/authPage", params);
};

// * 新增组织机构
export const addSysOrgApi = (params: SysOrg.Form) => {
	return http.post("/sysOrg/add", params);
};

// * 编辑组织机构
export const editSysOrgApi = (params: SysOrg.Form) => {
	return http.post("/sysOrg/edit", params);
};

// * 删除组织机构
export const deleteSysOrgApi = (params: { id: string }) => {
	return http.post("/sysOrg/delete", params);
};
