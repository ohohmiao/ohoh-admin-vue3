<template>
	<div class="designer-dialog">
		<el-dialog v-model="formVisible" fullscreen :show-close="false" :close-on-click-modal="false" destroy-on-close>
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
					<div class="designer-left designer-with-bg" ref="designerRef"></div>
					<div class="designer-right">
						<el-form ref="formRef" label-position="top" label-suffix=" :" :rules="rules" :model="formProps.rowData">
							<el-form-item label="类别" prop="deftypeId">
								<el-tree-select
									v-model="formProps.rowData.deftypeId"
									:data="defTypeTreeSelectDatas"
									:props="{ label: 'name' }"
									node-key="id"
									check-strictly
									filterable
								>
									<template #default="{ node, data }">
										<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
										<el-icon v-else><Document /></el-icon>
										<span>{{ node.label }}</span>
									</template>
								</el-tree-select>
							</el-form-item>
							<el-form-item label="流程名称" prop="defName">
								<el-input v-model="formProps.rowData.defName" placeholder="请输入流程名称" maxlength="30" clearable></el-input>
							</el-form-item>
							<el-form-item label="类别编码" prop="defCode">
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
							<el-form-item label="排序" prop="defSort" v-if="computedEditMode">
								<el-input-number
									v-model="formProps.rowData.defSort"
									:min="1"
									:max="9999"
									placeholder="请输入排序"
									controls-position="right"
									style="width: 100%"
								></el-input-number>
							</el-form-item>
							<el-form-item label="备注" prop="defRemark">
								<el-input
									v-model="formProps.rowData.defRemark"
									placeholder="请输入备注"
									type="textarea"
									:autosize="{ minRows: 5, maxRows: 8 }"
									maxlength="100"
									clearable
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
	</div>
</template>

<script setup lang="ts" name="WorkflowDefForm">
import { ref, onMounted, nextTick, shallowRef, reactive, computed } from "vue";
import { storeToRefs } from "pinia";
import AlignTools from "@/components/BpmnDesign/components/Toolbar/AlignTools.vue";
import ScaleTools from "@/components/BpmnDesign/components/Toolbar/ScaleTools.vue";
import CommandTools from "@/components/BpmnDesign/components/Toolbar/CommandTools.vue";
import ExternalTools from "@/components/BpmnDesign/components/Toolbar/ExternalTools.vue";
import OperationTools from "@/components/BpmnDesign/components/Toolbar/OperationTools.vue";
import BpmnEditorState from "@/stores/modules/bpmn/editor";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";
import initModeler from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram } from "@/components/BpmnDesign/utils/createNewDiagram";
import { getWorkflowDefTypeTreeApi, WorkflowDef, WorkflowDefType } from "@/api/modules/workflow/def";
import * as eleValidate from "@/utils/eleValidate";
import { FormInstance, ElMessage } from "element-plus";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";
import BpmnModdle from "bpmn-moddle";

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
const defTypeTreeSelectDatas = ref<WorkflowDefType.TreeNode[]>([]);
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

const editorStore = BpmnEditorState();
const { editorSettings } = storeToRefs(editorStore);
const designerRef = shallowRef<HTMLDivElement | null>(null);
const initBpmnDesigner = async (defXml?: string) => {
	const modelerModules = modulesAndModdle(editorSettings);
	await nextTick();
	initModeler(designerRef, modelerModules);
	await createNewDiagram(defXml);
};

onMounted(() => {
	// document.body.addEventListener("contextmenu", function (ev) {
	// 	ev.preventDefault();
	// });
});

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
	handleDefTypeTreeCheckDisable(data);
	defTypeTreeSelectDatas.value = data;

	formVisible.value = true;
	initBpmnDesigner(formProps.value.rowData.defXml);
};

// 禁用一级树节点
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
const modelerStore = BpmnModelerState();
const moddle = new BpmnModdle();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			// 获取bpmnjs设计器数据
			const modeler = modelerStore.getModeler!;
			const { xml } = await modeler.saveXML({ format: true, preamble: true });
			const { svg } = await modeler.saveSVG();
			const jsonStr = await moddle.fromXML(xml!);
			formProps.value.rowData.defXml = xml;
			formProps.value.rowData.defSvg = svg;
			formProps.value.rowData.defJson = JSON.stringify(jsonStr);

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
<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style src="@/components/BpmnDesign/styles/toolbar.scss"></style>
<style scoped lang="scss">
.designer-dialog :deep(.el-dialog__header) {
	display: none;
}
</style>
