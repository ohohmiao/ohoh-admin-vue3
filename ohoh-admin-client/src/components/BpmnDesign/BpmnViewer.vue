<template>
	<div class="container">
		<div class="designer-left" ref="designerRef"></div>
	</div>
</template>

<script setup lang="ts">
import { nextTick, shallowRef, onMounted, ref, onUnmounted } from "vue";
import { EditorSettings } from "@/components/BpmnDesign/settings";
import { initModeler4Viewer } from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram4Viewer } from "@/components/BpmnDesign/utils/createNewDiagram";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";
import { Element } from "bpmn-js/lib/model/Types";
import Modeler from "bpmn-js/lib/Modeler";

const props = defineProps({
	bpmnXml: String
});

const editorSettings: EditorSettings = {
	processEngine: "activiti",
	rendererMode: "default",
	miniMap: true,
	draggable: false
};

const designerRef = shallowRef<HTMLDivElement | null>(null);
let modeler: Modeler;

// 定义图形点击事件
type EmitProps = {
	(e: "element-click", element: Element): void;
	(e: "modeler-init", modeler: Modeler): void;
};
const emit = defineEmits<EmitProps>();

const initBpmnDesigner = async (defXml: string) => {
	const modelerModules = modulesAndModdle(ref(editorSettings));
	await nextTick();
	modeler = initModeler4Viewer(designerRef, modelerModules);
	await createNewDiagram4Viewer(modeler, defXml, editorSettings);
	emit("modeler-init", modeler);
	modeler.on("element.click", event => {
		const { element } = event;
		if (!event || !element.parent || element.type === "bpmn:Process") {
			return false;
		} else {
			element.name = element.di.bpmnElement.name;
			emit("element-click", element);
		}
	});
};

onMounted(() => {
	if (props.bpmnXml) {
		initBpmnDesigner(props.bpmnXml);
	}
});

onUnmounted(() => {
	modeler.destroy();
});
</script>

<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style scoped lang="scss"></style>
