<template>
	<el-dialog v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}流程事件`">
		<el-form
			ref="formRef"
			label-suffix=" :"
			label-width="120"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="事件类别" prop="eventType">
				<el-radio-group v-model="formProps.rowData.eventType">
					<el-radio-button :value="0">存储</el-radio-button>
					<el-radio-button :value="1">前置</el-radio-button>
					<el-radio-button :value="2">后置</el-radio-button>
					<el-radio-button :value="3">判断</el-radio-button>
					<el-radio-button :value="4">读取</el-radio-button>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="事件名称" prop="eventName">
				<el-input v-model="formProps.rowData.eventName" placeholder="请输入事件名称" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="绑定环节" prop="bindNodeNames">
				<div class="selector-box">
					<div class="selector-box-top">
						<el-tag
							v-for="(item, index) in formProps.rowData.bindNodeNames"
							:key="index"
							@close="handleCloseSelectedNode(index)"
							plain
							closable
							>{{ item }}</el-tag
						>
					</div>
					<div class="selector-box-bottom">
						<span class="selector-btn" @click="handleClearSelectedNodes"
							><el-icon><Delete /></el-icon>清空</span
						>
						<span class="selector-btn" @click="openNodeSelector"
							><el-icon><Pointer /></el-icon>选择</span
						>
					</div>
				</div>
			</el-form-item>
			<el-form-item label="实现方式" prop="implType">
				<el-radio-group v-model="formProps.rowData.implType">
					<el-radio-button :value="0">本地服务</el-radio-button>
					<el-radio-button :value="1">脚本方式</el-radio-button>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="本地服务" prop="implLocalservice" v-if="formProps.rowData.implType == 0" required>
				<el-input v-model="formProps.rowData.implLocalservice" placeholder="请输入本地服务" maxlength="60" clearable></el-input>
			</el-form-item>
			<el-form-item label="实现脚本" prop="implScript" v-if="formProps.rowData.implType == 1" required>
				<Codemirror
					v-model:value="formProps.rowData.implScript"
					ref="cmRef"
					:options="cmOptions"
					border
					width="100%"
					height="200"
				>
				</Codemirror>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-dialog>
	<!-- 流程环节选择器 -->
	<BpmnNodeSelector
		ref="nodeSelectorRef"
		title="请选择流程环节"
		:node-types="['bpmn:Task', 'bpmn:ExclusiveGateway']"
		@select="handleNodeSelect"
	></BpmnNodeSelector>
</template>

<script setup lang="ts">
import { defineProps, onUnmounted, reactive, ref, watch } from "vue";
import { WorkflowEvent } from "@/api/modules/workflow/event";
import { ElMessage, FormInstance } from "element-plus";
import { Delete, Pointer } from "@element-plus/icons-vue";
import "codemirror/mode/javascript/javascript.js";
import Codemirror from "codemirror-editor-vue3";
import type { CmComponentRef } from "codemirror-editor-vue3";
import type { EditorConfiguration } from "codemirror";
import "codemirror/theme/3024-night.css";
import BpmnNodeSelector from "@/components/BpmnDesign/BpmnNodeSelector.vue";

const props = defineProps({
	defCode: String,
	defVersion: Number,
	defXml: String
});

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowEvent.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});

// codemirror实例
const cmRef = ref<CmComponentRef>();
const cmOptions = ref<EditorConfiguration>({
	mode: "text/x-java",
	theme: "3024-night"
});

watch(formProps.value, newFormProps => {
	cmOptions.value.readOnly = newFormProps.isView;
});

const rules = reactive({
	eventType: [{ required: true, message: "请选择事件类别" }],
	eventName: [{ required: true, message: "请输入事件名称" }],
	bindNodeNames: [{ type: "array", required: true, message: "请选择绑定环节" }],
	implType: [{ required: true, message: "请选择实现方式" }],
	implLocalservice: [
		{
			type: "string",
			validator: (rule: any, value: string) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.implType == 0) {
						if (!value) {
							reject("请输入本地服务");
						} else {
							resolve("");
						}
					} else {
						resolve("");
					}
				});
			}
		}
	],
	implScript: [
		{
			type: "string",
			validator: (rule: any, value: string) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.implType == 1) {
						if (!value) {
							reject("请输入实现脚本");
						} else {
							resolve("");
						}
					} else {
						resolve("");
					}
				});
			}
		}
	]
});

// 打开环节选择器
const nodeSelectorRef = ref<InstanceType<typeof BpmnNodeSelector>>();
const openNodeSelector = () => {
	nodeSelectorRef.value?.acceptParams({
		bpmnXml: props.defXml,
		selected: formProps.value.rowData.bindNodeIds
	});
};
// 删掉某个已选择的环节
const handleCloseSelectedNode = (index: number) => {
	formProps.value.rowData.bindNodeIds?.splice(index, 1);
	formProps.value.rowData.bindNodeNames?.splice(index, 1);
};
// 清空已选择的环节
const handleClearSelectedNodes = () => {
	formProps.value.rowData.bindNodeIds?.splice(0, formProps.value.rowData.bindNodeIds.length);
	formProps.value.rowData.bindNodeNames?.splice(0, formProps.value.rowData.bindNodeNames.length);
};
// 环节选择器回调
const handleNodeSelect = (selected: { [key: string]: any }[]) => {
	formProps.value.rowData.bindNodeIds = selected.map(n => n.value);
	formProps.value.rowData.bindNodeNames = selected.map(n => n.label);
};

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			const { msg } = await formProps.value.api!(formProps.value.rowData);
			ElMessage.success({ message: msg });
			formProps.value.getTableList!();
			formVisible.value = false;
		} catch (e) {
			console.log(e);
		}
	});
};

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	Object.keys(params).forEach(key => {
		if (key === "rowData") {
			// 存在默认赋值的rowData，不覆盖
			if (params[key] && Object.keys(params[key]).length) {
				formProps.value[key] = params[key];
			} else {
				formProps.value[key] = Object.assign({}, params[key], {
					//表单字段默认值
					eventType: 0,
					implType: 0
				});
			}
		} else {
			formProps.value[key] = params[key];
		}
	});
	formProps.value.rowData.defCode = props.defCode;
	formProps.value.rowData.defVersion = props.defVersion;

	formVisible.value = true;
};

onUnmounted(() => {
	cmRef.value?.destroy();
});

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss">
.selector-box {
	width: 100%;
	height: 70px;
	padding: 5px 5px 0;
	border: 1px solid #dcdfe6;
	border-radius: 4px;
	.selector-box-top {
		height: 40px;
		overflow-y: auto;
		.el-scrollbar__wrap {
			overflow: hidden;
		}
		.el-tag {
			margin-right: 8px;
			margin-bottom: 3px;
		}
	}
	.selector-box-bottom {
		height: 20px;
		margin-top: 5px;
		line-height: 20px;
		text-align: right;
		.selector-btn {
			cursor: pointer;
		}
		.selector-btn:nth-child(n + 1) {
			margin-left: 15px;
		}
	}
}
</style>
