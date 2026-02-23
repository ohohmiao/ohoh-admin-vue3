<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="待我审批列表"
			:columns="formColumns"
			:requestApi="getMyApprovalPageApi"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
		>
		</ProTable>
	</div>
</template>

<script setup lang="tsx">
import { ref } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { getMyApprovalPageApi, Workflow } from "@/api/modules/workflow/core";
import { ColumnProps } from "@/components/ProTable/interface";

const proTable = ref();

const formColumns: ColumnProps<Workflow.ProcessInstanceForm>[] = [
	{ type: "index", label: "#", width: 80 },
	{ prop: "processNum", label: "流水号", width: 150, search: { el: "input" } },
	{ prop: "processSubject", label: "流程标题", search: { el: "input" } },
	{ prop: "taskNodename", label: "办理环节", width: 150 },
	{ prop: "creatorName", label: "发起人", width: 150 },
	{ prop: "processStarttime", label: "发起时间", width: 150 },
	{ prop: "taskStarttime", label: "任务创建时间", width: 150 },
	{ prop: "taskDeadline", label: "任务截止时间", width: 150 }
];
</script>

<style scoped lang="scss"></style>
