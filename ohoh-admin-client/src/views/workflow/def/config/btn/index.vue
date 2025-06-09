<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="流程按钮绑定列表"
			rowKey="btnId"
			:columns="columns"
			:requestApi="getWorkflowBtnBindPageApi"
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
	<!-- 流程按钮绑定表单 -->
	<DefConfigBtnBindForm
		ref="formRef"
		:def-code="props.defCode"
		:def-version="props.defVersion"
		:def-xml="props.defXml"
	></DefConfigBtnBindForm>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import {
	WorkflowBtn,
	getWorkflowBtnBindPageApi,
	addOrEditWorkflowBtnBindApi,
	multiDeleteWorkflowBtnBindApi
} from "@/api/modules/workflow/btn";
import DefConfigBtnBindForm from "./form.vue";
import { useHandleData } from "@/hooks/useHandleData";

const props = defineProps({
	defCode: String,
	defVersion: Number,
	defXml: String
});

const proTable = ref();
// 表格配置项
const columns: ColumnProps<WorkflowBtn.BindForm>[] = [
	{ type: "selection", fixed: "left", width: 50 },
	{ prop: "btnText", label: "按钮文本", width: 150 },
	{ prop: "btnColor", label: "按钮颜色", width: 150 },
	{ prop: "btnFun", label: "执行方法", width: 150 },
	{ prop: "bindNodeNames", label: "绑定环节" },
	{ prop: "operation", label: "操作", width: 150 }
];

// 打开按钮绑定表单
const formRef = ref<InstanceType<typeof DefConfigBtnBindForm>>();
const openForm = (title: string, rowData: Partial<WorkflowBtn.Form> = {}) => {
	formRef.value?.acceptParams({
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: title === "新增" || title === "编辑" ? addOrEditWorkflowBtnBindApi : undefined,
		getTableList: proTable.value.getTableList
	});
};

// 删除按钮绑定
const handleDelete = async (params: WorkflowBtn.Form) => {
	if (!params.btnId) return;
	await useHandleData(multiDeleteWorkflowBtnBindApi, { id: [params.btnId] }, `删除【${params.btnText}】流程按钮`);
	proTable.value.getTableList();
};

// 批量删除按钮绑定
const handleMultiDelete = async (id: string[]) => {
	await useHandleData(multiDeleteWorkflowBtnBindApi, { id }, `删除所选流程按钮`);
	proTable.value.clearSelection();
	proTable.value.getTableList();
};
</script>

<style scoped lang="scss">
.table-box {
	width: 100%;
}
</style>
