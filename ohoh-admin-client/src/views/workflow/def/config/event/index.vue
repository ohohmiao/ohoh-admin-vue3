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
				<el-button type="primary" :icon="CirclePlus" @click="openEventForm('新增')">新增</el-button>
			</template>
		</ProTable>
	</div>
	<!-- 流程事件绑定表单 -->
	<DefConfigEventForm ref="defConfigEventFormRef" :def-code="props.defCode" :def-version="props.defVersion"></DefConfigEventForm>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";
import { CirclePlus } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { WorkflowEvent, getWorkflowEventPageApi, addWorkflowEventApi, editWorkflowEventApi } from "@/api/modules/workflow/event";
import DefConfigEventForm from "./form.vue";

const props = defineProps({
	defCode: String,
	defVersion: Number
});

const proTable = ref();
// 表格配置项
const columns: ColumnProps<WorkflowEvent.Form>[] = [
	{
		prop: "eventType",
		label: "事件类别",
		width: 150,
		enum: [
			{ label: "存储", value: 0 },
			{ label: "前置", value: 1 },
			{ label: "后置", value: 2 },
			{ label: "判断", value: 3 },
			{ label: "读取", value: 4 }
		]
	},
	{ prop: "eventName", label: "事件名称", width: 300 },
	{ prop: "bindNodeNames", label: "绑定的流程节点名称" },
	{ prop: "operation", label: "操作", width: 150 }
];

// 打开事件绑定表单
const defConfigEventFormRef = ref<InstanceType<typeof DefConfigEventForm>>();
const openEventForm = (title: string, rowData: Partial<WorkflowEvent.Form> = {}) => {
	defConfigEventFormRef.value?.acceptParams({
		title,
		rowData: { ...rowData },
		isView: title === "查看",
		api: title === "新增" ? addWorkflowEventApi : title === "编辑" ? editWorkflowEventApi : undefined,
		getTableList: proTable.value.getTableList
	});
};
</script>

<style scoped lang="scss">
.table-box {
	width: 100%;
}
</style>
