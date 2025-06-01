<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="下一步办理人列表"
			:columns="columns"
			:request-api="getWorkflowHandlerPageApi"
			:initParam="{ defCode: props.defCode, defVersion: props.defVersion, nodeId: props.nodeId }"
			:toolButton="false"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
		>
			<!-- 表格 header 按钮 -->
			<template #tableHeader>
				<el-button type="primary" @click="openHandlerForm('新增')">新增</el-button>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" link @click="openHandlerForm('编辑', scope.row)">编辑</el-button>
				<el-button type="primary" link @click="handleDeleteHandler(scope.row)">删除</el-button>
			</template>
		</ProTable>
	</div>
	<!-- 下一步办理人配置对话框 -->
	<DefNodeHandlerForm
		ref="formRef"
		:def-code="props.defCode"
		:def-version="props.defVersion"
		:node-id="props.nodeId"
		:next-task-node-list="props.nextTaskNodeList"
	></DefNodeHandlerForm>
</template>

<script setup lang="ts">
import { ref, defineProps, withDefaults } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { WorkflowHandler, getWorkflowHandlerPageApi, deleteWorkflowHandlerApi } from "@/api/modules/workflow/handler";
import DefNodeHandlerForm from "./DefNodeHandlerForm.vue";
import { useHandleData } from "@/hooks/useHandleData";

const props = withDefaults(
	defineProps<{
		defCode: string;
		defVersion: number;
		nodeId: string;
		nextTaskNodeList: { label: string; value: string }[];
	}>(),
	{
		nextTaskNodeList: () => []
	}
);

const proTable = ref();
// 表格配置项
const columns: ColumnProps<WorkflowHandler.Form>[] = [
	{ type: "index", label: "#", width: 50 },
	{ prop: "nextnodeName", label: "下一环节", width: 300 },
	{
		prop: "handlerType",
		label: "办理人类型",
		width: 200,
		enum: [
			{ label: "指定人员", value: 0 },
			{ label: "指定接口", value: 1 },
			{ label: "自行选择", value: 2 }
		]
	},
	{ prop: "filterRule", label: "过滤方式" },
	{
		prop: "multiHandletype",
		label: "多人审核方式",
		width: 200,
		enum: [
			{ label: "并审", value: 0 },
			{ label: "串审", value: 1 }
		]
	},
	{ prop: "operation", label: "操作", width: 150 }
];

// 打开下一环节办理人配置表单
const formRef = ref<InstanceType<typeof DefNodeHandlerForm>>();
const openHandlerForm = (title: string, rowData: Partial<WorkflowHandler.Form> = {}) => {
	formRef.value?.acceptParams({
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: undefined,
		getTableList: proTable.value.getTableList
	});
};
// 删除办理人配置
const handleDeleteHandler = async (params: WorkflowHandler.Form) => {
	if (!params.handlerId) return;
	await useHandleData(deleteWorkflowHandlerApi, { id: params.handlerId }, `删除【${params.nextnodeName}】环节办理人配置`);
	proTable.value.getTableList();
};
</script>

<style scoped lang="scss">
.table-box {
	width: 100%;
}
</style>
