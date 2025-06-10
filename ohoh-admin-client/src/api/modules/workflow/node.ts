import http from "@/api";
import { Workflow } from "./core";

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
		multiassignWeightjson?: WeightForm[];
		taskReturntype?: number;
		processlimitPermit?: number;
		approvalPermit?: number;
	}
	export interface WeightForm extends Workflow.FlowTaskHandler {
		weight: number;
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

// * 获取多人决策权重配置数据
export const getMultiAssignWeightListApi = (params: { defCode: string; defVersion: number; nodeId: string }) => {
	return http.post<WorkflowNode.WeightForm[]>("/workflowNode/listMultiAssignWeight", params);
};

// * 重置多人决策权重配置数据
export const resetMultiAssignWeightApi = (params: { defCode: string; defVersion: number; nodeId: string }) => {
	return http.post<WorkflowNode.WeightForm[]>("/workflowNode/resetMultiAssignWeight", params);
};

// * 获取流程下一任务环节基本信息
export const getNextTaskNodeInfoListApi = (params: { defCode: string; defVersion: number; nodeId: string }) => {
	return http.post<{ label: string; value: string }[]>("/workflowNode/listNextTaskNodeInfo", params);
};
