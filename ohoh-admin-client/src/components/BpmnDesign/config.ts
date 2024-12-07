import { EditorSettings } from "@/components/BpmnDesign/types/settings";
import { defaultLang } from "@/components/BpmnDesign/extends/AdditionalModules/Translate";

export const defaultSettings: EditorSettings = {
	language: defaultLang,
	processId: `Process_${new Date().getTime()}`,
	processName: `业务流程`,
	processEngine: "activiti",
	paletteMode: "enhancement",
	penalMode: "custom",
	contextPadMode: "enhancement",
	rendererMode: "rewrite",
	bg: "grid-image",
	toolbar: true,
	miniMap: true,
	contextmenu: true,
	customContextmenu: true,
	otherModule: true,
	templateChooser: true,
	useLint: false,
	customTheme: {}
};
