import { type Ref } from "vue";
import type { ModuleDeclaration } from "didi";
import type { EditorSettings } from "@/components/BpmnDesign/types/settings";
// moddle 定义文件
import activitiModdleDescriptors from "@/components/BpmnDesign/extends/ModdleExtensions/activiti.json";
import flowableModdleDescriptors from "@/components/BpmnDesign/extends/ModdleExtensions/flowable.json";
import camundaModdleDescriptors from "camunda-bpmn-moddle/resources/camunda.json";
// 自定义 modules 扩展模块
//import Translate from "@/components/BpmnDesign/extends/AdditionalModules/Translate";

export type ModulesAndModdles = [ModuleDeclaration[], { [key: string]: any }, { [key: string]: unknown }];

export default function (settings: Ref<EditorSettings>): ModulesAndModdles {
	const modules: ModuleDeclaration[] = []; // modules 扩展模块数组
	let moddle: { [key: string]: any } = {}; // moddle 声明文件对象
	const options: { [key: string]: unknown } = {}; // modeler 其他配置

	//modules.push(Translate);

	// 设置对应的 moddle 解析配置文件 ( 避免上面已经配置了 camunda )
	if (!Object.keys(moddle).length) {
		if (settings.value.processEngine === "activiti") moddle["activiti"] = activitiModdleDescriptors;
		if (settings.value.processEngine === "camunda") moddle["camunda"] = camundaModdleDescriptors;
		if (settings.value.processEngine === "flowable") moddle["flowable"] = flowableModdleDescriptors;
	}

	return [modules, moddle, options];
}
