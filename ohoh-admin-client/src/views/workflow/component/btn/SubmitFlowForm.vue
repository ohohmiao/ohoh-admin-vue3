<template>
	<el-dialog v-model="formVisible" destroy-on-close :close-on-click-modal="false" :size="450" title="提交流程">
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";

const formVisible = ref(false);

// 接收父组件传过来的参数
const acceptParams = () => {
	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
