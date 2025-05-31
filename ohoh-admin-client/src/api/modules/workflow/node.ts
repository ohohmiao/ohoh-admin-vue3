import http from "@/api";

/**
 * @name 流程环节模块
 */
// * 数据定义
export namespace WorkflowNode {
	export interface Form {
		propId?: string;
		defCode?: string;
		defVersion?: number;
		nodeId?: string;
		nodeName?: string;
		taskAssigntype?: number;
		multiassignRule?: number;
		multiassignRatio?: number;
		multiassignWeightjson?: string;
		taskReturntype?: number;
		processlimitPermit?: number;
		approvalPermit?: number;
	}
}

// * 获取流程某环节属性
export const getWorkflowNodeApi = (params: { defCode: string; defVersion: number; nodeId: string }) => {
	return http.post<WorkflowNode.Form>("/workflowNode/get", params);
};

// * 保存或修改流程某环节属性
export const addOrEditWorkflowNodeApi = (params: WorkflowNode.Form) => {
	return http.post<string>("/workflowNode/addOrEdit", params);
};
