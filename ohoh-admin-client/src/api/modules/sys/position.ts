import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 岗位管理模块
 */
// * 数据定义
export namespace SysPosition {
	export interface ReqParams extends ReqPage {
		parentId?: string;
		userName?: string;
	}
	export interface Form {
		positionId?: string;
		positionName?: string;
		positionCode?: string;
		positionLevel?: number;
		positionRemark?: string;
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
	export interface grantUserForm {
		positionId: string;
		propIds: string[];
	}
	export interface PositionUserProp {
		propId: string;
		positionId?: string;
		positionName?: string;
		orgId: string;
		orgName: string;
		userId: string;
		userAccount?: string;
		userName: string;
		userStatus?: number;
	}
}

// * 获取全部岗位树
export const getAllSysPositionTreeApi = () => {
	return http.post<SysPosition.TreeNode[]>("/sysPosition/tree");
};

// * 新增岗位
export const addSysPositionApi = (params: SysPosition.Form) => {
	return http.post("/sysPosition/add", params);
};

// * 编辑岗位
export const editSysPositionApi = (params: SysPosition.Form) => {
	return http.post("/sysPosition/edit", params);
};

// * 删除岗位
export const deleteSysPositionApi = (params: { id: string }) => {
	return http.post("/sysPosition/delete", params);
};

// * 获取岗位用户分页列表
export const getSysPositionUserPageApi = (params: SysPosition.ReqParams) => {
	return http.post("/sysPosition/pageUser", params);
};

// * 获取某岗位的用户列表
export const listSysPositionUserApi = (params: { id: string }) => {
	return http.post<SysPosition.PositionUserProp[]>("/sysPosition/listUser", params);
};

// * 给岗位授权用户
export const grantUserSysPositionApi = (params: SysPosition.grantUserForm) => {
	return http.post("/sysPosition/grantUser", params);
};

// * 取消授权岗位用户
export const revokeUserSysPositionApi = (params: { id: string[] }) => {
	return http.post("/sysPosition/revokeUser", params);
};
