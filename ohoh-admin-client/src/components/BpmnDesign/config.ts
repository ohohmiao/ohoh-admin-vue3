import { EditorSettings } from "@/components/BpmnDesign/types/settings";

export const defaultSettings: EditorSettings = {
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
