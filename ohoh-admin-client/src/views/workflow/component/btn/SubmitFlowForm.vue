<template>
	<el-dialog v-model="formVisible" destroy-on-close :close-on-click-modal="false" :size="450" title="提交流程">
		<el-form ref="formRef">
			<!-- 下一步环节 -->
			<el-row v-for="(nextHandler, nextHandlerIndex) in formProps?.taskNodeList" :key="nextHandlerIndex">
				<!-- 一般/并行 -->
				<template v-if="nextHandler.nodeList == undefined">
					<el-col :span="12">
						<el-form-item
							label="下一步环节"
							:prop="'formProps?.taskNodeList.' + nextHandlerIndex + '.nodeName'"
							:rules="[{ required: true, trigger: 'blur', message: '下一步环节不能为空' }]"
						>
							<el-tag size="large" :title="nextHandler.nodeName">
								{{ nextHandler.nodeName.length <= 15 ? nextHandler.nodeName : nextHandler.nodeName.substring(0, 15) + "..." }}
							</el-tag>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="nextHandler.nodeType == Workflow.NodeTypeEnum.TASK">
						<el-form-item
							label="环节审核人"
							:prop="'formProps?.taskNodeList.' + nextHandlerIndex + '.handlers'"
							:rules="[{ required: true, type: 'array', trigger: 'blur,change', message: '请选择环节审核人' }]"
						>
							<el-input placeholder="请选择" :value="getNextHandlerNames(nextHandlerIndex)" autocomplete="off" readonly>
								<template #append v-if="nextHandler.reselectPermit == 1">
									<el-button>选择</el-button>
								</template>
							</el-input>
						</el-form-item>
					</el-col>
				</template>
				<!-- 分支 -->
				<template v-else-if="nextHandler.nodeList.length">
					<el-col :span="12">
						<el-form-item
							label="下一步环节"
							:prop="'formProps?.taskNodeList.' + nextHandlerIndex + '.nodeId'"
							:rules="[{ required: true, trigger: 'change', message: '请选择下一步环节' }]"
						>
							<el-select
								v-model="nextHandler.nodeId"
								placeholder="请选择"
								@change="(val: string) => handleMultiNodeNextHandlerSelChange(val, nextHandlerIndex)"
							>
								<el-option
									v-for="item in nextHandler.nodeList"
									:key="item.nodeId"
									:label="item.nodeName"
									:value="item.nodeId"
								></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12" v-if="nextHandler.nodeType == Workflow.NodeTypeEnum.TASK">
						<el-form-item
							label="环节审核人"
							:prop="'formProps?.taskNodeList.' + nextHandlerIndex + '.handlers'"
							:rules="[{ required: true, type: 'array', trigger: 'blur,change', message: '请选择环节审核人' }]"
						>
							<el-input placeholder="请选择" :value="getNextHandlerNames(nextHandlerIndex)" autocomplete="off" readonly>
								<template #append v-if="nextHandler.reselectPermit == 1">
									<el-button>选择</el-button>
								</template>
							</el-input>
						</el-form-item>
					</el-col>
				</template>
			</el-row>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" @click="handleSubmit">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { WorkflowNode } from "@/api/modules/workflow/node";
import { Workflow } from "@/api/modules/workflow/core";
import { FormInstance } from "element-plus";

interface FormProps {
	nodeProp: WorkflowNode.Form;
	taskNodeList: Workflow.FlowTaskNode[];
}

const formVisible = ref(false);
const formProps = ref<FormProps>();

const getNextHandlerNames = (index: number) => {
	return formProps.value?.taskNodeList[index].handlers.map(handler => handler.handlerName).join(",");
};

const handleMultiNodeNextHandlerSelChange = (nodeId: string, index: number) => {
	const selectedNode = formProps.value?.taskNodeList[index].nodeList?.find(node => node.nodeId == nodeId);
	if (selectedNode) {
		console.info(selectedNode);
		formProps.value!.taskNodeList[index].nodeId = selectedNode.nodeId;
		formProps.value!.taskNodeList[index].nodeName = selectedNode.nodeName;
		formProps.value!.taskNodeList[index].nodeType = selectedNode.nodeType;
		formProps.value!.taskNodeList[index].handlers = selectedNode.handlers;
		formProps.value!.taskNodeList[index].multiHandletype = selectedNode.multiHandletype;
		formProps.value!.taskNodeList[index].reselectPermit = selectedNode.reselectPermit;
		formProps.value!.taskNodeList[index].inclusiveGateWayId = selectedNode.inclusiveGateWayId;
	}
};

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	formProps.value = params;

	formVisible.value = true;
};

// 提交表单
const formRef = ref<FormInstance>();
const handleSubmit = () => {};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
