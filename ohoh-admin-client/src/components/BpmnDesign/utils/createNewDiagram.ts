import EmptyXML from "./emptyXML";
import { EditorSettings } from "../settings";
import BpmnModelerState from "@/stores/modules/bpmn/modeler";

export const createNewDiagram = async function (newXml?: string, settings?: EditorSettings, emit?: any) {
	try {
		const store = BpmnModelerState();
		const timestamp = Date.now();
		const { processId, processName } = settings || {};
		const newId: string = processId ? processId : `Process_${timestamp}`;
		const newName: string = processName || `工作流_${timestamp}`;
		const xmlString = newXml || EmptyXML(newId, newName);
		const modeler = store.getModeler;
		const { warnings } = await modeler!.importXML(xmlString);
		if (warnings && warnings.length) {
			warnings.forEach(warn => console.warn(warn));
		}
		// 触发事件
		emit && emit("modeler-init", modeler);
		modeler?.on("element.click", event => {
			const { element } = event;
			if (!event || !element.parent || element.type === "bpmn:Process") {
				return false;
			} else {
				element.name = element.di.bpmnElement.name;
				emit && emit("element-click", element);
			}
		});
	} catch (e) {
		console.error(`[Process Designer Warn]: ${typeof e === "string" ? e : (e as Error)?.message}`);
	}
};
