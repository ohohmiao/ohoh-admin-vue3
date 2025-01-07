<template>
	<div class="designer-left designer-with-bg" ref="designerRef"></div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, shallowRef } from "vue";
import { storeToRefs } from "pinia";
import BpmnEditorState from "@/stores/modules/bpmn/editor";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";
import initModeler from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram } from "@/components/BpmnDesign/utils/createNewDiagram";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";
import BpmnModdle from "bpmn-moddle";

const editorStore = BpmnEditorState();
const { editorSettings } = storeToRefs(editorStore);
const designerRef = shallowRef<HTMLDivElement | null>(null);
const modelerStore = BpmnModelerState();
const moddle = new BpmnModdle();

const initBpmnDesigner = async (defXml?: string) => {
	const modelerModules = modulesAndModdle(editorSettings);
	await nextTick();
	initModeler(designerRef, modelerModules);
	await createNewDiagram(defXml);
};

const getModelerData = async () => {
	const modeler = modelerStore.getModeler!;
	const { xml } = await modeler.saveXML({ format: true, preamble: true });
	const { svg } = await modeler.saveSVG();
	const jsonStr = await moddle.fromXML(xml!);
	return {
		xml: xml,
		svg: svg,
		json: JSON.stringify(jsonStr)
	};
};

onMounted(() => {
	// TODO 屏蔽浏览器右键
	// document.body.addEventListener("contextmenu", function (ev) {
	// 	ev.preventDefault();
	// });
});

defineExpose({
	initBpmnDesigner,
	getModelerData
});
</script>

<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style src="@/components/BpmnDesign/styles/toolbar.scss"></style>
<style scoped lang="scss"></style>
