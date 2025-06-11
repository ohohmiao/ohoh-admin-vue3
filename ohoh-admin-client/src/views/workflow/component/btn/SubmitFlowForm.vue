<template>
	<el-dialog v-model="formVisible" destroy-on-close :close-on-click-modal="false" :size="650" title="提交流程">
		<el-form ref="formRef" :model="formProps?.rowData" label-width="110px">
			<!-- 下一步环节 -->
			<el-row v-for="(nextHandler, nextHandlerIndex) in formProps?.rowData.nextHandlerList" :key="nextHandlerIndex">
				<!-- 一般/并行 -->
				<template v-if="nextHandler.nodeList == undefined">
					<el-col :span="12">
						<el-form-item
							label="下一步环节"
							:prop="'nextHandlerList.' + nextHandlerIndex + '.nodeName'"
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
							:prop="'nextHandlerList.' + nextHandlerIndex + '.handlers'"
							:rules="[{ required: true, type: 'array', trigger: 'blur,change', message: '请选择环节审核人' }]"
						>
							<el-input placeholder="请选择" :value="getNextHandlerNames(nextHandlerIndex)" autocomplete="off" readonly>
								<template #append v-if="nextHandler.reselectPermit == 1">
									<el-button @click="openHandlerSelector(nextHandlerIndex)">选择</el-button>
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
							:prop="'nextHandlerList.' + nextHandlerIndex + '.nodeId'"
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
							:prop="'nextHandlerList.' + nextHandlerIndex + '.handlers'"
							:rules="[{ required: true, type: 'array', trigger: 'blur,change', message: '请选择环节审核人' }]"
						>
							<el-input placeholder="请选择" :value="getNextHandlerNames(nextHandlerIndex)" autocomplete="off" readonly>
								<template #append v-if="nextHandler.reselectPermit == 1">
									<el-button @click="openHandlerSelector(nextHandlerIndex)">选择</el-button>
								</template>
							</el-input>
						</el-form-item>
					</el-col>
				</template>
			</el-row>
			<el-row v-if="formProps?.nodeProp.approvalPermit == 1 || formProps?.nodeProp.processlimitPermit == 1">
				<!-- 审批结果控件 -->
				<el-col :span="12" v-if="formProps?.nodeProp.approvalPermit == 1">
					<el-form-item
						label="审核结果"
						prop="processForm.approvalResult"
						:rules="[{ required: true, message: '请选择审核结果' }]"
					>
						<el-radio-group v-model="formProps.rowData.processForm.approvalResult">
							<el-radio-button label="通过" :value="1"></el-radio-button>
							<el-radio-button label="不通过" :value="0"></el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<!-- 指定办理期限 -->
				<el-col :span="12" v-if="formProps?.nodeProp.processlimitPermit == 1">
					<el-form-item
						label="指定办理期限"
						prop="processForm.handleDeadline"
						:rules="[{ required: true, message: '请选择指定办理期限' }]"
					>
						<el-date-picker
							v-model="formProps.rowData.processForm.handleDeadline"
							type="datetime"
							value-format="YYYY-MM-DD HH:mm:ss"
							placeholder="请选择"
							style="width: 100%"
						></el-date-picker>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<el-form-item label="审核意见" prop="processForm.handleOpition">
						<el-input
							type="textarea"
							v-model.trim="formProps!.rowData.processForm.handleOpition"
							:maxlength="300"
							:rows="8"
							show-word-limit
						></el-input>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" @click="handleSubmit">确定</el-button>
		</template>
	</el-dialog>
	<!-- 审核人员选择对话框 -->
	<SysCompositeSelector
		ref="selectorRef"
		title="请选择审核人员"
		:selectorTypes="[SelectorTypeEnum.USER]"
		:selectUserTypes="[SelectorUserTypeEnum.ORG, SelectorUserTypeEnum.POSITION]"
		@select="handleHandlerSelected"
	>
	</SysCompositeSelector>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { WorkflowNode } from "@/api/modules/workflow/node";
import { Workflow } from "@/api/modules/workflow/core";
import { FormInstance } from "element-plus";
import SysCompositeSelector from "@/components/Selector/SysCompositeSelector.vue";
import { SelectorTypeEnum, SelectorUserTypeEnum } from "@/components/Selector/interface";

interface FormProps {
	nodeProp: WorkflowNode.Form;
	rowData: Workflow.FlowSubmitForm;
}

const formVisible = ref(false);
const formProps = ref<FormProps>();

// 审核人员姓名回显
const getNextHandlerNames = (index: number) => {
	return formProps.value?.rowData.nextHandlerList[index].handlers.map(handler => handler.handlerName).join(",");
};

// 分支多环节下拉框选择
const handleMultiNodeNextHandlerSelChange = (nodeId: string, index: number) => {
	const thizNodeList = formProps.value?.rowData.nextHandlerList[index].nodeList;
	const selectedNode = thizNodeList?.find(node => node.nodeId == nodeId);
	if (selectedNode) {
		formProps.value!.rowData.nextHandlerList[index] = { ...selectedNode };
		formProps.value!.rowData.nextHandlerList[index].nodeList = thizNodeList;
	}
};

// 选择审核人员
const selectorRef = ref<InstanceType<typeof SysCompositeSelector>>();
let selectorIndex = -1;
const openHandlerSelector = (index: number) => {
	selectorIndex = index;
	selectorRef.value?.acceptParams({
		selected: formProps.value?.rowData.nextHandlerList[index].handlers.map(item => {
			return {
				type: SelectorTypeEnum.USER,
				value: item.handlerId,
				label: item.handlerName
			};
		})
	});
};
const handleHandlerSelected = (datas: { [key: string]: any }[]) => {
	formProps.value!.rowData.nextHandlerList[selectorIndex].handlers = datas.map(item => {
		return {
			handlerId: item.value,
			handlerName: item.label
		};
	});
};

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	formProps.value = params;

	formVisible.value = true;
};

// 提交表单
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
	});
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
