import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 流程定义模块
 */
// * 数据定义
export namespace WorkflowDefType {
	export interface Form {
		deftypeId?: string;
		deftypeName?: string;
		deftypeCode?: string;
		deftypeRemark?: string;
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
export namespace WorkflowDef {
	export interface ReqParams extends ReqPage {
		parentId?: string;
		defName?: string;
	}
	export interface Form {
		defId: string;
		deftypeId: string;
		deftypeName?: string;
	}
}

// * 获取流程定义类别树
export const getWorkflowDefTypeTreeApi = () => {
	return http.post<WorkflowDefType.TreeNode[]>("/workflowDefType/tree");
};

// * 获取流程定义分页列表
export const getWorkflowDefPageApi = (params: WorkflowDef.ReqParams) => {
	return http.post("/workflowDef/page", params);
};

// * 保存流程定义类别
export const addWorkflowDefTypeApi = (params: WorkflowDefType.Form) => {
	return http.post("/workflowDefType/add", params);
};

// * 修改流程定义类别
export const editWorkflowDefTypeApi = (params: WorkflowDefType.Form) => {
	return http.post("/workflowDefType/edit", params);
};

// * 删除流程定义类别
export const deleteWorkflowDefTypeApi = (params: { id: string }) => {
	return http.post("/workflowDefType/delete", params);
};
