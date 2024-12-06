<template>
	<el-button-group>
		<el-tooltip effect="light" content="缩小视图" placement="bottom">
			<el-button @click="zoomOut()"><LucideIcon name="ZoomOut"></LucideIcon></el-button>
		</el-tooltip>
		<el-tooltip effect="light" content="重置缩放" placement="bottom">
			<el-button @click="zoomReset('fit-viewport')">
				<span style="display: inline-block; line-height: 16px">{{ Math.floor(currentScale * 10) * 10 + "%" }}</span>
			</el-button>
		</el-tooltip>
		<el-tooltip effect="light" content="放大视图" placement="bottom">
			<el-button @click="zoomIn()"><LucideIcon name="ZoomIn"></LucideIcon></el-button>
		</el-tooltip>
	</el-button-group>
</template>

<script setup lang="ts">
import { ref } from "vue";
import LucideIcon from "@/components/LucideIcon/index.vue";

const currentScale = ref(1);

const zoomOut = () => {};

const zoomReset = (param: string) => {
	console.info(param);
};

const zoomIn = () => {};
</script>
