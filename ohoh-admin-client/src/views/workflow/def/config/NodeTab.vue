<template>
	<BpmnViewer :bpmn-xml="props.defXml" @element-click="handleElementClick" @modeler-init="handleModelerInit"></BpmnViewer>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import BpmnViewer from "@/components/BpmnDesign/BpmnViewer.vue";
import { Element } from "bpmn-js/lib/model/Types";
import Modeler from "bpmn-js/lib/Modeler";
import type ElementRegistry from "diagram-js/lib/core/ElementRegistry";
import type Modeling from "bpmn-js/lib/features/modeling/Modeling";

const props = defineProps({
	defXml: String
});

const handleElementClick = (element: Element) => {
	console.info("外部点击捕获到了");
	console.info(element);
};

const handleModelerInit = (modeler: Modeler) => {
	const elementRegistry = modeler.get<ElementRegistry>("elementRegistry");
	// 获取任务节点集合
	const nodeList = elementRegistry.getAll().filter(node => node.type === "bpmn:Task");
	const modeling = modeler.get<Modeling>("modeling");
	nodeList.forEach(node => {
		if (node.id == "Activity_02szuig" || node.id == "Activity_1drhdip") {
			modeling.setColor(node, { fill: "#fff", stroke: "#ff0000" });
		}
	});
};
</script>

<style scoped lang="scss"></style>
