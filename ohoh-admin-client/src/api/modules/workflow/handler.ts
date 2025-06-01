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
		targetReferResList?: ReferResForm[];
		interfaceCode?: string;
		filterRule?: string;
		multiHandletype: number;
		reselectPermit: number;
	}
	export interface ReferResForm {
		referRestype: string;
		referResid: string;
		referResname: string;
	}
}

// * 获取流程环节办理人配置分页列表
export const getWorkflowHandlerPageApi = (params: WorkflowHandler.ReqParams) => {
	return http.post<WorkflowHandler.Form>("/workflowHandler/page", params);
};

// * 删除流程环节办理人配置
export const deleteWorkflowHandlerApi = (params: { id: string }) => {
	return http.post("/workflowHandler/delete", params);
};
