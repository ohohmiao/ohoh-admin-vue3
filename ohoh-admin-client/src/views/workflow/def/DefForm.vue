<template>
	<el-dialog
		v-model="formVisible"
		class="designer-dialog"
		fullscreen
		:show-close="false"
		:close-on-click-modal="false"
		destroy-on-close
	>
		<div class="container">
			<div class="toolbar">
				<div class="flex">
					<AlignTools></AlignTools>
					<ScaleTools></ScaleTools>
					<CommandTools></CommandTools>
					<ExternalTools></ExternalTools>
				</div>
				<OperationTools></OperationTools>
			</div>
			<div class="designer">
				<BpmnEditor ref="bpmnEditorRef"></BpmnEditor>
				<div class="designer-right">
					<el-form ref="formRef" label-position="top" label-suffix=" :" :rules="rules" :model="formProps.rowData">
						<el-form-item label="类别" prop="deftypeId">
							<el-tree-select
								v-model="formProps.rowData.deftypeId"
								:data="defTypeTreeSelectDatas"
								:props="{ label: 'name' }"
								:default-expanded-keys="defTypeTreeSelectDefaultExpandKeys"
								node-key="id"
								check-strictly
								filterable
								:disabled="computedEditMode"
							>
								<template #default="{ node, data }">
									<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
									<el-icon v-else><Document /></el-icon>
									<span>{{ node.label }}</span>
								</template>
							</el-tree-select>
						</el-form-item>
						<el-form-item label="流程名称" prop="defName">
							<el-input
								v-model="formProps.rowData.defName"
								placeholder="请输入流程名称"
								maxlength="30"
								clearable
								:disabled="computedEditMode"
							></el-input>
						</el-form-item>
						<el-form-item prop="defCode">
							<template #label>
								<el-tooltip content="注：仅限输入字母、数字和下划线！" placement="top">
									<el-icon><QuestionFilled /></el-icon>
								</el-tooltip>
								流程编码：
							</template>
							<el-input
								v-model="formProps.rowData.defCode"
								placeholder="请输入流程编码"
								maxlength="30"
								clearable
								:disabled="computedEditMode"
							></el-input>
						</el-form-item>
						<el-form-item>
							<div style="margin: 0 auto">
								<el-button type="primary" @click="handleSubmit()">部署流程</el-button>
								<el-button @click="formVisible = false">关闭</el-button>
							</div>
						</el-form-item>
					</el-form>
				</div>
			</div>
		</div>
	</el-dialog>
</template>

<script setup lang="ts" name="WorkflowDefForm">
import { ref, reactive, computed, shallowRef, nextTick } from "vue";
import AlignTools from "@/components/BpmnDesign/components/Toolbar/AlignTools.vue";
import ScaleTools from "@/components/BpmnDesign/components/Toolbar/ScaleTools.vue";
import CommandTools from "@/components/BpmnDesign/components/Toolbar/CommandTools.vue";
import ExternalTools from "@/components/BpmnDesign/components/Toolbar/ExternalTools.vue";
import OperationTools from "@/components/BpmnDesign/components/Toolbar/OperationTools.vue";
import BpmnEditor from "@/components/BpmnDesign/BpmnEditor.vue";
import { getWorkflowDefTypeTreeApi, WorkflowDef, WorkflowDefType } from "@/api/modules/workflow/def";
import * as eleValidate from "@/utils/eleValidate";
import { FormInstance, ElMessage } from "element-plus";

interface FormProps {
	[key: string]: any;
	rowData: Partial<WorkflowDef.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	rowData: {}
});
// 类别树数据
const defTypeTreeSelectDatas = ref<WorkflowDefType.TreeNode[]>([]);
// 类别树默认展开节点
const defTypeTreeSelectDefaultExpandKeys = ref<string[]>([]);
const computedEditMode = computed(() => !!formProps.value.rowData.defId);

const rules = reactive({
	deftypeId: [{ required: true, message: "请选择类别" }],
	defName: [{ required: true, message: "请输入流程名称" }],
	defCode: [
		{ required: true, message: "请输入流程编码" },
		{ validator: eleValidate.checkLetterOrNumOrUnderline, message: "仅限输入字母、数字和下划线" }
	],
	defSort: [{ required: true, message: "请输入排序" }]
});

const bpmnEditorRef = shallowRef<InstanceType<typeof BpmnEditor>>();

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
				});
			}
		} else {
			formProps.value[key] = params[key];
		}
	});

	const { data } = await getWorkflowDefTypeTreeApi();
	!computedEditMode.value && handleDefTypeTreeCheckDisable(data);
	// 默认展开一级的树节点
	data?.forEach((item: { [key: string]: any }) => {
		if (item.parentId === "0") {
			defTypeTreeSelectDefaultExpandKeys.value.push(item.deftypeId);
		}
	});
	defTypeTreeSelectDatas.value = data;

	formVisible.value = true;

	await nextTick();
	await bpmnEditorRef.value?.initBpmnDesigner(formProps.value.rowData.defXml);
};

// 禁用流程类别树中的一级树节点
const handleDefTypeTreeCheckDisable = (params: WorkflowDefType.TreeNode[]) => {
	if (!params) return;
	params.forEach((param: WorkflowDefType.TreeNode) => {
		if (param.treeLevel == 1) {
			param.disabled = true;
		}
		handleDefTypeTreeCheckDisable(param.children || []);
	});
};

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			const modelerData = await bpmnEditorRef.value?.getModelerData();
			formProps.value.rowData.defXml = modelerData!.xml;
			formProps.value.rowData.defSvg = modelerData!.svg;

			const { msg } = await formProps.value.api!(formProps.value.rowData);
			ElMessage.success({ message: msg });
			formProps.value.getTableList!();
			formVisible.value = false;
		} catch (e) {
			console.log(e);
		}
	});
};

defineExpose({
	acceptParams
});
</script>
<style scoped lang="scss">
.designer-dialog :deep(.el-dialog__header) {
	display: none;
}
</style>
