import { ElMessage } from "element-plus";
import { ElementLike } from "diagram-js/lib/core";

declare global {
	interface Window {
		bpmnInstances: any;
		__messageBox: ElMessage;
	}

	type BpmnElement = ElementLike & { type: string };
}

declare interface Window {
	bpmnInstances: any;
}
