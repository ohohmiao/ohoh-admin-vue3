/**
 * @name 请假业务模块
 */
// * 数据定义
export namespace Leave {
	export interface Form {
		leaveId: string;
		applyUser: string;
		leaveType: number;
		leaveStartdate: string;
		leaveEnddate: string;
		leaveDays: number;
		applyReason: string;
		agentUserid?: string;
		agentUser?: string;
	}
}
