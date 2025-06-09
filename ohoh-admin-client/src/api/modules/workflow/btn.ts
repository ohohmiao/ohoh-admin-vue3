import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 流程按钮模块
 */
// * 数据定义
export namespace WorkflowBtn {
	export interface ReqParams extends ReqPage {
		btnText: string;
		btnFun: string;
	}
	export interface Form {
		btnId: string;
		btnText: string;
		btnColor: string;
		btnFun: string;
	}
}

// * 获取流程按钮分页列表
export const getWorkflowBtnPageApi = (params: WorkflowBtn.ReqParams) => {
	return http.post<WorkflowBtn.Form>("/workflowBtn/page", params);
};

// * 获取单个流程按钮
export const getWorkflowBtnApi = (params: { id: string }) => {
	return http.post<WorkflowBtn.Form>("/workflowBtn/get", params);
};

// * 新增流程按钮
export const addWorkflowBtnApi = (params: WorkflowBtn.Form) => {
	return http.post<string>("/workflowBtn/add", params);
};

// * 修改流程按钮
export const editWorkflowBtnApi = (params: WorkflowBtn.Form) => {
	return http.post<string>("/workflowBtn/edit", params);
};

// * 删除流程按钮
export const deleteWorkflowBtnApi = (params: { id: string }) => {
	return http.post<string>("/workflowBtn/delete", params);
};
