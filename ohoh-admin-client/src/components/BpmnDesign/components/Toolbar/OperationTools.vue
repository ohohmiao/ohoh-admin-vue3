<template>
	<el-button-group>
		<el-button type="primary" plain @click="openImportWindow()"
			>打开文件<input type="file" ref="importRef" style="display: none" accept=".xml,.bpmn" @change="handleImportChange()"
		/></el-button>
		<el-popover placement="bottom" trigger="hover">
			<template #reference>
				<el-button type="primary" plain>导出文件</el-button>
			</template>
			<template #default>
				<div class="button-list_column">
					<el-button type="primary" @click="downloadProcessAsBpmn()">导出为Bpmn</el-button>
					<el-button type="primary" @click="downloadProcessAsXml()">导出为XML</el-button>
					<el-button type="primary" @click="downloadProcessAsSvg()">导出为SVG</el-button>
				</div>
			</template>
		</el-popover>
		<el-popover placement="bottom" trigger="hover">
			<template #reference>
				<el-button type="primary" plain>预览文件</el-button>
			</template>
			<div class="button-list_column">
				<el-button type="primary" @click="openXMLPreviewModel()">预览为XML</el-button>
				<el-button type="primary" @click="openJSONPreviewModel()">预览为JSON</el-button>
			</div>
		</el-popover>
	</el-button-group>
	<div class="preview-dialog">
		<el-dialog v-model="previewDialogVisible" title="预览文件">
			<div class="preview-model">
				<Codemirror v-model="previewDialogContent" disabled :extensions="[{ javascript }['javascript'](), oneDark]"></Codemirror>
			</div>
		</el-dialog>
	</div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";
import BpmnModdle from "bpmn-moddle";
import { ElMessage } from "element-plus";
import { downloadFile, setEncoded } from "@/components/BpmnDesign/utils/files";
import { Codemirror } from "vue-codemirror";
import { javascript } from "@codemirror/lang-javascript";
import { oneDark } from "@codemirror/theme-one-dark";

const modelerStore = BpmnModelerState();
const importRef = ref<HTMLInputElement | null>(null);

const moddle = new BpmnModdle();

const previewDialogVisible = ref(false);
const previewDialogContent = ref("");

// 打开文件
const openImportWindow = () => {
	importRef.value && importRef.value.click();
};

const handleImportChange = () => {
	if (importRef.value && importRef.value.files) {
		const file = importRef.value.files[0];
		const reader = new FileReader();
		reader.readAsText(file);
		reader.onload = function () {
			const xmlStr = this.result;
			modelerStore.getModeler!.importXML(xmlStr as string);
		};
		importRef.value.value = "";
		importRef.value.files = null;
	}
};

// 下载文件
const downloadProcess = async (type: string, name = "diagram") => {
	try {
		const modeler = modelerStore.getModeler;
		// 按需要类型创建文件并下载
		if (type === "xml" || type === "bpmn") {
			const { error, xml } = await modeler!.saveXML({});
			// 读取异常时抛出异常
			if (error) {
				console.error(`[Process Designer Warn ]: ${error.message || error}`);
			}
			const { href, filename } = setEncoded(type.toUpperCase(), name, xml!);
			downloadFile(href, filename);
		} else {
			const { svg } = await modeler!.saveSVG();
			// 读取异常时抛出异常
			const { href, filename } = setEncoded("SVG", name, svg!);
			downloadFile(href, filename);
		}
	} catch (e: any) {
		console.error(`[Process Designer Warn ]: ${e.message || e}`);
	}
};

const downloadProcessAsBpmn = () => {
	downloadProcess("bpmn");
};
const downloadProcessAsXml = () => {
	downloadProcess("xml");
};
const downloadProcessAsSvg = () => {
	downloadProcess("svg");
};

// 预览文件
const openXMLPreviewModel = async () => {
	try {
		const modeler = modelerStore.getModeler!;
		if (!modeler) {
			return ElMessage.warning("模型加载失败，请刷新重试");
		}
		const { xml } = await modeler.saveXML({ format: true, preamble: true });
		previewDialogContent.value = xml!;
		previewDialogVisible.value = true;
	} catch (e) {
		ElMessage.error((e as Error).message || (e as string));
	}
};
const openJSONPreviewModel = async () => {
	try {
		const modeler = modelerStore.getModeler!;
		if (!modeler) {
			return ElMessage.warning("模型加载失败，请刷新重试");
		}
		const { xml } = await modeler.saveXML({ format: true, preamble: true });
		const jsonStr = await moddle.fromXML(xml!);
		previewDialogContent.value = JSON.stringify(jsonStr, null, 2);
		previewDialogVisible.value = true;
	} catch (e) {
		ElMessage.error((e as Error).message || (e as string));
	}
};
</script>
<style scoped lang="scss">
.preview-dialog :deep(.el-dialog__header) {
	display: block !important;
}
</style>
