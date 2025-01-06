<template>
	<div class="container">
		<div class="designer-left designer-with-bg" ref="designerRef"></div>
	</div>
</template>

<script setup lang="ts">
import { nextTick, shallowRef, onMounted } from "vue";
import { storeToRefs } from "pinia";
import BpmnEditorState from "@/stores/modules/bpmn/editor";
import initModeler from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram } from "@/components/BpmnDesign/utils/createNewDiagram";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";

const props = defineProps({
	defXml: String
});

const designerRef = shallowRef<HTMLDivElement | null>(null);
const editorStore = BpmnEditorState();
const { editorSettings } = storeToRefs(editorStore);

const initBpmnDesigner = async (defXml: string) => {
	//console.info(`触发了：${defXml}`);
	const modelerModules = modulesAndModdle(editorSettings);
	await nextTick();
	initModeler(designerRef, modelerModules);
	await createNewDiagram(defXml);
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
