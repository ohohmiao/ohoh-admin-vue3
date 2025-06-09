import http from "@/api";
import { WorkflowBtn } from "@/api/modules/workflow/btn";

/**
 * @name 流程核心模块
 */
// * 数据定义
export namespace Workflow {
	export interface ReqParams {
		defCode?: string;
		defVersion?: string;
		exeId?: string;
		curTaskId?: string;
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
		curNodeName: string;
		curNodeId: string;
		curRunningNodeIds: string;
		flowBtns: WorkflowBtn.Form[];
		doQueryFlag: boolean;
	}
}

// * 获取流程核心信息
export const getWorkflowFlowInfoApi = (params: Workflow.ReqParams) => {
	return http.post<Workflow.FlowInfo>("/workflow/getFlowInfo", params);
};
