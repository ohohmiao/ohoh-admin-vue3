<template>
	<div class="container">
		<div class="designer-left" ref="designerRef"></div>
	</div>
</template>

<script setup lang="ts">
import { nextTick, shallowRef, onMounted, ref } from "vue";
import { EditorSettings } from "@/components/BpmnDesign/settings";
import initModeler from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram } from "@/components/BpmnDesign/utils/createNewDiagram";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";
import { Element } from "bpmn-js/lib/model/Types";
import Modeler from "bpmn-js/lib/Modeler";

const props = defineProps({
	defXml: String
});

const editorSettings: EditorSettings = {
	processEngine: "activiti",
	rendererMode: "default",
	miniMap: true,
	draggable: false
};

const designerRef = shallowRef<HTMLDivElement | null>(null);

// 定义图形点击事件
type EmitProps = {
	(e: "element-click", element: Element): void;
	(e: "modeler-init", modeler: Modeler): void;
};
const emit = defineEmits<EmitProps>();

const initBpmnDesigner = async (defXml: string) => {
	const modelerModules = modulesAndModdle(ref(editorSettings));
	await nextTick();
	initModeler(designerRef, modelerModules);
	await createNewDiagram(defXml, editorSettings, emit);
};

onMounted(() => {
	if (props.defXml) {
		initBpmnDesigner(props.defXml);
	}
});

defineExpose({
	initBpmnDesigner
});
</script>

<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style scoped lang="scss"></style>
