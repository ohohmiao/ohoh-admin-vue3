<template>
	<el-button-group>
		<el-tooltip effect="light" content="开启/关闭流程模拟" placement="bottom">
			<el-button @click="mockSimulation()"><LucideIcon name="Bot"></LucideIcon></el-button>
		</el-tooltip>
		<el-tooltip effect="light" content="展开/收起小地图" placement="bottom">
			<el-button @click="minimapToggle()"><LucideIcon name="Map"></LucideIcon></el-button>
		</el-tooltip>
	</el-button-group>
</template>

<script setup lang="ts">
import LucideIcon from "@/components/LucideIcon/index.vue";

const mockSimulation = () => {};
const minimapToggle = () => {};
</script>
