import { ElementLike } from "diagram-js/lib/core";

declare global {
	interface Window {
		bpmnInstances: any;
	}

	type BpmnElement = ElementLike & { type: string };
}

declare interface Window {
	bpmnInstances: any;
}
