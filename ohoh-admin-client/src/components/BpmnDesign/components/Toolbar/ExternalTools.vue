<template>
	<el-button-group>
		<el-tooltip effect="light" content="开启/关闭流程模拟" placement="bottom">
			<el-button @click="mockSimulation()"><LucideIcon name="Bot"></LucideIcon></el-button>
		</el-tooltip>
		<el-tooltip effect="light" content="展开/收起小地图" placement="bottom" v-if="minimapStatus">
			<el-button @click="minimapToggle()"><LucideIcon name="Map"></LucideIcon></el-button>
		</el-tooltip>
	</el-button-group>
</template>

<script setup lang="ts">
import { computed } from "vue";
import LucideIcon from "@/components/LucideIcon/index.vue";
import ToggleMode from "bpmn-js-token-simulation/lib/features/toggle-mode/modeler/ToggleMode";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";
import BpmnEditorState from "@/stores/modules/bpmn/editor";

const moduleStore = BpmnModelerState();

let minimap: any | null = null;
const minimapStatus = computed(() => BpmnEditorState().getEditorConfig.miniMap);

const mockSimulation = () => {
	moduleStore.getModeler!.get<ToggleMode>("toggleMode").toggleMode();
};

const minimapToggle = () => {
	!minimap && (minimap = moduleStore.getModeler!.get("minimap"));
	minimap && minimap.toggle();
};
</script>
