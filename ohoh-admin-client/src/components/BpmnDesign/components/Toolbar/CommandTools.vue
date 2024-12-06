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

const undo = () => {};
const redo = () => {};
const restart = () => {};
</script>
