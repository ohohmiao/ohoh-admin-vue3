import http from "@/api";
import { WorkflowBtn } from "@/api/modules/workflow/btn";
import { WorkflowNode } from "./node";

/**
 * @name 流程核心模块
 */
// * 数据定义
export namespace Workflow {
	export interface ReqParams {
		defCode?: string;
		defVersion?: number;
		exeId?: string;
		curTaskId?: string;
	}
	export interface ReqNextNodeParams extends ReqParams {
		actType: number;
	}
	export interface FlowInfo {
		startFlowFlag: boolean;
		defCode: string;
		defVersion: number;
		defName: string;
		defXml: string;
		defJson: string;
		formId: string;
		formPath: string;
		flowSubject: string;
		curNodeInfo: WorkflowNode.Form;
		curRunningNodeIds: string;
		flowBtns: WorkflowBtn.Form[];
		doQueryFlag: boolean;
	}
	export interface FlowTaskHandler {
		handlerId: string;
		handlerName: string;
	}
	export interface FlowTaskNode {
		nodeId: string;
		nodeName: string;
		nodeType: string;
		handlers: FlowTaskHandler[];
		multiHandletype: number;
		reselectPermit: number;
		inclusiveGateWayId: string;
		nodeList?: FlowTaskNode[];
	}
	export interface ProcessForm {
		approvalResult?: number;
		handleDeadline?: string;
		handleOpition: string;
	}
	export interface FlowSubmitForm {
		nextHandlerList: FlowTaskNode[];
		processForm: ProcessForm;
		businessForm: Object;
	}
	export enum ActTypeEnum {
		TEMPSAVE = 0,
		SUBMIT = 1,
		RETURN = 2
	}
	export enum NodeTypeEnum {
		TASK = "task",
		END = "end",
		DECISION = "exclusiveGateway",
		PARALLEL = "parallelGateway",
		INCLUSIVE = "inclusiveGateway"
	}
}

// * 获取流程核心信息
export const getWorkflowFlowInfoApi = (params: Workflow.ReqParams) => {
	return http.post<Workflow.FlowInfo>("/workflow/getFlowInfo", params);
};

// * 查询流程下一环节信息
export const getWorkflowNextNodeListApi = (params: Workflow.ReqNextNodeParams) => {
	return http.post<Workflow.FlowTaskNode[]>("/workflow/getNextNodeList", params);
};
