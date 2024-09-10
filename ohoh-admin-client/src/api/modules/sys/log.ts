import http from "@/api";
import { ReqPage } from "@/api/interface";

/**
 * @name 系统日志管理模块
 */
// * 数据定义
export namespace SysLog {
	export interface ReqParams extends ReqPage {
		logType?: number;
	}
	export interface Form {
		logId: string;
		logType: number;
		logName: string;
		logDetail: string;
		operateIp: string;
		operateBrowser: string;
		operateOs: string;
		requestUri: string;
		paramJson: string;
		resultJson: string;
		operateUserid: string;
		operateUser: string;
		operateTime: string;
	}
}

// * 获取系统日志分页列表
export const getSysLogPageApi = (params: SysLog.ReqParams) => {
	return http.post("/sysLog/page", params);
};
