<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			:requestApi="getWorkflowDefTypeTreeApi"
			id="id"
			label="name"
			:defaultValue="initParam.parentId"
			@change="changeTreeFilter"
		>
			<template #default="{ row }">
				<span :title="row.node.label">{{ row.node.label }}</span>
				<span class="actions" v-if="BUTTONS.addType || BUTTONS.editType || BUTTONS.deleteType">
					<el-dropdown>
						<el-link :icon="Operation"></el-link>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item
									@click="openDefTypeForm('新增', row.data)"
									v-if="BUTTONS.addType && (row.data.treeLevel == undefined || row.data.treeLevel <= 1)"
									>新增子类别</el-dropdown-item
								>
								<el-dropdown-item v-if="BUTTONS.editType && row.data.id" @click="openDefTypeForm('编辑', row.data)"
									>编辑</el-dropdown-item
								>
								<el-dropdown-item v-if="BUTTONS.deleteType && row.data.id" @click="handleDeleteDefType(row.data)"
									>删除</el-dropdown-item
								>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</span>
			</template>
		</TreeFilter>
		<div class="table-box">
			<ProTable
				ref="proTable"
				title="流程定义列表"
				rowKey="defId"
				:columns="columns"
				:requestApi="getWorkflowDefPageApi"
				:initParam="initParam"
				:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			>
				<!-- 表格 header 按钮 -->
				<template #tableHeader>
					<el-button type="primary" :icon="CirclePlus" v-auth="'add'" @click="openDefForm('新增')">新增</el-button>
				</template>
				<!-- 表格操作 -->
				<template #operation="scope">
					<el-button type="primary" link :icon="EditPen" @click="openDefForm('编辑', scope.row)" v-auth="'edit'">编辑</el-button>
					<el-button
						type="primary"
						link
						:icon="Setting"
						@click="openDefConfigTabs(scope.row.defCode, scope.row.defVersion)"
						v-auth="'config'"
						>配置</el-button
					>
					<el-dropdown v-if="BUTTONS.delete || BUTTONS.hisDeploy">
						<el-button type="primary" link :icon="More">
							更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
						</el-button>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item @click="openHisDeployList(scope.row)" v-if="BUTTONS.hisDeploy">历史版本</el-dropdown-item>
								<el-dropdown-item @click="handleDeleteDef(scope.row)" v-if="BUTTONS.delete">删除</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</template>
			</ProTable>
		</div>
		<!-- 流程定义类别表单 -->
		<DefTypeForm ref="defTypeFormRef"></DefTypeForm>
		<!-- 流程定义表单 -->
		<DefForm ref="defFormRef"></DefForm>
		<!-- 流程配置对话框 -->
		<DefConfigTabs ref="defConfigTabsRef"></DefConfigTabs>
		<!-- 历史版本列表对话框 -->
		<DefHisDeployList ref="defHisDeployListRef"></DefHisDeployList>
	</div>
</template>

<script setup lang="tsx" name="WorkflowDefManage">
import { reactive, ref } from "vue";
import { CirclePlus, EditPen, Operation, More, Setting, ArrowDown } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import {
	WorkflowDefType,
	getWorkflowDefTypeTreeApi,
	addWorkflowDefTypeApi,
	editWorkflowDefTypeApi,
	deleteWorkflowDefTypeApi,
	WorkflowDef,
	getWorkflowDefPageApi,
	addWorkflowDefApi,
	editWorkflowDefApi,
	getWorkflowDefApi,
	deleteWorkflowDefApi
} from "@/api/modules/workflow/def";
import { ColumnProps } from "@/components/ProTable/interface";
import DefTypeForm from "./DefTypeForm.vue";
import { useHandleData } from "@/hooks/useHandleData";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import DefForm from "./DefForm.vue";
import DefConfigTabs from "./DefConfigTabs.vue";
import DefHisDeployList from "@/views/workflow/def/config/DefHisDeployList.vue";

const { BUTTONS, authButtons } = useAuthButtons();

// 获取 ProTable、TreeFilter 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const treeFilter = ref();
const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({ parentId: "" });
// 树形筛选切换
const changeTreeFilter = (val: string) => {
	initParam.parentId = val;
};

const columns: ColumnProps<WorkflowDef.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{
		prop: "deftypeName",
		label: "类别",
		width: 120,
		render: scope => {
			return <el-tag>{scope.row.deftypeName}</el-tag>;
		}
	},
	{ prop: "defName", label: "流程名称", search: { el: "input" } },
	{ prop: "defCode", label: "流程编码", width: 150, search: { el: "input" } },
	{ prop: "defVersion", label: "版本号", width: 70 },
	{ prop: "defSort", label: "排序", width: 70 }
];
if (
	authButtons.includes("edit") ||
	authButtons.includes("delete") ||
	authButtons.includes("config") ||
	authButtons.includes("hisDeploy")
) {
	columns.push({ prop: "operation", label: "操作", width: 270, fixed: "right" });
}

// 打开流程定义类别表单
const defTypeFormRef = ref<InstanceType<typeof DefTypeForm> | null>(null);
const openDefTypeForm = (title: string, rowData: Partial<WorkflowDefType.TreeNode> = {}) => {
	let defTypeData: Partial<WorkflowDefType.TreeNode> = {};
	if (title === "新增") {
		defTypeData.parentId = rowData.id !== "" ? rowData.id : "0";
	} else {
		defTypeData = JSON.parse(JSON.stringify(rowData));
	}
	const params = {
		title,
		rowData: { ...defTypeData },
		isView: title === "查看",
		api: title === "新增" ? addWorkflowDefTypeApi : title === "编辑" ? editWorkflowDefTypeApi : undefined,
		getTableList: async () => {
			await treeFilter.value.refreshTree(initParam.parentId);
			proTable.value.getTableList();
		}
	};
	defTypeFormRef.value?.acceptParams(params);
};

// 删除流程定义类别
const handleDeleteDefType = async (params: WorkflowDefType.TreeNode) => {
	if (!params.deftypeId) return;
	await useHandleData(deleteWorkflowDefTypeApi, { id: params.deftypeId }, `删除【${params.deftypeName}】流程定义类别`);
	await treeFilter.value.refreshTree(initParam.parentId);
	proTable.value.getTableList();
};

// 打开流程定义表单
const defFormRef = ref<InstanceType<typeof DefForm> | null>(null);
const openDefForm = async (title: string, rowData: Partial<WorkflowDef.Form> = {}) => {
	if (title === "编辑") {
		const { data } = await getWorkflowDefApi({ id: rowData.defId! });
		rowData = data;
	}
	const params = {
		rowData: { ...rowData },
		api: title === "新增" ? addWorkflowDefApi : title === "编辑" ? editWorkflowDefApi : undefined,
		getTableList: proTable.value.getTableList
	};
	defFormRef.value?.acceptParams(params);
};

// 打开流程配置对话框
const defConfigTabsRef = ref<InstanceType<typeof DefConfigTabs>>();
const openDefConfigTabs = (defCode: string, defVersion: number) => {
	const params = {
		rowData: {
			defCode: defCode,
			defVersion: defVersion
		},
		getTableList: proTable.value.getTableList
	};
	defConfigTabsRef.value?.acceptParams(params);
};

const handleDeleteDef = async (params: WorkflowDef.Form) => {
	await useHandleData(deleteWorkflowDefApi, { id: params.defId }, `删除【${params.defName}】流程定义`);
	proTable.value.getTableList();
};

const defHisDeployListRef = ref<InstanceType<typeof DefHisDeployList>>();
const openHisDeployList = (params: WorkflowDef.Form) => {
	defHisDeployListRef.value?.acceptParams({
		defCode: params.defCode,
		getTableList: proTable.value.getTableList
	});
};
</script>

<style scoped lang="scss">
:deep(.el-dropdown) {
	height: 25px;
	margin-left: 12px;
	line-height: 25px;
}
</style>
