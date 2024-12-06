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
import { ref, shallowRef, computed, inject, onMounted } from "vue";
import { WetBpmnDesignMainPanelProps } from "@/components/BpmnDesign/types";
import { bpmnstate } from "@/components/BpmnDesign/symbol";
import { createNewDiagram } from "@/components/BpmnDesign/utils";
import type { BpmnProvideType, BpmnElement, BpmnEventBus, BpmnEvent } from "@/components/BpmnDesign/types";
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

const ProcessTypes = {
	activiti: activitiSchema,
	camunda: camundaSchema,
	flowable: flowableSchema
};
const designerRef = shallowRef<HTMLDivElement>();
let rootElement: BpmnElement;
const props = defineProps(WetBpmnDesignMainPanelProps);

const moddleExtensions = computed(() => {
	const processType = props.processType || "flowable";
	const actions: Record<string, any> = {};
	actions[processType] = ProcessTypes[processType];
	return actions;
});

const { modules, modeler, seletedBpmnElement, addedBpmnElementsMap } = inject(bpmnstate) as BpmnProvideType;

const addedBpmnElements = (element: BpmnElement) => {
	if (!addedBpmnElementsMap.value[element.businessObject.id]) {
		addedBpmnElementsMap.value[element.businessObject.id] = {
			element,
			error: []
		};
	} else {
		addedBpmnElementsMap.value[element.businessObject.id].element = element;
	}
	seletedBpmnElement.value = [element];
};

const init = async () => {
	const events = [
		"shape.added",
		"shape.move.end",
		"shape.removed",
		"connect.end",
		"connect.move",
		"connection.removed",
		"connection.add",
		"selection.changed",
		"element.changed"
	];
	const res = await import("bpmn-js/lib/Modeler");
	const Modeler = res.default;
	modeler.value = new Modeler({
		container: designerRef.value,
		additionalModules: [...modules.value],
		moddleExtensions: moddleExtensions.value
	});

	const elementRegistry: any = modeler.value.get("elementRegistry");
	// 注册modeler事件
	events.forEach(event => {
		modeler.value?.on(event, (e: BpmnEvent) => {
			const element = e.element;
			if (event === "selection.changed") {
				const $element = e.newSelection?.[0];
				if ($element) {
					addedBpmnElements($element);
				} else {
					seletedBpmnElement.value = [rootElement];
				}
			} else if (event === "shape.removed" || event === "connection.removed") {
				// console.log('connection.removed')
				seletedBpmnElement.value = [rootElement];
				addedBpmnElementsMap.value[element.businessObject.id] = null;
				delete addedBpmnElementsMap.value[element.businessObject.id];
			}
		});
	});
	// 注册diagram element事件
	const eventBus = modeler.value.get("eventBus") as BpmnEventBus<string>;
	const elementEvents = ["element.click", "element.changed"];
	elementEvents.forEach(event => {
		eventBus.on(event, (e: BpmnEvent) => {
			console.info(e);
		});
	});
	await createNewDiagram(modeler.value, {
		xml: props.xml
	});
	const bpmnProcessElement = elementRegistry.find((item: any) => item.type === "bpmn:Process") as BpmnElement;
	if (bpmnProcessElement) {
		rootElement = bpmnProcessElement;
		seletedBpmnElement.value = [bpmnProcessElement];
		addedBpmnElements(rootElement);
	}
};
onMounted(() => {});

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	console.info(params);
	init();
	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>
<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss" scoped></style>
<style src="@/components/BpmnDesign/styles/toolbar.scss"></style>
<style scoped lang="scss">
.designer-dialog :deep(.el-dialog__header) {
	display: none;
}
</style>
