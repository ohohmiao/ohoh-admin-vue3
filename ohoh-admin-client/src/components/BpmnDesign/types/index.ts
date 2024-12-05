import type { PropType, ShallowRef, ComputedRef } from "vue";
import type { BpmnModuleDeclaration, BpmnElement } from "./bpmn";
import type Modeler from "bpmn-js/lib/Modeler";
import type { WetSchemaItemType, WetSchemaFormInstance } from "@wetspace/pro-components";
// import type { WetProFormInstance } from '@wetspace/pro-components'

export type WetBpmnProcessType = "activiti" | "camunda" | "flowable";

export type WetDfPaletteType =
	| "create.data-object"
	| "create.data-store"
	| "create.end-event"
	| "create.exclusive-gateway"
	| "create.group"
	| "create.intermediate-event"
	| "create.participant-expanded"
	| "create.start-event"
	| "create.subprocess-expanded"
	| "create.task;global-connect-tool"
	| "hand-tool"
	| "lasso-tool"
	| "space-tool"
	| "tool-separator";

export const WetBpmnDesignProps = {
	xml: {
		type: String,
		default: ""
	},
	json: {
		type: Object as PropType<Record<string, any>>,
		default: null
	},
	processType: {
		type: String as PropType<WetBpmnProcessType>,
		default: "flowable"
	},
	defaultPalettes: {
		type: Array as PropType<WetDfPaletteType[]>,
		default() {
			return [];
		}
	},
	elementProperties: {
		type: Object as PropType<Record<string, any>>,
		default() {
			return null;
		}
	}
};

export const WetBpmnDesignMainPanelProps = {
	...WetBpmnDesignProps
};

export type BpmnProvideType = {
	modules: ShallowRef<BpmnModuleDeclaration[]>;
	modeler: ShallowRef<Modeler | null>;
	seletedBpmnElement: ShallowRef<[BpmnElement | null]>;
	processType: ComputedRef<WetBpmnProcessType>;
	addedBpmnElementsMap: ShallowRef<Record<string, any>>;
	elementProperties: ComputedRef<Record<string, any>>;
};

export type BpmnPropertiesFormItem = Omit<WetSchemaItemType, "property"> & {
	// isBpmnModdleElement?:boolean,
	// isOnlyPropery?:boolean,
	property: string;
	bpmnElement?: string | string[];
	prefixElement?: string | string[];
};

export type BpmnPropertiesFormIns = WetSchemaFormInstance;

export * from "./bpmn";
