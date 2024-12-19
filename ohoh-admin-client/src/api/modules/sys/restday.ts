import http from "@/api";
import { EventInput } from "@fullcalendar/core";

/**
 * @name 节假日模块
 */
// * 数据定义
export namespace SysRestDay {
	export interface ListParams {
		start: string;
		end: string;
	}
}

// * 获取FullCalendar节假日列表
export const listSysRestDay4FCApi = (params: SysRestDay.ListParams) => {
	return http.post<EventInput[]>("/sysRestDay/list4FC", params);
};

// * 新增节假日
export const addSysRestDayApi = (params: { date: string }) => {
	return http.post("/sysRestDay/add", params);
};

// * 删除节假日
export const deleteSysRestDayApi = (params: { date: string }) => {
	return http.post("/sysRestDay/delete", params);
};
