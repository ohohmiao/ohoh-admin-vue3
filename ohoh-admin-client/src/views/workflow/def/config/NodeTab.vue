<template>
	<div class="container">
		<div class="designer-left" ref="designerRef" @modeler-init="handleModelerInit"></div>
	</div>
</template>

<script setup lang="ts">
import { nextTick, shallowRef, onMounted, ref } from "vue";
import initModeler from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram } from "@/components/BpmnDesign/utils/createNewDiagram";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";
import Modeler from "bpmn-js/lib/Modeler";

import { EditorSettings } from "@/components/BpmnDesign/settings";

const props = defineProps({
	defXml: String
});

const defaultSettings: EditorSettings = {
	processEngine: "activiti",
	rendererMode: "default",
	miniMap: true,
	draggable: false
};

const designerRef = shallowRef<HTMLDivElement | null>(null);

const initBpmnDesigner = async (defXml: string) => {
	const modelerModules = modulesAndModdle(ref(defaultSettings));
	await nextTick();
	initModeler(designerRef, modelerModules);
	await createNewDiagram(defXml, defaultSettings);
};

onMounted(() => {
	if (props.defXml) {
		initBpmnDesigner(props.defXml);
	}
});

const handleModelerInit = (modeler: Modeler) => {
	console.info("初始化成功");
	console.info(modeler);
};

defineExpose({
	initBpmnDesigner
});
</script>

<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style scoped lang="scss"></style>
