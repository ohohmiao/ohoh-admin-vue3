import { markRaw, ShallowRef } from "vue";
import Modeler from "bpmn-js/lib/Modeler";
import EventEmitter from "@/components/BpmnDesign/utils/eventEmitter";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";

import type { BaseViewerOptions } from "bpmn-js/lib/BaseViewer";
import type { ModulesAndModdles } from "@/components/BpmnDesign/utils/modulesAndModdle";

export default function (designer: ShallowRef<HTMLElement | null>, modelerModules: ModulesAndModdles | []) {
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

	// modeler.on("commandStack.changed", async event => {
	// 	try {
	// 		const { xml } = await modeler.saveXML({ format: true });
	//
	// 		emit("update:xml", xml);
	// 		emit("command-stack-changed", event);
	// 	} catch (error) {
	// 		console.error(error);
	// 	}
	// });
}
