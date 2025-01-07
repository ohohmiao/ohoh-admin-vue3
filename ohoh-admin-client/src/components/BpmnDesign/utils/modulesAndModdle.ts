import { type Ref } from "vue";
import type { ModuleDeclaration } from "didi";
import type { EditorSettings } from "@/components/BpmnDesign/settings";
// moddle 定义文件
import activitiModdleDescriptors from "@/components/BpmnDesign/extends/ModdleExtensions/activiti.json";
import flowableModdleDescriptors from "@/components/BpmnDesign/extends/ModdleExtensions/flowable.json";
import camundaModdleDescriptors from "camunda-bpmn-moddle/resources/camunda.json";
// 自定义 modules 扩展模块
import Translate from "@/components/BpmnDesign/extends/AdditionalModules/Translate";
import TokenSimulationModule from "bpmn-js-token-simulation";
import minimapModule from "diagram-js-minimap";

export type ModulesAndModdles = [ModuleDeclaration[], { [key: string]: any }, { [key: string]: unknown }];

export default function (settings: Ref<EditorSettings>): ModulesAndModdles {
	const modules: ModuleDeclaration[] = []; // modules 扩展模块数组
	let moddle: { [key: string]: any } = {}; // moddle 声明文件对象
	const options: { [key: string]: unknown } = {}; // modeler 其他配置

	// 左侧工具栏
	if (settings.value.paletteMode == "default") {
		// 默认
	} else if (settings.value.paletteMode == "rewrite") {
		// 重写
	} else {
		modules.push({ paletteProvider: ["type", function () {}] });
	}

	// 画布上的图形
	if (settings.value.paletteMode == "rewrite") {
		//重写
	} else {
		//默认
	}

	// 节点的菜单弹窗
	if (settings.value.contextPadMode == "default") {
		// 默认
	} else if (settings.value.contextPadMode == "rewrite") {
		// 重写
	} else {
		modules.push({ contextPadProvider: ["type", function () {}] });
	}

	if (!settings.value.draggable) {
		// 禁止线条拖动
		modules.push({ bendpoints: ["type", function () {}] });
		// 禁止单个图形拖动
		modules.push({ move: ["type", function () {}] });
	}

	// 小地图
	if (settings.value.miniMap) {
		modules.push(minimapModule);
		options["minimap"] = {
			open: true
		};
	}

	// 设置其他模块的启用
	if (settings.value.otherModule) {
		modules.push(TokenSimulationModule);
	}

	// 翻译
	modules.push(Translate);

	// 设置对应的 moddle 解析配置文件 ( 避免上面已经配置了 camunda )
	if (!Object.keys(moddle).length) {
		if (settings.value.processEngine === "activiti") moddle["activiti"] = activitiModdleDescriptors;
		if (settings.value.processEngine === "camunda") moddle["camunda"] = camundaModdleDescriptors;
		if (settings.value.processEngine === "flowable") moddle["flowable"] = flowableModdleDescriptors;
	}

	return [modules, moddle, options];
}
