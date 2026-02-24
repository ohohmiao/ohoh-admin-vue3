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
		<!-- 动态流程表单 -->
		<component
			:is="DynamicFlowForm"
			v-if="DynamicFlowForm"
			ref="dynamicFlowFormRef"
			@dynamic-form-init="handleDynamicFlowFormInit"
		></component>
	</div>
</template>

<script setup lang="tsx">
import { ref, shallowRef, defineAsyncComponent } from "vue";
import type { DefineComponent } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { getMyApprovalPageApi, getWorkflowFlowInfoApi, Workflow } from "@/api/modules/workflow/core";
import { ColumnProps } from "@/components/ProTable/interface";

const proTable = ref();

const formColumns: ColumnProps<Workflow.ProcessInstanceForm>[] = [
	{ type: "index", label: "#", width: 80 },
	{ prop: "processNum", label: "流水号", width: 200, search: { el: "input" } },
	{
		prop: "processSubject",
		label: "流程标题",
		search: { el: "input" },
		render: scope => {
			return (
				<el-link underline="always" href="javascript:;" onClick={() => handleMyApproval(scope.row)}>
					{scope.row.processSubject}
				</el-link>
			);
		}
	},
	{ prop: "taskNodename", label: "办理环节", width: 150 },
	{ prop: "creatorName", label: "发起人", width: 100 },
	{ prop: "taskStarttime", label: "任务创建时间", width: 150 },
	{ prop: "taskDeadline", label: "任务截止时间", width: 150 }
];

// 动态表单异步组件
const DynamicFlowForm = shallowRef();
const dynamicFlowFormRef = shallowRef();
// Vite自动扫描指定目录下的Vue文件
const dynamicFlowFormViews = import.meta.glob("@/views/workflow/workspace/**/*.vue");
// 流程核心信息
let thizFlowInfo: Workflow.FlowInfo<any>;

// 办理流程
const handleMyApproval = async (item: any) => {
	const { data } = await getWorkflowFlowInfoApi({ processId: item.processId, curTaskId: item.taskId });
	const dynamicFlowFormPath = `/src/views/workflow/workspace${data.formPath}.vue`;
	const thizLoader = dynamicFlowFormViews[dynamicFlowFormPath];
	thizFlowInfo = data;
	if (!thizLoader) {
		console.warn("表单组件未找到：", dynamicFlowFormPath);
		return;
	}
	DynamicFlowForm.value = defineAsyncComponent(thizLoader as () => Promise<DefineComponent>);
};

// 动态表单加载完成回调
const handleDynamicFlowFormInit = () => {
	dynamicFlowFormRef.value?.acceptParams({
		flowInfo: thizFlowInfo,
		getTableList: proTable.value.getTableList
	});
};
</script>

<style scoped lang="scss"></style>
