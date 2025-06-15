<template>
	<el-button v-for="btn in props.flowBtns" :key="btn.btnId" :color="btn.btnColor" plain @click="dynamicInvoke(btn.btnFun)">{{
		btn.btnText
	}}</el-button>
	<SubmitFlowForm ref="submitFlowFormRef"></SubmitFlowForm>
</template>

<script setup lang="ts">
import { ref, defineProps, PropType } from "vue";
import { WorkflowBtn } from "@/api/modules/workflow/btn";
import { Workflow, getWorkflowNextNodeListApi } from "@/api/modules/workflow/core";
import SubmitFlowForm from "./btn/SubmitFlowForm.vue";
import { WorkflowNode } from "@/api/modules/workflow/node";

const props = defineProps({
	defCode: {
		type: String,
		required: true
	},
	defVersion: {
		type: Number,
		required: true
	},
	nodeProp: {
		type: Object as PropType<WorkflowNode.Form>,
		required: true
	},
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
	doWorkflowSubmit: async () => {
		const { valid: thizValid, busParams: thizBusParams } = await new Promise<{ valid: boolean; busParams: Record<string, any> }>(
			resolve => {
				emit("doWorkflowSubmit", (valid: boolean, busParams: Record<string, any>) => {
					resolve({ valid, busParams });
				});
			}
		);
		if (!thizValid) {
			return;
		}
		const { data } = await getWorkflowNextNodeListApi({
			defCode: props.defCode,
			defVersion: props.defVersion,
			actType: Workflow.ActTypeEnum.SUBMIT,
			businessForm: thizBusParams
		});
		submitFlowFormRef.value.acceptParams({
			nodeProp: props.nodeProp,
			rowData: {
				defCode: props.defCode,
				defVersion: props.defVersion,
				nextHandlerList: data,
				processForm: {},
				businessForm: thizBusParams
			}
		});
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
