<template>
	<el-button v-for="btn in props.flowBtns" :key="btn.btnId" :color="btn.btnColor" plain @click="dynamicInvoke(btn.btnFun)">{{
		btn.btnText
	}}</el-button>
	<SubmitFlowForm ref="submitFlowFormRef"></SubmitFlowForm>
</template>

<script setup lang="ts">
import { ref, defineProps, PropType } from "vue";
import { WorkflowBtn } from "@/api/modules/workflow/btn";
import SubmitFlowForm from "./btn/SubmitFlowForm.vue";

const props = defineProps({
	flowBtns: {
		type: Array as PropType<WorkflowBtn.Form[]>,
		required: true
	}
});

const emit = defineEmits<{
	(e: "doWorkflowSubmit", callback: Function): void;
	(e: "doWorkflowClose"): void;
}>();
const submitFlowFormRef = ref();

const preDefinedMethods: Record<string, Function> = {
	/**
	 * 执行流程
	 */
	doWorkflowSubmit: () => {
		let thizValid = true;
		let thizBusParams = null;
		emit("doWorkflowSubmit", (valid: boolean, busParams: Object) => {
			thizValid = valid;
			thizBusParams = busParams;
		});
		if (!thizValid) {
			return;
		}
		console.info(thizBusParams);
		submitFlowFormRef.value.acceptParams();
	},
	/**
	 * 关闭窗口
	 */
	doWorkflowClose: () => {
		emit("doWorkflowClose");
	}
};

const dynamicInvoke = (funName: string) => {
	if (preDefinedMethods[funName]) {
		preDefinedMethods[funName]();
	} else {
		console.warn("未定义的流程按钮执行方法：", funName);
	}
};
</script>

<style scoped lang="scss"></style>
