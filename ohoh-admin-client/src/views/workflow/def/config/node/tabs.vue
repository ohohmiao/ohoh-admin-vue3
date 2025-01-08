<template>
	<el-dialog v-model="formVisible" :title="formTitle" fullscreen :close-on-click-modal="false" destroy-on-close> </el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";

interface FormProps {
	nodeId: string;
	nodeName: string;
	defCode: string;
	defVersion: number;
}

const formVisible = ref(false);
const formTitle = ref();

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	formTitle.value = `[${params.nodeName}-${params.nodeId}]环节配置`;

	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
