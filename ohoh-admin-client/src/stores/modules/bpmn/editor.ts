import { defineStore } from "pinia";
import { EditorSettings } from "@/components/BpmnDesign/settings";
import { defaultSettings } from "@/components/BpmnDesign/config";

const state = {
	editorSettings: defaultSettings
};

export default defineStore("BpmnEditorState", {
	state: () => state,
	getters: {
		getProcessDef: (state): Pick<EditorSettings, "processName" | "processId"> => ({
			processName: state.editorSettings.processName,
			processId: state.editorSettings.processId
		}),
		getProcessEngine: (state): EditorSettings["processEngine"] => state.editorSettings.processEngine,
		getEditorConfig: (state): Omit<EditorSettings, "language" | "processName" | "processId" | "processEngine"> => ({
			bg: state.editorSettings.bg,
			paletteMode: state.editorSettings.paletteMode,
			penalMode: state.editorSettings.penalMode,
			contextPadMode: state.editorSettings.contextPadMode,
			rendererMode: state.editorSettings.rendererMode,
			toolbar: state.editorSettings.toolbar,
			miniMap: state.editorSettings.miniMap,
			contextmenu: state.editorSettings.contextmenu,
			customContextmenu: state.editorSettings.customContextmenu,
			otherModule: state.editorSettings.otherModule,
			templateChooser: state.editorSettings.templateChooser,
			useLint: state.editorSettings.useLint,
			customTheme: state.editorSettings.customTheme
		})
	},
	actions: {
		updateConfiguration(conf: Partial<EditorSettings>) {
			this.$state.editorSettings = { ...this.$state.editorSettings, ...conf };
		}
	}
});
