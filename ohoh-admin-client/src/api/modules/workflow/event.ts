import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 流程事件模块
 */
// * 数据定义
export namespace WorkflowEvent {
	export interface ReqParams extends ReqPage {
		defCode: string;
		defVersion: number;
	}
	export interface Form {
		eventId: string;
		eventName: string;
		eventType: number;
		bindNodeIds: string;
		bindNodeNames: string;
		implType: number;
		implLocalservice: string;
		implScript: string;
		defCode?: string;
		defVersion?: number;
	}
}

// * 获取流程事件分页列表
export const getWorkflowEventPageApi = (params: WorkflowEvent.ReqParams) => {
	return http.post<WorkflowEvent.Form>("/workflowEvent/page", params);
};

// * 新增流程事件
export const addWorkflowEventApi = (params: WorkflowEvent.Form) => {
	return http.post<string>("/workflowEvent/add", params);
};

// * 修改流程事件
export const editWorkflowEventApi = (params: WorkflowEvent.Form) => {
	return http.post<string>("/workflowEvent/edit", params);
};

// * 删除流程事件
export const deleteWorkflowEventApi = (params: { id: string }) => {
	return http.post("/workflowEvent/delete", params);
};
