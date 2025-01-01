<template>
	<el-dialog v-model="formVisible" title="流程配置" fullscreen :close-on-click-modal="false" destroy-on-close>
		<el-tabs v-model="activeName" tab-position="left">
			<el-tab-pane label="基本信息" name="basicTab"></el-tab-pane>
			<el-tab-pane label="表单绑定" name="formTab"></el-tab-pane>
			<el-tab-pane label="事件绑定" name="eventTab"></el-tab-pane>
			<el-tab-pane label="按钮绑定" name="buttonTab"></el-tab-pane>
			<el-tab-pane label="环节配置" name="nodeTab"></el-tab-pane>
		</el-tabs>
	</el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";

interface FormProps {
	defCode: string;
	defVersion: number;
	getTableList?: () => void;
}

const formVisible = ref(false);
const activeName = ref("basicTab");

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	console.info(params);
	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
