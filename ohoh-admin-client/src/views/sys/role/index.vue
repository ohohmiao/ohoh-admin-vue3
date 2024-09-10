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
				title="系统角色列表"
				:columns="columns"
				:requestApi="getAuthSysRolePageApi"
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
					<el-dropdown v-if="BUTTONS.grantRes || BUTTONS.grantData || BUTTONS.grantUser">
						<el-button type="primary" link :icon="Key">
							授权<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item @click="handleGrantRes(scope.row)" v-if="BUTTONS.grantRes">授权资源</el-dropdown-item>
								<el-dropdown-item @click="handleGrantDataScope(scope.row)" v-if="BUTTONS.grantData">授权数据</el-dropdown-item>
								<el-dropdown-item
									v-if="BUTTONS.grantUser && !(scope.row.publicroleFlag && scope.row.publicroleFlag === 1)"
									@click="handleGrantUser(scope.row)"
									>授权用户</el-dropdown-item
								>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</template>
			</ProTable>
		</div>
		<!-- 角色表单 -->
		<SysRoleForm ref="formRef" />
		<!-- 授权数据范围对话框 -->
		<TreeFilterSelector
			ref="grantDataScopeFormRef"
			title="给角色授权数据范围"
			itemTitle="数据范围"
			idField="orgId"
			labelField="orgName"
			:data="authOrgTreeSelectDatas"
			allowChangeCheckStrictly
			:checkDisableHookFun="handleGrantDataScopeCheckDisable"
		>
			<template #formItem>
				<el-form-item label="所属组织" prop="orgId" :rules="{ required: true, message: '所属组织不能为空' }">
					<el-tree-select
						v-model="grantDataScopeFormRef.formProps.rowData.orgId"
						:data="authAllOrgTreeSelectDatas"
						:props="{ label: 'name' }"
						node-key="id"
						check-strictly
						filterable
						disabled
					>
						<template #default="{ node, data }">
							<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
							<el-icon v-else><Document /></el-icon>
							<span>{{ node.label }}</span>
						</template>
					</el-tree-select>
				</el-form-item>
				<el-form-item label="角色名称" prop="roleName" :rules="{ required: true, message: '角色名称不能为空' }">
					<el-input v-model="grantDataScopeFormRef.formProps.rowData.roleName" readonly></el-input>
				</el-form-item>
				<el-form-item label="数据范围" prop="datascopeType" :rules="{ required: true, message: '数据范围类别不能为空' }">
					<el-select
						v-model="grantDataScopeFormRef.formProps.rowData.datascopeType"
						placeholder="请选择数据范围类别"
						@change="
							grantDataScopeFormRef.formProps.isTreeFieldShow = grantDataScopeFormRef.formProps.rowData.datascopeType === 4
						"
					>
						<el-option :value="0" label="仅本人数据权限"></el-option>
						<el-option
							:value="1"
							label="全部机构数据权限"
							v-if="grantDataScopeFormRef.formProps.rowData.orgId === '0'"
						></el-option>
						<el-option :value="2" label="角色机构数据权限"></el-option>
						<el-option :value="3" label="角色机构及下属数据权限"></el-option>
						<el-option :value="4" label="自定义机构数据权限"></el-option>
					</el-select>
				</el-form-item>
			</template>
		</TreeFilterSelector>
		<!-- 授权资源对话框 -->
		<TreeFilterSelector
			ref="grantResFormRef"
			title="给角色授权系统资源"
			itemTitle="系统资源"
			idField="resId"
			labelField="resName"
			:requestApi="getAuthSysResTreeApi"
			allowChangeCheckStrictly
			:checkDisableHookFun="handleGrantResCheckDisable"
		>
			<template #formItem>
				<el-form-item label="所属组织" prop="orgId" :rules="{ required: true, message: '所属组织不能为空' }">
					<el-tree-select
						v-model="grantResFormRef.formProps.rowData.orgId"
						:data="authAllOrgTreeSelectDatas"
						:props="{ label: 'name' }"
						node-key="id"
						check-strictly
						filterable
						disabled
					>
						<template #default="{ node, data }">
							<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
							<el-icon v-else><Document /></el-icon>
							<span>{{ node.label }}</span>
						</template>
					</el-tree-select>
				</el-form-item>
				<el-form-item label="角色名称" prop="roleName" :rules="{ required: true, message: '角色名称不能为空' }">
					<el-input v-model="grantResFormRef.formProps.rowData.roleName" readonly></el-input>
				</el-form-item>
			</template>
		</TreeFilterSelector>
		<!-- 授权用户对话框 -->
		<SysCompositeSelector
			ref="grantUserRef"
			:title="operateRecord?.roleName != undefined ? `给角色[${operateRecord.roleName}]授权用户` : '给角色授权用户'"
			:notEmpty="false"
			:selectorTypes="[SelectorTypeEnum.USER]"
			:selectUserTypes="[SelectorUserTypeEnum.ORG]"
			@select="handleGrantUserSelected"
		></SysCompositeSelector>
	</div>
</template>

<script setup lang="tsx" name="SysRoleManage">
import { onBeforeMount, reactive, ref } from "vue";
import { CirclePlus, EditPen, Delete, Key } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import { getAuthSysOrgTreeApi, SysOrg } from "@/api/modules/sys/org";
import {
	SysRole,
	getAuthSysRolePageApi,
	addSysRoleApi,
	editSysRoleApi,
	deleteSysRoleApi,
	grantResSysRoleApi,
	listResIdSysRoleApi,
	grantDataScopeSysRoleApi
} from "@/api/modules/sys/role";
import { grantRoleToUserApi, listAuthUsersByRoleIdApi } from "@/api/modules/sys/user";
import SysRoleForm from "./form.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import { getAuthSysResTreeApi } from "@/api/modules/sys/res";
import TreeFilterSelector from "@/components/TreeFilterSelector/index.vue";
import SysCompositeSelector from "@/components/Selector/SysCompositeSelector.vue";
import { ElMessage } from "element-plus";
import { SelectorTypeEnum, SelectorUserTypeEnum } from "@/components/Selector/interface";
import { GlobalStore } from "@/stores";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const globalStore = GlobalStore();
const loginUserInfo = globalStore.userInfo;
const grantedDataScopes = loginUserInfo.grantedDataScopes;
const { BUTTONS, authButtons } = useAuthButtons();

const authAllOrgTreeSelectDatas = ref<SysOrg.TreeNode[]>([]);
const authOrgTreeSelectDatas = ref<SysOrg.TreeNode[]>([]);
onBeforeMount(async () => {
	// 获取授权的资源树形选择数据
	const { data } = await getAuthSysOrgTreeApi();
	authAllOrgTreeSelectDatas.value = [
		{
			id: "0",
			name: "全局角色",
			children: data
		}
	];
	authOrgTreeSelectDatas.value = data;
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
const columns: ColumnProps<SysRole.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{
		prop: "orgName",
		label: "组织名称",
		width: 150,
		render: scope => {
			return scope.row.orgName ? scope.row.orgName : <el-tag>全局角色</el-tag>;
		}
	},
	{ prop: "roleName", label: "角色名称", search: { el: "input" } },
	{ prop: "roleSort", label: "排序", width: 80 }
];
if (
	authButtons.includes("edit") ||
	authButtons.includes("delete") ||
	authButtons.includes("grantRes") ||
	authButtons.includes("grantData") ||
	authButtons.includes("grantUser")
) {
	columns.push({ prop: "operation", label: "操作", width: 250, fixed: "right" });
}

// 删除
const handleDelete = async (params: SysRole.Form) => {
	await useHandleData(deleteSysRoleApi, { id: params.roleId }, `删除【${params.roleName}】角色`);
	await treeFilter.value.refreshTree(initParam.orgId);
	proTable.value.getTableList();
};

// 打开form（查看、新增、编辑）
const formRef = ref<InstanceType<typeof SysRoleForm> | null>(null);
const openForm = (title: string, rowData: Partial<SysRole.Form> = {}) => {
	if (title === "新增") {
		if (initParam.orgId != "") {
			rowData.orgId = initParam.orgId;
		}
	}
	const params = {
		title,
		rowData: { ...rowData },
		isView: title === "查看",
		api: title === "新增" ? addSysRoleApi : title === "编辑" ? editSysRoleApi : undefined,
		getTableList: async () => {
			await treeFilter.value.refreshTree(initParam.orgId);
			proTable.value.getTableList();
		}
	};
	formRef.value?.acceptParams(params);
};

// 授权数据范围
const grantDataScopeFormRef = ref<InstanceType<typeof TreeFilterSelector> | null>(null);
const handleGrantDataScope = async (rowData: Partial<SysRole.Form> = {}) => {
	if (!rowData.roleId) return;
	const params = {
		isTreeFieldShow: rowData.datascopeType === 4,
		rowData: { ...rowData, selected: rowData.datascopeOrgIds },
		api: async (rowData: Partial<SysRole.Form> = {}, selected: string[]) => {
			if (!rowData.roleId || !rowData.orgId || rowData.datascopeType === undefined) return;
			const { msg } = await grantDataScopeSysRoleApi({
				roleId: rowData.roleId,
				orgId: rowData.orgId,
				datascopeType: rowData.datascopeType,
				datascopeOrgIds: selected
			});
			ElMessage.success({ message: msg });
			await treeFilter.value.refreshTree(initParam.orgId);
			proTable.value.getTableList();
		}
	};
	grantDataScopeFormRef.value?.acceptParams(params);
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

// 授权系统资源
const grantResFormRef = ref<InstanceType<typeof TreeFilterSelector> | null>(null);
const handleGrantRes = async (rowData: Partial<SysRole.Form> = {}) => {
	if (!rowData.roleId) return;
	const { data: resIdList } = await listResIdSysRoleApi({ id: rowData.roleId });
	const params = {
		isTreeFieldShow: true,
		rowData: { ...rowData, selected: resIdList },
		api: async (rowData: Partial<SysRole.Form> = {}, selected: string[]) => {
			if (!rowData.roleId || !rowData.orgId || !selected.length) return;
			const { msg } = await grantResSysRoleApi({ roleId: rowData.roleId, resIdList: selected });
			ElMessage.success({ message: msg });
			await treeFilter.value.refreshTree(initParam.orgId);
			proTable.value.getTableList();
		}
	};
	grantResFormRef.value?.acceptParams(params);
};
const handleGrantResCheckDisable = (params: any) => {
	if (!params) return;
	const loginUserInfo = globalStore.userInfo;
	const grantedResIds = loginUserInfo.grantedResIds;
	params.forEach((param: { resId: any; disabled: boolean; children: any }) => {
		if (!(loginUserInfo.superAdmin || (grantedResIds && grantedResIds.indexOf(param.resId) !== -1))) {
			param.disabled = true;
		}
		handleGrantResCheckDisable(param.children);
	});
};

// 授权用户
let operateRecord = ref<Partial<SysRole.Form> | null>(null);
const grantUserRef = ref<InstanceType<typeof SysCompositeSelector> | null>(null);
const handleGrantUser = async (rowData: Partial<SysRole.Form> = {}) => {
	if (!rowData.roleId) return;
	operateRecord.value = rowData;
	const { data } = await listAuthUsersByRoleIdApi({ id: rowData.roleId });
	const selectedData = data.map(d => {
		return {
			value: d.orgId + "#" + d.userId,
			label: d.userName,
			type: SelectorTypeEnum.USER,
			parentLabel: d.orgName,
			parentValue: d.orgId,
			extendValue: d.orgPropid
		};
	});
	grantUserRef.value?.acceptParams({ selected: selectedData });
};
const handleGrantUserSelected = async (datas: { [key: string]: any }[]) => {
	if (!operateRecord.value || !operateRecord.value?.roleId) return;
	const { msg } = await grantRoleToUserApi({
		roleId: operateRecord.value.roleId,
		userIdList: datas.map(data => data.value)
	});
	ElMessage.success({ message: msg });
};
</script>

<style scoped lang="scss">
:deep(.el-dropdown) {
	height: 25px;
	margin-left: 12px;
	line-height: 25px;
}
</style>
