// * 请求响应参数(不包含data)
export interface Result {
	code: number;
	msg: string;
}

// * 请求响应参数(包含data)
export interface ResultData<T = any> extends Result {
	data: T;
}

// * 分页响应参数
export interface ResPage<T> {
	records: T[];
	current: number;
	size: number;
	total: number;
}

// * 分页请求参数
export interface ReqPage {
	current: number;
	size: number;
}

// * 文件上传模块
export namespace Upload {
	export interface ResFileUrl {
		fileUrl: string;
	}
}

// * 登录模块
export namespace Login {
	export interface ReqLoginForm {
		username: string;
		password: string;
		code: string;
		uuid: string;
	}
	export interface ResLogin {
		//TODO 弃用
		access_token: string;
	}
	export interface LoginUser {
		userId: string;
		userName: string;
		userAccount: string;
		lastLoginip: string;
		lastLogintime: string;
		userMobile: string;
		userGender: number;
		userEmail: string;
		userStatus: number;
		orgList: LoginUserOrg[];
	}
	export interface LoginUserOrg {
		orgId: string;
		orgName: string;
		orgShortname: string;
		orgCode: string;
		defaultFlag: number;
		userSort: number;
		propExtendis: string;
		parentId: string;
		treeLevel: number;
		treePath: string;
		treeSort: number;
	}
}

// * 用户管理模块
export namespace User {
	export interface ReqUserParams extends ReqPage {
		username: string;
		gender: number;
		idCard: string;
		email: string;
		address: string;
		createTime: string[];
		status: number;
	}
	export interface ResUserList {
		id: string;
		username: string;
		gender: number;
		user: {
			detail: {
				age: number;
			};
		};
		idCard: string;
		email: string;
		address: string;
		createTime: string;
		status: number;
		avatar: string;
		photo: any[];
		children?: ResUserList[];
	}
	export interface ResStatus {
		userLabel: string;
		userValue: number;
	}
	export interface ResGender {
		genderLabel: string;
		genderValue: number;
	}
	export interface ResDepartment {
		id: string;
		name: string;
		children?: ResDepartment[];
	}
	export interface ResRole {
		id: string;
		name: string;
		children?: ResDepartment[];
	}
}
