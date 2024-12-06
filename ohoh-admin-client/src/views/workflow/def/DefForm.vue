<template>
	<div class="designer-dialog">
		<el-dialog v-model="formVisible" fullscreen :show-close="false" :close-on-click-modal="false" destroy-on-close>
			<div class="container">
				<div class="toolbar">
					<div class="flex">
						<AlignTools></AlignTools>
						<ScaleTools></ScaleTools>
						<CommandTools></CommandTools>
						<ExternalTools></ExternalTools>
					</div>
					<OperationTools></OperationTools>
				</div>
				<div class="designer">
					<div class="designer-left designer-with-bg" ref="designerRef"></div>
					<div class="designer-right">
						<el-button @click="formVisible = false">关闭</el-button>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="WorkflowDefForm">
import { ref, shallowRef, computed, onMounted } from "vue";
import { WetBpmnDesignMainPanelProps } from "@/components/BpmnDesign/types";
import { createNewDiagram } from "@/components/BpmnDesign/utils";
import type { BpmnModuleDeclaration } from "@/components/BpmnDesign/types";
import type Modeler from "bpmn-js/lib/Modeler";
import activitiSchema from "@/components/BpmnDesign/moddles/activiti/schema.json";
import flowableSchema from "@/components/BpmnDesign/moddles/flowable/schema.json";
import camundaSchema from "camunda-bpmn-moddle/resources/camunda.json";

import AlignTools from "@/components/BpmnDesign/toolbars/AlignTools.vue";
import ScaleTools from "@/components/BpmnDesign/toolbars/ScaleTools.vue";
import CommandTools from "@/components/BpmnDesign/toolbars/CommandTools.vue";
import ExternalTools from "@/components/BpmnDesign/toolbars/ExternalTools.vue";
import OperationTools from "@/components/BpmnDesign/toolbars/OperationTools.vue";

interface FormProps {
	[key: string]: any;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
// const formProps = ref<FormProps>({
// 	rowData: {}
// });

const ProcessModdles = {
	activiti: activitiSchema,
	camunda: camundaSchema,
	flowable: flowableSchema
};
const designerRef = shallowRef<HTMLDivElement>();
const bpmnDesignProps = defineProps(WetBpmnDesignMainPanelProps);

const moddleExtensions = computed(() => {
	const processType = bpmnDesignProps.processType || "flowable";
	const actions: Record<string, any> = {};
	actions[processType] = ProcessModdles[processType];
	return actions;
});
const modeler = shallowRef<Modeler | null>(null);
const modules = shallowRef<BpmnModuleDeclaration[]>([]);

const initBpmnDesigner = async () => {
	const res = await import("bpmn-js/lib/Modeler");
	const Modeler = res.default;
	modeler.value = new Modeler({
		container: designerRef.value,
		additionalModules: [...modules.value],
		moddleExtensions: moddleExtensions.value
	});

	await createNewDiagram(modeler.value, {
		xml: bpmnDesignProps.xml
	});
};
onMounted(() => {
	// document.body.addEventListener("contextmenu", function (ev) {
	// 	ev.preventDefault();
	// });
});

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	console.info(params);
	initBpmnDesigner();
	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>
<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style src="@/components/BpmnDesign/styles/toolbar.scss"></style>
<style scoped lang="scss">
.designer-dialog :deep(.el-dialog__header) {
	display: none;
}
</style>
