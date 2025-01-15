<template>
	<el-dialog
		v-model="formVisible"
		:title="`环节配置[${formProps.nodeName}-${formProps.nodeId}]`"
		fullscreen
		:close-on-click-modal="false"
		destroy-on-close
	>
	</el-dialog>
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
const formProps = ref<FormProps>({
	nodeId: "",
	nodeName: "",
	defCode: "",
	defVersion: 1
});

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	formProps.value = params;

	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
