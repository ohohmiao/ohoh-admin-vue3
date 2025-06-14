import http from "@/api";
import { WorkflowBtn } from "@/api/modules/workflow/btn";
import { WorkflowNode } from "./node";

/**
 * @name 流程核心模块
 */
// * 数据定义
export namespace Workflow {
	// * 流程核心信息请求参数
	export interface ReqParams {
		defCode?: string;
		defVersion?: number;
		exeId?: string;
		curTaskId?: string;
	}
	// * 下一步环节信息请求参数
	export interface ReqNextNodeParams extends ReqParams {
		actType: number;
		businessForm: object;
	}
	// * 流程核心信息
	export interface FlowInfo<T> {
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
		processId: string;
		flowEntityClassName: string;
		entityVO: T;
		//entityId: string;
		doQueryFlag: boolean;
	}
	// * 流程环节办理人
	export interface FlowTaskHandler {
		handlerId: string;
		handlerName: string;
	}
	// * 流程任务环节
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
	// * 审核表单
	export interface ProcessForm {
		approvalResult?: number;
		handleDeadline?: string;
		handleOpition: string;
	}
	// * 流程提交表单
	export interface FlowSubmitForm {
		nextHandlerList: FlowTaskNode[];
		processForm: ProcessForm;
		businessForm: Record<string, any>;
	}
	// * 流程执行动作枚举
	export enum ActTypeEnum {
		TEMPSAVE = 0,
		SUBMIT = 1,
		RETURN = 2
	}
	// * 流程节点类型枚举
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
	return http.post<Workflow.FlowInfo<any>>("/workflow/getFlowInfo", params);
};

// * 查询流程下一环节信息
export const getWorkflowNextNodeListApi = (params: Workflow.ReqNextNodeParams) => {
	return http.post<Workflow.FlowTaskNode[]>("/workflow/getNextNodeList", params);
};
