import http from "@/api";

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
	export interface Form {
		startFlowFlag: boolean;
		defCode: string;
		defVersion: number;
		defName: string;
		defXml: string;
		defJson: string;
		formId: string;
		formPath: string;
		curNodeName: string;
		curNodeId: string;
		curRunningNodeIds: string;
		doQueryFlag: boolean;
	}
}

// * 获取流程核心信息
export const getWorkflowFlowInfoApi = (params: Workflow.ReqParams) => {
	return http.post<Workflow.Form>("/workflow/getFlowInfo", params);
};
