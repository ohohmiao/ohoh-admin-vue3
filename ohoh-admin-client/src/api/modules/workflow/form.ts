import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 流程表单模块
 */
// * 数据定义
export namespace WorkflowForm {
	export interface ReqParams extends ReqPage {
		parentId?: string;
		formName?: string;
	}
	export interface Form {
		formId: string;
		deftypeId: string;
		deftypeName?: string;
		formName: string;
		formPath: string;
	}
}

// * 获取流程表单分页列表
export const getWorkflowFormPageApi = (params: WorkflowForm.ReqParams) => {
	return http.post("/workflowForm/page", params);
};

// * 获取单个流程表单
export const getWorkflowFormApi = (params: { id: string }) => {
	return http.post<WorkflowForm.Form>("/workflowForm/get", params);
};

// * 保存流程表单
export const addWorkflowFormApi = (params: WorkflowForm.Form) => {
	return http.post<string>("/workflowForm/add", params);
};

// * 修改流程表单
export const editWorkflowFormApi = (params: WorkflowForm.Form) => {
	return http.post<string>("/workflowForm/edit", params);
};

// * 删除流程表单
export const deleteWorkflowFormApi = (params: { id: string }) => {
	return http.post<string>("/workflowForm/delete", params);
};
