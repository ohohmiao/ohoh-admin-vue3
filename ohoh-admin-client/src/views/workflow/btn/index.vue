<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="流程按钮列表"
			:columns="formColumns"
			:requestApi="getWorkflowBtnPageApi"
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
		<!-- 流程表单表单 -->
		<FlowBtnForm ref="formRef"></FlowBtnForm>
	</div>
</template>

<script setup lang="tsx" name="WorkflowBtnManage">
import { ref } from "vue";
import { CirclePlus, EditPen, Setting } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import {
	getWorkflowBtnPageApi,
	WorkflowBtn,
	getWorkflowBtnApi,
	addWorkflowBtnApi,
	editWorkflowBtnApi,
	deleteWorkflowBtnApi
} from "@/api/modules/workflow/btn";
import { ColumnProps } from "@/components/ProTable/interface";
import { useAuthButtons } from "@/hooks/useAuthButtons";
import FlowBtnForm from "./form.vue";
import { useHandleData } from "@/hooks/useHandleData";

const { authButtons } = useAuthButtons();

const proTable = ref();

const formColumns: ColumnProps<WorkflowBtn.Form>[] = [
	{ type: "index", label: "#", width: 80 },
	{ prop: "btnText", label: "按钮文本", width: 150, search: { el: "input" } },
	{ prop: "btnColor", label: "按钮颜色", width: 150 },
	{ prop: "btnFun", label: "执行方法", search: { el: "input" } }
];
if (authButtons.includes("edit") || authButtons.includes("delete")) {
	formColumns.push({ prop: "operation", label: "操作", width: 200, fixed: "right" });
}

// 打开表单
const formRef = ref<InstanceType<typeof FlowBtnForm>>();
const openForm = async (title: string, rowData: Partial<WorkflowBtn.Form> = {}) => {
	if (title === "编辑") {
		const { data } = await getWorkflowBtnApi({ id: rowData.btnId! });
		rowData = data;
	}
	const params = {
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: title === "新增" ? addWorkflowBtnApi : title === "编辑" ? editWorkflowBtnApi : undefined,
		getTableList: proTable.value.getTableList
	};
	formRef.value?.acceptParams(params);
};

// 删除流程表单
const handleDelete = async (params: WorkflowBtn.Form) => {
	await useHandleData(deleteWorkflowBtnApi, { id: params.btnId }, `删除【${params.btnText}】流程按钮`);
	proTable.value.getTableList();
};
</script>

<style scoped lang="scss"></style>
