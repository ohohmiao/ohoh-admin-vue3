<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			id="orgId"
			label="orgName"
			:data="authOrgTreeSelectDatas"
			:defaultValue="initParam.orgId"
			@change="changeTreeFilter"
		>
			<template #default="{ row }">
				<el-icon v-if="row.data.children"><FolderOpened v-if="row.node.expanded" /><Folder v-else /></el-icon>
				<el-icon v-else><Document /></el-icon>
				<span>{{ row.node.label }}</span>
			</template>
		</TreeFilter>
		<div class="table-box">
			<ProTable
				ref="proTable"
				title="系统用户列表"
				:columns="columns"
				:requestApi="getAuthSysUserPageApi"
				:initParam="initParam"
				:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			>
				<!-- 表格 header 按钮 -->
				<template #tableHeader>
					<el-button type="primary" :icon="CirclePlus" @click="openForm('新增')" v-auth="'add'">新增</el-button>
				</template>
				<!-- 表格操作 -->
				<template #operation="scope">
					<el-button type="primary" link :icon="EditPen" @click="openForm('编辑', scope.row)" v-auth="'edit'">编辑</el-button>
					<el-button type="primary" link :icon="Delete" @click="handleDelete(scope.row)" v-auth="'delete'">删除</el-button>
					<el-dropdown v-if="BUTTONS.resetPassword || BUTTONS.grantRole || BUTTONS.grantData">
						<el-button type="primary" link :icon="More">
							更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item @click="handleResetUserPassword(scope.row)" v-if="BUTTONS.resetPassword"
									>重置密码</el-dropdown-item
								>
								<el-dropdown-item @click="handleGrantRole(scope.row)" v-if="BUTTONS.grantRole">授权角色</el-dropdown-item>
								<el-dropdown-item @click="handleGrantDataScope(scope.row)" v-if="BUTTONS.grantData">授权数据</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</template>
			</ProTable>
		</div>
		<!-- 系统用户信息表单 -->
		<SysUserForm ref="formRef" />
		<!-- 授权角色选择器 -->
		<SysRoleSelector ref="roleSelectorRef" title="给用户授权角色" :notEmpty="false" @select="handleSysRoleSelected" />
		<!-- 授权数据范围对话框 -->
		<TreeFilterSelector
			ref="grantDataScopeFormRef"
			title="给用户授权数据范围"
			itemTitle="数据范围"
			idField="orgId"
			labelField="orgName"
			:data="authOrgTreeSelectDatas"
			allowChangeCheckStrictly
			:isNotEmpty="false"
			:checkDisableHookFun="handleGrantDataScopeCheckDisable"
		>
			<template #formItem="{ treeFilterFormProps }">
				<el-form-item label="姓名" prop="userName" :rules="{ required: true, message: '姓名不能为空' }">
					<el-input v-model="treeFilterFormProps.rowData.userName" readonly></el-input>
				</el-form-item>
				<el-form-item label="账号" prop="userAccount" :rules="{ required: true, message: '账号不能为空' }">
					<el-input v-model="treeFilterFormProps.rowData.userAccount" readonly></el-input>
				</el-form-item>
			</template>
		</TreeFilterSelector>
	</div>
</template>

<script setup lang="tsx" name="SysUserManage">
import { reactive, ref, onBeforeMount } from "vue";
import { CirclePlus, EditPen, Delete, More } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import { SysOrg, getAuthSysOrgTreeApi } from "@/api/modules/sys/org";
import {
	SysUser,
	getAuthSysUserPageApi,
	listOwnSysOrgsApi,
	listOwnSysRolesApi,
	addSysUserApi,
	editSysUserApi,
	deleteSysUserApi,
	enableSysUserApi,
	disableSysUserApi,
	resetPwdSysUserApi,
	grantRoleApi,
	listOwnDataScopesApi,
	grantDataScopeApi
} from "@/api/modules/sys/user";
import SysUserForm from "./form.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import SysRoleSelector from "@/components/Selector/SysRoleSelector.vue";
import TreeFilterSelector from "@/components/TreeFilterSelector/index.vue";
import { GlobalStore } from "@/stores";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const globalStore = GlobalStore();
const loginUserInfo = globalStore.userInfo;
const grantedDataScopes = loginUserInfo.grantedDataScopes;
const { BUTTONS, authButtons } = useAuthButtons();

const authOrgTreeSelectDatas = ref<SysOrg.TreeNode[]>([]);
onBeforeMount(async () => {
	// 获取授权的资源树形选择数据
	({ data: authOrgTreeSelectDatas.value } = await getAuthSysOrgTreeApi());
});

// 获取 ProTable、TreeFilter 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const treeFilter = ref();
const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({ orgId: "" });

// 树形筛选切换
const changeTreeFilter = (val: string) => {
	initParam.orgId = val;
};

// 表格配置项
const columns: ColumnProps<SysUser.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{ prop: "orgName", label: "组织名称", width: 120 },
	{ prop: "userAccount", label: "账号", search: { el: "input" } },
	{ prop: "userName", label: "姓名", search: { el: "input" } },
	{ prop: "userMobile", label: "手机号码", search: { el: "input" }, isShow: false },
	{
		prop: "userGender",
		label: "性别",
		tag: true,
		width: 80,
		enum: [
			{ label: "女", value: 0, tagType: "danger" },
			{ label: "男", value: 1, tagType: "success" }
		],
		search: { el: "select" }
	},
	{
		prop: "userStatus",
		label: "状态",
		width: 80,
		enum: [
			{ label: "禁用", value: 0 },
			{ label: "启用", value: 1 }
		],
		search: { el: "select" },
		render: scope => {
			return (
				<el-switch
					model-value={scope.row.userStatus}
					active-value={1}
					inactive-value={0}
					onClick={() => handleChangeUserStatus(scope.row)}
				></el-switch>
			);
		}
	},
	{ prop: "userSort", label: "排序", width: 80 }
];
if (
	authButtons.includes("edit") ||
	authButtons.includes("delete") ||
	authButtons.includes("resetPassword") ||
	authButtons.includes("grantRole") ||
	authButtons.includes("grantData")
) {
	columns.push({ prop: "operation", label: "操作", width: 240, fixed: "right" });
}

// 删除
const handleDelete = async (params: SysUser.Form) => {
	await useHandleData(deleteSysUserApi, { id: params.userId }, `删除【${params.userAccount}】系统用户`);
	proTable.value.getTableList();
};

// 启用/禁用
const handleChangeUserStatus = async (params: SysUser.Form) => {
	if (!authButtons.includes("edit")) {
		ElMessage.warning({ message: "操作失败，您没有修改用户的权限" });
		return false;
	}
	await useHandleData(
		params.userStatus == 1 ? disableSysUserApi : enableSysUserApi,
		{ id: params.userId },
		`${params.userStatus == 1 ? "禁用" : "启用"}【${params.userAccount}】系统用户`
	);
	proTable.value.getTableList();
};

// 重置密码
const handleResetUserPassword = async (params: SysUser.Form) => {
	await useHandleData(resetPwdSysUserApi, { id: params.userId }, `重置密码【${params.userAccount}】系统用户`, "warning", res => {
		ElMessageBox.alert(res.msg);
	});
};

// 授权角色
let operateRecord: SysUser.Form | null = null;
const roleSelectorRef = ref<InstanceType<typeof SysRoleSelector>>();
const handleGrantRole = async (param: SysUser.Form) => {
	operateRecord = param;
	const { data } = await listOwnSysRolesApi({ id: param.userId });
	const selectedData = data.map(d => {
		return { value: d.roleId, label: d.roleName };
	});
	roleSelectorRef.value?.acceptParams({ selected: selectedData });
};
const handleSysRoleSelected = async (datas: { [key: string]: any }[]) => {
	if (!operateRecord) return;
	const { msg } = await grantRoleApi({
		userId: operateRecord.userId,
		roleIdList: datas.map(data => data.value)
	});
	ElMessage.success({ message: msg });
};

// 授权数据
const grantDataScopeFormRef = ref<InstanceType<typeof TreeFilterSelector>>();
const handleGrantDataScope = async (params: Partial<SysUser.Form> = {}) => {
	if (!params || !params.userId) return;
	const { data: datascopeOrgIds } = await listOwnDataScopesApi({ id: params.userId });
	const selectorProps = {
		isTreeFieldShow: true,
		rowData: { ...params, selected: datascopeOrgIds },
		api: async (rowData: Partial<SysUser.Form> = {}, selected: string[]) => {
			if (!rowData.userId) return;
			const { msg } = await grantDataScopeApi({
				userId: rowData.userId,
				datascopeOrgIds: selected
			});
			ElMessage.success({ message: msg });
			await treeFilter.value.refreshTree(initParam.orgId);
			proTable.value.getTableList();
		}
	};
	grantDataScopeFormRef.value?.acceptParams(selectorProps);
};
const handleGrantDataScopeCheckDisable = (params: any) => {
	if (!params) return;
	params.forEach((param: { orgId: any; disabled: boolean; children: any }) => {
		if (!(loginUserInfo.superAdmin || (grantedDataScopes && grantedDataScopes.indexOf(param.orgId) !== -1))) {
			param.disabled = true;
		}
		handleGrantDataScopeCheckDisable(param.children);
	});
};

// 打开form（查看、新增、编辑）
const formRef = ref<InstanceType<typeof SysUserForm> | null>(null);
const openForm = async (title: string, rowData: Partial<SysUser.Form> = {}) => {
	const plainRowData = JSON.parse(JSON.stringify(rowData));
	if (title === "新增") {
		if (initParam.orgId) {
			plainRowData.orgList = [
				{
					propRecordid: initParam.orgId
				}
			];
		}
	}
	if (title === "编辑") {
		if (rowData.userId) {
			({ data: plainRowData.orgList } = await listOwnSysOrgsApi({ id: rowData.userId }));
		}
	}
	const params = {
		title,
		rowData: { ...plainRowData },
		isView: title === "查看",
		api: title === "新增" ? addSysUserApi : title === "编辑" ? editSysUserApi : undefined,
		getTableList: proTable.value.getTableList
	};
	formRef.value?.acceptParams(params);
};
</script>

<style scoped lang="scss">
:deep(.el-dropdown) {
	height: 25px;
	margin-left: 12px;
	line-height: 25px;
}
</style>
