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
						<el-button @click="formVisible = false">关闭</el-button>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="WorkflowDefForm">
import { ref, onMounted, nextTick, shallowRef } from "vue";
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

interface FormProps {
	[key: string]: any;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
// const formProps = ref<FormProps>({
// 	rowData: {}
// });

const editorStore = BpmnEditorState();
const { editorSettings } = storeToRefs(editorStore);
const designerRef = shallowRef<HTMLDivElement | null>(null);
const initBpmnDesigner = async () => {
	const modelerModules = modulesAndModdle(editorSettings);
	await nextTick();
	initModeler(designerRef, modelerModules);
	await createNewDiagram();
};

onMounted(() => {
	// document.body.addEventListener("contextmenu", function (ev) {
	// 	ev.preventDefault();
	// });
});

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	console.info(params);
	formVisible.value = true;
	initBpmnDesigner();
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
