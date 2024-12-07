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
import type Modeler from "bpmn-js/lib/Modeler";
import type Canvas from "diagram-js/lib/core/Canvas";
import { CanvasEvent } from "diagram-js/lib/core/EventBus";
import EventEmitter from "@/components/BpmnDesign/utils/eventEmitter";

const currentScale = ref(1);
let canvas: Canvas | null = null;

EventEmitter.on("modeler-init", (modeler: Modeler) => {
	try {
		canvas = modeler.get<Canvas>("canvas");
		currentScale.value = canvas.zoom();
	} finally {
		modeler.on("canvas.viewbox.changed", ({ viewbox }: CanvasEvent) => {
			currentScale.value = viewbox.scale;
		});
	}
});

const zoomOut = (newScale?: number) => {
	currentScale.value = newScale || Math.floor(currentScale.value * 100 - 0.1 * 100) / 100;
	zoomReset(currentScale.value);
};

const zoomReset = (newScale: any) => {
	canvas && canvas.zoom(newScale, newScale === "fit-viewport" ? undefined : { x: 0, y: 0 });
};

const zoomIn = (newScale?: number) => {
	currentScale.value = newScale || Math.floor(currentScale.value * 100 + 0.1 * 100) / 100;
	zoomReset(currentScale.value);
};
</script>
