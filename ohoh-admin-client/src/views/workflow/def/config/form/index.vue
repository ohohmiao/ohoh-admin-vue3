<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="流程表单绑定列表"
			rowKey="formId"
			:columns="columns"
			:requestApi="getWorkflowFormBindPageApi"
			:initParam="{ defCode: props.defCode, defVersion: props.defVersion }"
			:toolButton="false"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
		>
			<!-- 表格 header 按钮 -->
			<template #tableHeader="scope">
				<el-button type="primary" @click="openForm('新增')">新增</el-button>
				<el-button type="danger" plain @click="handleMultiDelete(scope.selectedListIds)" :disabled="!scope.isSelected">
					批量删除
				</el-button>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" link @click="openForm('编辑', scope.row)">编辑</el-button>
				<el-button type="primary" link @click="handleDelete(scope.row)">删除</el-button>
			</template>
		</ProTable>
	</div>
	<!-- 流程表单绑定表单 -->
	<DefConfigFormBindForm
		ref="defConfigFormBindFormRef"
		:def-code="props.defCode"
		:def-version="props.defVersion"
		:def-xml="props.defXml"
	>
	</DefConfigFormBindForm>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import {
	WorkflowForm,
	getWorkflowFormBindPageApi,
	addWorkflowFormBindApi,
	editWorkflowFormBindApi,
	multiDeleteWorkflowFormBindApi
} from "@/api/modules/workflow/form";
import DefConfigFormBindForm from "./form.vue";
import { useHandleData } from "@/hooks/useHandleData";

const props = defineProps({
	defCode: String,
	defVersion: Number,
	defXml: String
});

const proTable = ref();
// 表格配置项
const columns: ColumnProps<WorkflowForm.BindForm>[] = [
	{ type: "selection", fixed: "left", width: 50 },
	{ prop: "formName", label: "表单名称", width: 200 },
	{ prop: "formPath", label: "表单路径", width: 200 },
	{ prop: "bindNodeNames", label: "绑定环节" },
	{ prop: "operation", label: "操作", width: 150 }
];

// 打开表单绑定表单
const defConfigFormBindFormRef = ref<InstanceType<typeof DefConfigFormBindForm>>();
const openForm = (title: string, rowData: Partial<WorkflowForm.Form> = {}) => {
	defConfigFormBindFormRef.value?.acceptParams({
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: title === "新增" ? addWorkflowFormBindApi : title === "编辑" ? editWorkflowFormBindApi : undefined,
		getTableList: proTable.value.getTableList
	});
};

// 删除表单
const handleDelete = async (params: WorkflowForm.Form) => {
	if (!params.formId) return;
	await useHandleData(multiDeleteWorkflowFormBindApi, { id: [params.formId] }, `删除【${params.formName}】流程表单`);
	proTable.value.getTableList();
};

// 批量删除表单
const handleMultiDelete = async (id: string[]) => {
	await useHandleData(multiDeleteWorkflowFormBindApi, { id }, `删除所选流程表单`);
	proTable.value.clearSelection();
	proTable.value.getTableList();
};
</script>

<style scoped lang="scss">
.table-box {
	width: 100%;
}
</style>
