<template>
	<div class="selector-dialog">
		<el-dialog v-model="selectorVisible" :title="selectorProps.title" class="designer-dialog" @close="handleClose" fullscreen>
			<div ref="designerRef"></div>
			<!-- 选择器按钮区域 -->
			<template #footer>
				<el-button @click="selectorVisible = false">取消</el-button>
				<el-button type="primary" @click="handleSubmit">确定</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts">
import { nextTick, shallowRef, ref } from "vue";
import { EditorSettings } from "@/components/BpmnDesign/settings";
import { initModeler4Viewer } from "@/components/BpmnDesign/utils/initModeler";
import { createNewDiagram4Viewer } from "@/components/BpmnDesign/utils/createNewDiagram";
import modulesAndModdle from "@/components/BpmnDesign/utils/modulesAndModdle";
import Modeler from "bpmn-js/lib/Modeler";
import type ElementRegistry from "diagram-js/lib/core/ElementRegistry";
import type Modeling from "bpmn-js/lib/features/modeling/Modeling";
import { ElMessage } from "element-plus";

// 组件参数定义
interface SelectorProps {
	title?: string; //标题
	maxSelectNum?: number; //最大选择数 <=0 不限制
	notEmpty?: boolean; //是否允许不选，true=禁止不选false=允许不选
	nodeTypes?: string[]; //节点类别
}
// 返回值定义
interface SelectorResult {
	label: string;
	value: string;
}
// 选中回调监听，触发父组件改变
type EmitProps = {
	(e: "select", val: { [key: string]: any }[]): void;
};
const emit = defineEmits<EmitProps>();

// 接受父组件参数，配置默认值
const selectorProps = withDefaults(defineProps<SelectorProps>(), {
	title: "请选择",
	maxSelectNum: 0,
	notEmpty: true,
	nodeTypes: () => ["bpmn:Task"]
});

const selectorVisible = ref(false);

// 选中节点颜色
const selectedColor = "#ff0000";
// 渲染流程图
const editorSettings: EditorSettings = {
	processEngine: "activiti",
	rendererMode: "default",
	miniMap: true,
	draggable: false
};
let modeler: Modeler;
const designerRef = shallowRef<HTMLDivElement | null>(null);
const initBpmnDesigner = async (defXml: string, selected?: string[]) => {
	const modelerModules = modulesAndModdle(ref(editorSettings));
	await nextTick();
	modeler = initModeler4Viewer(designerRef, modelerModules);
	await createNewDiagram4Viewer(modeler, defXml, editorSettings);
	// 初始化处理
	if (selected && selected.length) {
		setNodeColor(selected, { fill: "#fff", stroke: selectedColor });
	}
	// 点击处理
	modeler.on("element.click", event => {
		const { element } = event;
		if (!event || !element.parent || element.type === "bpmn:Process") {
			return false;
		} else {
			if (selectorProps.nodeTypes.indexOf(element.type) != -1) {
				if (element.di.stroke && element.di.stroke == selectedColor) {
					setNodeColor([element.id], { fill: "#fff", stroke: "#000" });
				} else {
					setNodeColor([element.id], { fill: "#fff", stroke: selectedColor });
				}
			}
		}
	});
};

// 节点描色
const setNodeColor = (nodeIds: string[], colors: { [key: string]: any }) => {
	const elementRegistry = modeler.get<ElementRegistry>("elementRegistry");
	const nodeList = elementRegistry.getAll().filter(node => nodeIds.indexOf(node.id) != -1);
	const modeling = modeler.get<Modeling>("modeling");
	nodeList.forEach(node => {
		modeling.setColor(node, colors);
	});
};

// 处理关闭
const handleClose = () => {
	modeler.destroy();
};

// 提交确定
const handleSubmit = () => {
	const selected: SelectorResult[] = [];
	const elementRegistry = modeler.get<ElementRegistry>("elementRegistry");
	const nodeList = elementRegistry.getAll().filter(element => selectorProps.nodeTypes.indexOf(element.type) != -1);
	nodeList.forEach(node => {
		if (node.type === "bpmn:ExclusiveGateway" && !node.name) {
			node.name = `判断节点#${node.id}`;
		} else {
			node.name = node.di.bpmnElement.name;
		}
		if (node.di.stroke && node.di.stroke == selectedColor) {
			selected.push({
				label: node.name,
				value: node.id
			});
		}
	});
	if (selectorProps.notEmpty && !selected.length) {
		ElMessage.warning({ message: "请选择" });
		return false;
	}
	if (selectorProps.maxSelectNum > 0) {
		if (selected.length > selectorProps.maxSelectNum) {
			ElMessage.warning({ message: `限制选择${selectorProps.maxSelectNum}个节点` });
			return false;
		}
	}
	emit("select", selected);
	selectorVisible.value = false;
};

// 接收父组件传过来的参数
const acceptParams = (params: { [key: string]: any }) => {
	selectorVisible.value = true;
	params.bpmnXml && initBpmnDesigner(params.bpmnXml, params.selected);
};

defineExpose({
	acceptParams: acceptParams
});
</script>

<style src="@/components/BpmnDesign/styles/index.scss"></style>
<style src="@/components/BpmnDesign/styles/design.scss"></style>
<style scoped lang="scss">
.selector-dialog :deep(.designer-dialog) {
	.bjs-container {
		height: 80vh !important;
	}
}
</style>
