<template>
	<el-button-group>
		<el-tooltip effect="light" content="撤销" placement="bottom">
			<el-button @click="undo()"><LucideIcon name="Undo2"></LucideIcon></el-button>
		</el-tooltip>
		<el-tooltip effect="light" content="恢复" placement="bottom">
			<el-button @click="redo()"><LucideIcon name="Redo2"></LucideIcon></el-button>
		</el-tooltip>
		<el-tooltip effect="light" content="擦除重做" placement="bottom">
			<el-button @click="restart()"><LucideIcon name="Eraser"></LucideIcon></el-button>
		</el-tooltip>
	</el-button-group>
</template>

<script setup lang="ts">
import LucideIcon from "@/components/LucideIcon/index.vue";
import Modeler from "bpmn-js/lib/Modeler";
import type CommandStack from "diagram-js/lib/command/CommandStack";
import EventEmitter from "@/components/BpmnDesign/utils/eventEmitter";
import { createNewDiagram } from "@/components/BpmnDesign/utils/createNewDiagram";

let command: CommandStack | null = null;

EventEmitter.on("modeler-init", (modeler: Modeler) => {
	command = modeler.get<CommandStack>("commandStack");
});

const undo = () => {
	command && command.canUndo() && command.undo();
};
const redo = () => {
	command && command.canRedo() && command.redo();
};
const restart = () => {
	command && command.clear();
	createNewDiagram();
};
</script>
