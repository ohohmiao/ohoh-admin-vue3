<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="审批过程日志"
			:columns="formColumns"
			:requestApi="getWorkflowTaskLogListApi"
			:initParam="{ processId: props.processId }"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			:pagination="false"
		>
		</ProTable>
	</div>
</template>

<script setup lang="tsx">
import { ref, defineProps } from "vue";
import { getWorkflowTaskLogListApi, Workflow } from "@/api/modules/workflow/core";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";

const props = defineProps({
	processId: {
		type: String,
		required: true
	}
});

const proTable = ref();

const formColumns: ColumnProps<Workflow.ProcessTaskForm>[] = [
	{ type: "index", label: "#", width: 80 },
	{ prop: "incomingNodename", label: "上一环节", width: 150 },
	{ prop: "taskNodename", label: "办理环节", width: 150 },
	{ prop: "handlerName", label: "办理人", width: 150, render: scope => scope.row.handlerName || scope.row.assignHandlernames },
	{
		prop: "handlerOrgname",
		label: "办理人部门",
		width: 150,
		render: scope => scope.row.handlerOrgname || scope.row.assignHandlerorgnames
	},
	{ prop: "taskStarttime", label: "任务创建时间", width: 200 },
	{
		prop: "taskState",
		label: "任务状态",
		width: 100,
		tag: true,
		enum: [
			{ label: "正在办理", value: 0, tagType: "success" },
			{ label: "已办理", value: 1, tagType: "primary" },
			{ label: "已退回", value: 2, tagType: "primary" },
			{ label: "等待中", value: 3, tagType: "danger" },
			{ label: "已转办", value: 4, tagType: "primary" },
			{ label: "已办结", value: 5, tagType: "primary" },
			{ label: "已挂起", value: 6, tagType: "primary" },
			{ label: "已重启", value: 7, tagType: "primary" },
			{ label: "被追回", value: 8, tagType: "primary" },
			{ label: "被跳转", value: 9, tagType: "primary" }
		]
	},
	{ prop: "taskEndtime", label: "任务办结时间", width: 200 } /*,
	{ prop: "consumeSeconds", label: "办理时长", width: 100 },
	{ prop: "taskDeadline", label: "办理截止时间", width: 200 },
	{ prop: "overtimeFlag", label: "是否超时办理", width: 100 },
	{ prop: "exceedSeconds", label: "超时时长", width: 100 }*/
];
</script>

<style scoped lang="scss"></style>
