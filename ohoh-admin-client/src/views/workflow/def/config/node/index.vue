<template>
	<BpmnViewer :bpmn-xml="props.defXml" @element-click="handleElementClick"></BpmnViewer>
	<DefConfigNodeTabs ref="defConfigNodeTabs"></DefConfigNodeTabs>
</template>

<script setup lang="ts">
import { defineProps, shallowRef } from "vue";
import BpmnViewer from "@/components/BpmnDesign/BpmnViewer.vue";
import { Element } from "bpmn-js/lib/model/Types";
import DefConfigNodeTabs from "./tabs.vue";

const props = defineProps({
	defXml: String,
	defCode: String,
	defVersion: Number
});

const defConfigNodeTabs = shallowRef<InstanceType<typeof DefConfigNodeTabs>>();

const handleElementClick = (element: Element) => {
	if (element.type === "bpmn:Task") {
		const params = {
			nodeId: element.id,
			nodeName: String(element.name),
			defCode: props.defCode || "",
			defVersion: props.defVersion || -1
		};
		defConfigNodeTabs.value?.acceptParams(params);
	}
};
</script>

<style scoped lang="scss"></style>
