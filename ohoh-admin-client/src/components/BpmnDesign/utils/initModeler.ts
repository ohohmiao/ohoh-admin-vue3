import { markRaw, ShallowRef } from "vue";
import Modeler from "bpmn-js/lib/Modeler";
import EventEmitter from "@/components/BpmnDesign/utils/eventEmitter";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";

import type { BaseViewerOptions } from "bpmn-js/lib/BaseViewer";
import type { ModulesAndModdles } from "@/components/BpmnDesign/utils/modulesAndModdle";

/**
 * 编辑器情形
 * @param designer
 * @param modelerModules
 */
export const initModeler = function (designer: ShallowRef<HTMLElement | null>, modelerModules: ModulesAndModdles | []) {
	const store = BpmnModelerState();

	const options: BaseViewerOptions = {
		container: designer!.value as HTMLElement,
		additionalModules: modelerModules[0] || [],
		moddleExtensions: modelerModules[1] || {},
		...modelerModules[2]
	};

	if (store.getModeler) {
		// 清除旧 modeler
		store.getModeler.destroy();
		store.setModeler(undefined);
	}

	const modeler: Modeler = new Modeler(options);

	store.setModeler(markRaw(modeler));

	EventEmitter.emit("modeler-init", modeler);
};

/**
 * 预览流程图情形
 * @param designer
 * @param modelerModules
 */
export const initModeler4Viewer = function (designer: ShallowRef<HTMLElement | null>, modelerModules: ModulesAndModdles | []) {
	const options: BaseViewerOptions = {
		container: designer!.value as HTMLElement,
		additionalModules: modelerModules[0] || [],
		moddleExtensions: modelerModules[1] || {},
		...modelerModules[2]
	};

	const modeler: Modeler = new Modeler(options);

	return modeler;
};
