import { ViewerOptions } from "diagram-js/lib/model/Types";
import { ModuleDeclaration } from "didi";

export interface EditorSettings {
	language?: string;
	processName?: string;
	processId?: string;
	processEngine: "flowable" | "activiti" | "camunda";
	paletteMode?: "default" | "rewrite" | undefined; //左侧工具栏，也叫调色板
	rendererMode: "default" | "rewrite"; //画布上的图形
	customTheme?: Record<string, string | number>;
	contextPadMode?: "default" | "rewrite" | undefined; //节点的菜单弹窗
	miniMap: boolean; //小地图
	otherModule?: boolean;
	useLint?: boolean;
	draggable: boolean;
}

export type ModelerOptions<E extends Element> = ViewerOptions<E> & {
	additionalModules: ModuleDeclaration[];
	moddleExtensions: Object;
};

// bpmn.js 事件参数
// 1. canvas 事件
type CanvasEventParams = {
	svg: SVGElement;
	viewport: SVGElement;
};
