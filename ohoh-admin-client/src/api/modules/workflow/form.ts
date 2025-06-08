import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 流程表单模块
 */
// * 数据定义
export namespace WorkflowForm {
	export interface ReqParams extends ReqPage {
		defCode: string;
		defVersion: number;
	}
	export interface Form {
		formId: string;
		formName: string;
		formPath: string;
		bindNodeIds: string[];
		bindNodeNames: string[];
		defCode?: string;
		defVersion?: number;
	}
}

// * 获取流程表单绑定分页列表
export const getWorkflowFormBindPageApi = (params: WorkflowForm.ReqParams) => {
	return http.post<WorkflowForm.Form>("/workflowForm/listBindByPage", params);
};

// * 修改流程表单绑定
export const addWorkflowFormBindApi = (params: WorkflowForm.Form) => {
	return http.post<string>("/workflowForm/addBind", params);
};

// * 新增流程表单绑定
export const editWorkflowFormBindApi = (params: WorkflowForm.Form) => {
	return http.post<string>("/workflowForm/editBind", params);
};

// * 批量删除流程表单绑定
export const multiDeleteWorkflowFormBindApi = (params: { id: string[] }) => {
	return http.post<string>("/workflowForm/multiDeleteBind", params);
};
