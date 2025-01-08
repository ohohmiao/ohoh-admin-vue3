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
		defCode?: string;
	}
	export interface Form {
		defId: string;
		deftypeId: string;
		deftypeName?: string;
		defName: string;
		defCode: string;
		defVersion: number;
		defSort?: number;
		defXml: string;
		defJson: string;
		defSvg: string;
	}
}

// * 获取流程定义类别树
export const getWorkflowDefTypeTreeApi = () => {
	return http.post<WorkflowDefType.TreeNode[]>("/workflowDefType/tree");
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

// * 获取流程定义分页列表
export const getWorkflowDefPageApi = (params: WorkflowDef.ReqParams) => {
	return http.post("/workflowDef/page", params);
};

// * 获取单个流程定义
export const getWorkflowDefApi = (params: { id: string }) => {
	return http.post<WorkflowDef.Form>("/workflowDef/get", params);
};

// * 保存流程定义
export const addWorkflowDefApi = (params: WorkflowDef.Form) => {
	return http.post("/workflowDef/add", params);
};

// * 修改流程定义
export const editWorkflowDefApi = (params: WorkflowDef.Form) => {
	return http.post("/workflowDef/edit", params);
};

// * 删除流程定义
export const deleteWorkflowDefApi = (params: { id: string }) => {
	return http.post("/workflowDef/delete", params);
};

// * 获取历史流程定义
export const getWorkflowHisDeployApi = (params: { defCode: string; defVersion: number }) => {
	return http.post<WorkflowDef.Form>("/workflowHisDeploy/get", params);
};

// * 修复历史流程定义
export const editWorkflowHisDeployApi = (params: Partial<WorkflowDef.Form>) => {
	return http.post<string>("/workflowHisDeploy/edit", params);
};

// * 获取历史流程版本列表
export const getWorkflowHisDeployListApi = (params: { defCode: string; defVersion: number }) => {
	return http.post<WorkflowDef.Form>("/workflowHisDeploy/list", params);
};
