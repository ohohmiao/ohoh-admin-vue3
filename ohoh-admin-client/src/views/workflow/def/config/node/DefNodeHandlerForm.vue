<template>
	<div></div>
</template>

<script setup lang="ts">
import { WorkflowHandler } from "@/api/modules/workflow/handler";

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowHandler.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	console.info(params);
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
