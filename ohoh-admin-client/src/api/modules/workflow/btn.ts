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
		btnSort?: number;
	}
	export interface ReqBindParams extends ReqPage {
		defCode: string;
		defVersion: number;
	}
	export interface BindForm {
		btnId: string;
		btnText?: string;
		btnColor?: string;
		btnFun?: string;
		bindNodeIds: string[];
		bindNodeNames: string[];
		defCode?: string;
		defVersion?: number;
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

// * 获取流程按钮绑定分页列表
export const getWorkflowBtnBindPageApi = (params: WorkflowBtn.ReqBindParams) => {
	return http.post<WorkflowBtn.BindForm>("/workflowBtn/listBindByPage", params);
};

// * 新增或修改流程按钮绑定
export const addOrEditWorkflowBtnBindApi = (params: WorkflowBtn.BindForm) => {
	return http.post<string>("/workflowBtn/addOrEditBind", params);
};

// * 批量删除流程按钮绑定
export const multiDeleteWorkflowBtnBindApi = (params: { id: string[] }) => {
	return http.post<string>("/workflowBtn/multiDeleteBind", params);
};

// * 获取流程按钮下拉框结构数据
export const getWorkflowBtnSelectApi = () => {
	return http.post<{ label: string; value: string }[]>("/workflowBtn/select");
};
