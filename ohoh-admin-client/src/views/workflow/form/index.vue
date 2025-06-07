<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			:requestApi="getWorkflowDefTypeTreeApi"
			id="id"
			label="name"
			:defaultValue="initParam.parentId"
			@change="val => (initParam.parentId = val)"
		>
		</TreeFilter>
		<div class="table-box">
			<ProTable
				ref="proTable"
				title="流程表单列表"
				rowKey="defId"
				:columns="formColumns"
				:requestApi="getWorkflowFormPageApi"
				:initParam="initParam"
				:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			>
				<!-- 表格 header 按钮 -->
				<template #tableHeader>
					<el-button type="primary" :icon="CirclePlus" v-auth="'add'" @click="openForm('新增')">新增</el-button>
				</template>
				<!-- 表格操作 -->
				<template #operation="scope">
					<el-button type="primary" link :icon="EditPen" @click="openForm('编辑', scope.row)" v-auth="'edit'">编辑</el-button>
					<el-button type="primary" link :icon="Setting" @click="handleDelete(scope.row)" v-auth="'delete'">删除</el-button>
				</template>
			</ProTable>
		</div>
		<!-- 流程表单表单 -->
		<FlowForm ref="formRef"></FlowForm>
	</div>
</template>

<script setup lang="tsx" name="WorkflowFormManage">
import { ref, reactive } from "vue";
import { CirclePlus, EditPen, Setting } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import { getWorkflowDefTypeTreeApi } from "@/api/modules/workflow/def";
import {
	getWorkflowFormPageApi,
	WorkflowForm,
	getWorkflowFormApi,
	addWorkflowFormApi,
	editWorkflowFormApi,
	deleteWorkflowFormApi
} from "@/api/modules/workflow/form";
import { ColumnProps } from "@/components/ProTable/interface";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import FlowForm from "./form.vue";
import { useHandleData } from "@/hooks/useHandleData";

const { authButtons } = useAuthButtons();

// 获取 ProTable、TreeFilter 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const treeFilter = ref();
const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({ parentId: "" });

const formColumns: ColumnProps<FlowForm.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{
		prop: "deftypeName",
		label: "类别",
		width: 120,
		render: scope => {
			return <el-tag>{scope.row.deftypeName}</el-tag>;
		}
	},
	{ prop: "formName", label: "表单名称", width: 250, search: { el: "input" } },
	{ prop: "formPath", label: "表单路径" }
];
if (authButtons.includes("edit") || authButtons.includes("delete")) {
	formColumns.push({ prop: "operation", label: "操作", width: 200, fixed: "right" });
}

// 打开表单
const formRef = ref<InstanceType<typeof FlowForm>>();
const openForm = async (title: string, rowData: Partial<WorkflowForm.Form> = {}) => {
	if (title === "新增") {
		if (initParam.parentId != "") {
			rowData.deftypeId = initParam.parentId;
		}
	}
	if (title === "编辑") {
		const { data } = await getWorkflowFormApi({ id: rowData.formId! });
		rowData = data;
	}
	const params = {
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		api: title === "新增" ? addWorkflowFormApi : title === "编辑" ? editWorkflowFormApi : undefined,
		getTableList: proTable.value.getTableList
	};
	formRef.value?.acceptParams(params);
};

// 删除流程表单
const handleDelete = async (params: WorkflowForm.Form) => {
	await useHandleData(deleteWorkflowFormApi, { id: params.defId }, `删除【${params.formName}】流程表单`);
	proTable.value.getTableList();
};
</script>

<style scoped lang="scss"></style>
