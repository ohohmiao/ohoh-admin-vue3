<template>
	<div class="process-dialog">
		<el-dialog
			v-model="formVisible"
			:title="formProps.flowInfo.defName"
			fullscreen
			:close-on-click-modal="false"
			destroy-on-close
		>
			<el-tabs v-model="activeName" tab-position="left">
				<el-tab-pane label="业务表单" name="busTab">
					<el-card>
						<template #header v-if="formProps.flowInfo.flowBtns?.length">
							<FlowBtnGroup
								:def-code="formProps.flowInfo.defCode!"
								:def-version="formProps.flowInfo.defVersion!"
								:node-prop="formProps.flowInfo.curNodeInfo!"
								:flow-btns="formProps.flowInfo.flowBtns"
								@do-workflow-submit="doWorkflowSubmit"
								@do-workflow-close="doWorkflowClose"
							></FlowBtnGroup>
						</template>
						<!--						<el-form ref="formRef" label-width="120" label-suffix=" :" :rules="formRules" :model="formProps.rowData">-->
						<!--							-->
						<!--						</el-form>-->
					</el-card>
				</el-tab-pane>
				<el-tab-pane label="审批过程" name="processTab" v-if="!formProps.flowInfo.startFlowFlag"> </el-tab-pane>
				<el-tab-pane label="流程图" name="chartTab">
					<BpmnViewer ref="flowChartRef" @modeler-init="handleFlowChartModelerInit"></BpmnViewer>
				</el-tab-pane>
			</el-tabs>
		</el-dialog>
	</div>
</template>

<script setup lang="ts">
import { defineExpose, ref, nextTick, onMounted } from "vue";
import { Workflow } from "@/api/modules/workflow/core";
import FlowBtnGroup from "@/views/workflow/component/FlowBtnGroup.vue";
import BpmnViewer from "@/components/BpmnDesign/BpmnViewer.vue";
import Modeler from "bpmn-js/lib/Modeler";
import type ElementRegistry from "diagram-js/lib/core/ElementRegistry";
import type Modeling from "bpmn-js/lib/features/modeling/Modeling";

interface FormProps {
	flowInfo: Partial<Workflow.FlowInfo>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	flowInfo: {}
});
const activeName = ref();
const flowChartRef = ref<typeof BpmnViewer>();

// 流程图初始化
const handleFlowChartModelerInit = (modeler: Modeler) => {
	if (!formProps.value.flowInfo.curRunningNodeIds) return;
	const elementRegistry = modeler.get<ElementRegistry>("elementRegistry");
	const nodeList = elementRegistry.getAll().filter(node => node.type === "bpmn:Task");
	const modeling = modeler.get<Modeling>("modeling");
	const curRunningNodeIdArray = formProps.value.flowInfo.curRunningNodeIds.split(",");
	nodeList.forEach((node: any) => {
		if (curRunningNodeIdArray.indexOf(node.id) != -1) {
			modeling.setColor(node, { fill: "#fff", stroke: "#ff0000" });
		}
	});
};

const doWorkflowSubmit = (callback: Function) => {
	callback(true, { a: 1 });
};

const doWorkflowClose = () => {
	formProps.value.getTableList!();
	formVisible.value = false;
};

// 动态表单mounted完成回调通知
const emit = defineEmits<{
	(e: "dynamicFormInit"): void;
}>();
onMounted(() => {
	emit("dynamicFormInit");
});

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	formProps.value.flowInfo = params.flowInfo;
	formProps.value.getTableList = params.getTableList || (() => {});
	activeName.value = "busTab";
	formVisible.value = true;

	await nextTick();
	flowChartRef.value?.initBpmnDesigner(formProps.value.flowInfo.defXml);
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss">
.process-dialog {
	:deep(.el-dialog__header) {
		border-bottom: none;
	}
	.el-tabs--left :deep(.el-tabs__content) {
		height: 100%;
		.el-tab-pane {
			height: calc(100vh - 86px);
		}
	}
}
</style>
