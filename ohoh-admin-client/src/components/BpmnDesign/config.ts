import { EditorSettings } from "@/components/BpmnDesign/settings";
import { defaultLang } from "@/components/BpmnDesign/extends/AdditionalModules/Translate";

export const defaultSettings: EditorSettings = {
	language: defaultLang,
	processId: `Process_${new Date().getTime()}`,
	processName: `工作流`,
	processEngine: "activiti",
	paletteMode: "default",
	rendererMode: "default",
	customTheme: {},
	contextPadMode: "default",
	miniMap: true,
	otherModule: true,
	useLint: false,
	draggable: true
};
