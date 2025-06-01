import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 系统字典模块
 */
// * 数据定义
export namespace SysDicType {
	export interface Form {
		dictypeId?: string;
		dictypeName?: string;
		dictypeCode?: string;
		dictypeRemark?: string;
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
export namespace SysDic {
	export interface ReqParams extends ReqPage {
		parentId?: string;
		dicName?: string;
	}
	export interface Form {
		dicId: string;
		dictypeId: string;
		dicName: string;
		dicCode: string;
		dicSort: number;
		extendField: string;
		dictypeCode?: string;
		dictypeName?: string;
	}
}

// * 获取系统字典类别树
export const getSysDicTypeTreeApi = () => {
	return http.post<SysDicType.TreeNode[]>("/sysDicType/tree");
};

// * 获取系统字典分页列表
export const getSysDicPageApi = (params: SysDic.ReqParams) => {
	return http.post("/sysDic/page", params);
};

// * 保存系统字典类别
export const addSysDicTypeApi = (params: SysDicType.Form) => {
	return http.post("/sysDicType/add", params);
};

// * 修改系统字典类别
export const editSysDicTypeApi = (params: SysDicType.Form) => {
	return http.post("/sysDicType/edit", params);
};

// * 删除系统字典类别
export const deleteSysDicTypeApi = (params: { id: string }) => {
	return http.post("/sysDicType/delete", params);
};

// * 保存系统字典
export const addSysDicApi = (params: SysDic.Form) => {
	return http.post("/sysDic/add", params);
};

// * 修改系统字典
export const editSysDicApi = (params: SysDic.Form) => {
	return http.post("/sysDic/edit", params);
};

// * 删除系统字典
export const deleteSysDicApi = (params: { id: string[] }) => {
	return http.post("/sysDic/delete", params);
};

// * 获取某类别字典的下拉框数据
export const getSysDicSelectApi = (params: { dictypeCode: string }) => {
	return http.post<{ label: string; value: string }[]>("/sysDic/select", params);
};
