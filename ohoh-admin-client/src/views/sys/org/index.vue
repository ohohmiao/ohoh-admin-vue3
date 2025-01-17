<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			id="orgId"
			label="orgName"
			:requestApi="getAuthSysOrgTreeApi"
			:defaultValue="initParam.parentId"
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
				title="组织机构列表"
				:columns="columns"
				:requestApi="getAuthSysOrgPageApi"
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
				</template>
			</ProTable>
		</div>
		<!-- 组织机构表单 -->
		<SysOrgForm ref="formRef" />
	</div>
</template>

<script setup lang="ts" name="SysOrgManage">
import { reactive, ref } from "vue";
import { CirclePlus, EditPen, Delete } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import {
	SysOrg,
	getAuthSysOrgTreeApi,
	getAuthSysOrgPageApi,
	addSysOrgApi,
	editSysOrgApi,
	deleteSysOrgApi
} from "@/api/modules/sys/org";
import { getLoginUserApi } from "@/api/modules/auth/accountPasswordLogin";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import SysOrgForm from "./form.vue";
import { GlobalStore } from "@/stores";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const globalStore = GlobalStore();
const { authButtons } = useAuthButtons();

// 获取 ProTable、TreeFilter 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const treeFilter = ref();
const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({ parentId: "" });

// 树形筛选切换
const changeTreeFilter = (val: string) => {
	initParam.parentId = val;
};

// 表格配置项
const columns: ColumnProps<SysOrg.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{ prop: "orgName", label: "组织名称", align: "left", search: { el: "input" } },
	{ prop: "orgLevel", label: "组织级别", width: 120 },
	{ prop: "treeLevel", label: "层级", width: 80 },
	{ prop: "treeSort", label: "排序", width: 80 }
];
if (authButtons.includes("edit") || authButtons.includes("delete")) {
	columns.push({ prop: "operation", label: "操作", width: 210, fixed: "right" });
}

// 删除
const handleDelete = async (params: SysOrg.Form) => {
	if (!params.orgId) return;
	await useHandleData(deleteSysOrgApi, { id: params.orgId }, `删除【${params.orgName}】组织机构`);
	await treeFilter.value.refreshTree(initParam.parentId);
	proTable.value.getTableList();
};

// 打开form（查看、新增、编辑）
const formRef = ref<InstanceType<typeof SysOrgForm> | null>(null);
const openForm = (title: string, rowData: Partial<SysOrg.Form> = {}) => {
	if (title === "新增") {
		if (initParam.parentId != "") {
			rowData.parentId = initParam.parentId;
		}
	}
	const params = {
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: title === "新增" ? addSysOrgApi : title === "编辑" ? editSysOrgApi : undefined,
		getTableList: async () => {
			await treeFilter.value.refreshTree(initParam.parentId);
			proTable.value.getTableList();
			// 如果是新增组织机构，则重写当前登录用户信息
			if (title == "新增") {
				const { data } = await getLoginUserApi();
				globalStore.setUserInfo(data);
			}
		}
	};
	formRef.value?.acceptParams(params);
};
</script>
