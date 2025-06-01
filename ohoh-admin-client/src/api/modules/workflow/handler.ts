import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 流程环节办理人模块
 */
// * 数据定义
export namespace WorkflowHandler {
	export interface ReqParams extends ReqPage {
		defCode: string;
		defVersion: number;
		nodeId: string;
	}
	export interface Form {
		handlerId: string;
		defCode: string;
		defVersion: number;
		nodeId: string;
		nextnodeId: string;
		nextnodeName: string;
		handlerType: number;
		targetReferResList?: { [p: string]: any }[];
		interfaceCode?: string;
		filterRule?: string;
		multiHandletype: number;
		reselectPermit: number;
	}
}

// * 获取流程环节办理人配置分页列表
export const getWorkflowHandlerPageApi = (params: WorkflowHandler.ReqParams) => {
	return http.post<WorkflowHandler.Form>("/workflowHandler/page", params);
};

// * 获取单个环节办理人配置
export const getWorkflowHandlerApi = (params: { id: string }) => {
	return http.post<WorkflowHandler.Form>("/workflowHandler/get", params);
};

// * 新增环节办理人配置
export const addWorkflowHandlerApi = (params: WorkflowHandler.Form) => {
	return http.post<string>("/workflowHandler/add", params);
};

// * 修改环节办理人配置
export const editWorkflowHandlerApi = (params: WorkflowHandler.Form) => {
	return http.post<string>("/workflowHandler/edit", params);
};

// * 批量删除环节办理人配置
export const multiDeleteWorkflowHandlerApi = (params: { id: string[] }) => {
	return http.post("/workflowHandler/multiDelete", params);
};
