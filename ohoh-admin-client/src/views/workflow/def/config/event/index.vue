<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="流程事件绑定列表"
			:columns="columns"
			:requestApi="getWorkflowEventPageApi"
			:initParam="{ defCode: props.defCode, defVersion: props.defVersion }"
			:toolButton="false"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
		>
			<!-- 表格 header 按钮 -->
			<template #tableHeader>
				<el-button type="primary" @click="openEventForm('新增')">新增</el-button>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" link @click="openEventForm('编辑', scope.row)">编辑</el-button>
				<el-button type="primary" link @click="handleDeleteEvent(scope.row)">删除</el-button>
			</template>
		</ProTable>
	</div>
	<!-- 流程事件绑定表单 -->
	<DefConfigEventForm
		ref="defConfigEventFormRef"
		:def-code="props.defCode"
		:def-version="props.defVersion"
		:def-xml="props.defXml"
	></DefConfigEventForm>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import {
	WorkflowEvent,
	getWorkflowEventPageApi,
	addWorkflowEventApi,
	editWorkflowEventApi,
	deleteWorkflowEventApi
} from "@/api/modules/workflow/event";
import DefConfigEventForm from "./form.vue";
import { useHandleData } from "@/hooks/useHandleData";

const props = defineProps({
	defCode: String,
	defVersion: Number,
	defXml: String
});

const proTable = ref();
// 表格配置项
const columns: ColumnProps<WorkflowEvent.Form>[] = [
	{ type: "index", label: "#", width: 50 },
	{
		prop: "eventType",
		label: "事件类别",
		width: 100,
		enum: [
			{ label: "存储", value: 0 },
			{ label: "前置", value: 1 },
			{ label: "后置", value: 2 },
			{ label: "判断", value: 3 },
			{ label: "读取", value: 4 }
		]
	},
	{ prop: "eventName", label: "事件名称", width: 200 },
	{ prop: "bindNodeNames", label: "绑定环节" },
	{
		prop: "implType",
		label: "实现方式",
		width: 100,
		enum: [
			{ label: "本地服务", value: 0 },
			{ label: "脚本方式", value: 1 }
		]
	},
	{ prop: "implLocalservice", label: "本地服务", width: 200 },
	{ prop: "operation", label: "操作", width: 150 }
];

// 打开事件绑定表单
const defConfigEventFormRef = ref<InstanceType<typeof DefConfigEventForm>>();
const openEventForm = (title: string, rowData: Partial<WorkflowEvent.Form> = {}) => {
	defConfigEventFormRef.value?.acceptParams({
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: title === "新增" ? addWorkflowEventApi : title === "编辑" ? editWorkflowEventApi : undefined,
		getTableList: proTable.value.getTableList
	});
};

// 删除事件
const handleDeleteEvent = async (params: WorkflowEvent.Form) => {
	if (!params.eventId) return;
	await useHandleData(deleteWorkflowEventApi, { id: params.eventId }, `删除【${params.eventName}】流程事件`);
	proTable.value.getTableList();
};
</script>

<style scoped lang="scss">
.table-box {
	width: 100%;
}
</style>
