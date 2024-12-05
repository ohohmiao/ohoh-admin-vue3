<template>
	<div class="my-dialog">
		<el-dialog v-model="formVisible" fullscreen :show-close="false" :close-on-click-modal="false" destroy-on-close>
			<div class="my-container">
				<div class="toolbar">
					<div class="flex"></div>
				</div>
				<div class="designer">
					<!--<div class="designer-left designer-with-bg" ref="designerRef"></div>-->
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
<style scoped lang="scss">
.my-dialog :deep(.el-dialog__header) {
	display: none;
}
.my-container {
	position: absolute;
	top: 0;
	left: 0;
	display: flex;
	flex-direction: column;
	width: 100%;
	height: 100%;
	overflow: hidden;
	.toolbar {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		width: 100%;
		padding: 8px 16px;
		box-sizing: border-box;
		.flex {
			flex: 1;
			display: flex;
			align-items: center;
			flex-wrap: wrap;
			:deep(.el-button-group) {
				margin-right: 16px;
			}
		}
	}
	.designer {
		display: flex;
		flex: 1;
		height: 100%;
		overflow: hidden;
		&-left {
			flex: 1;
			height: 100%;
			overflow: hidden;
			&.designer-with-bg {
				background: url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImEiIHdpZHRoPSI0MCIgaGVpZ2h0PSI0MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTTAgMTBoNDBNMTAgMHY0ME0wIDIwaDQwTTIwIDB2NDBNMCAzMGg0ME0zMCAwdjQwIiBmaWxsPSJub25lIiBzdHJva2U9IiNlMGUwZTAiIG9wYWNpdHk9Ii4yIi8+PHBhdGggZD0iTTQwIDBIMHY0MCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZTBlMGUwIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2EpIi8+PC9zdmc+")
					repeat !important;
			}
		}
		&-right {
			width: 230px;
			height: 100%;
			:deep(.card) {
				height: 100%;
				margin-bottom: 0 !important;
				.el-card__body {
					padding: 10px !important;
				}
			}
		}
	}
}
</style>
