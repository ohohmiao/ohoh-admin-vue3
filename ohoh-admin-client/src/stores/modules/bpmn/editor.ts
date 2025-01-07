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
			paletteMode: state.editorSettings.paletteMode,
			contextPadMode: state.editorSettings.contextPadMode,
			rendererMode: state.editorSettings.rendererMode,
			customTheme: state.editorSettings.customTheme,
			miniMap: state.editorSettings.miniMap,
			otherModule: state.editorSettings.otherModule,
			useLint: state.editorSettings.useLint,
			draggable: state.editorSettings.draggable
		})
	},
	actions: {
		updateConfiguration(conf: Partial<EditorSettings>) {
			this.$state.editorSettings = { ...this.$state.editorSettings, ...conf };
		}
	}
});
