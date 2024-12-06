<template>
	<el-button-group>
		<el-tooltip effect="light" :content="item.name" placement="bottom" v-for="item in buttons" :key="item.key">
			<el-button><LucideIcon :name="item.icon"></LucideIcon></el-button>
		</el-tooltip>
	</el-button-group>
</template>

<script setup lang="ts">
import { ComputedRef, computed } from "vue";
import LucideIcon from "@/components/LucideIcon/index.vue";

const buttons: ComputedRef<{ name: string; key: string; icon: string }[]> = computed(() => {
	return [
		{ name: "左对齐", key: "left", icon: "AlignStartVertical" },
		{ name: "水平居中", key: "center", icon: "AlignCenterVertical" },
		{ name: "右对齐", key: "right", icon: "AlignEndVertical" },
		{ name: "上对齐", key: "top", icon: "AlignStartHorizontal" },
		{ name: "垂直居中", key: "middle", icon: "AlignCenterHorizontal" },
		{ name: "下对齐", key: "bottom", icon: "AlignEndHorizontal" }
	];
});
</script>
