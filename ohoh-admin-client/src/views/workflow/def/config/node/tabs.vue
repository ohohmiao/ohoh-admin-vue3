<template>
	<el-dialog
		v-model="formVisible"
		:title="`环节配置[${tabProps.nodeName}-${tabProps.nodeId}]`"
		fullscreen
		:close-on-click-modal="false"
		destroy-on-close
	>
		<el-tabs v-model="activeTabName" tab-position="left">
			<el-tab-pane label="环节属性" name="baseTab">
				<DefNodePropTab :row-data="formProps.rowData"></DefNodePropTab>
			</el-tab-pane>
			<!--<el-tab-pane label="表单字段权限" name="formTab"></el-tab-pane>-->
			<el-tab-pane label="下一步办理人" name="nextHandlerTab" v-if="computedNextHandlerTab">
				<DefNodeHandlerListTab
					:def-code="tabProps.defCode"
					:def-version="tabProps.defVersion"
					:node-id="tabProps.nodeId"
					:next-task-node-list="nextTaskNodeList"
				></DefNodeHandlerListTab>
			</el-tab-pane>
		</el-tabs>
	</el-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { getWorkflowNodeApi, WorkflowNode } from "@/api/modules/workflow/node";
import { getNextTaskNodeInfoListApi } from "@/api/modules/workflow/node";
import DefNodePropTab from "./DefNodePropTab.vue";
import DefNodeHandlerListTab from "./DefNodeHandlerListTab.vue";

interface TabProps {
	nodeId: string;
	nodeName: string;
	defCode: string;
	defVersion: number;
}
interface FormProps {
	rowData: Partial<WorkflowNode.Form>;
}

const formVisible = ref(false);
const activeTabName = ref();
const tabProps = ref<TabProps>({
	nodeId: "",
	nodeName: "",
	defCode: "",
	defVersion: -1
});
const formProps = ref<FormProps>({
	rowData: {}
});

// 下一步任务环节信息
const nextTaskNodeList = ref<{ label: string; value: string }[]>([]);
// 是否渲染下一步办理人tab计算
const computedNextHandlerTab = computed(() => !!nextTaskNodeList.value.length);

// 接收父组件传过来的参数
const acceptParams = async (params: TabProps) => {
	tabProps.value = params;
	activeTabName.value = "baseTab";

	const { data: nodeData } = await getWorkflowNodeApi({
		defCode: params.defCode,
		defVersion: params.defVersion,
		nodeId: params.nodeId
	});
	formProps.value.rowData = nodeData || {
		defCode: params.defCode,
		defVersion: params.defVersion,
		nodeId: params.nodeId,
		nodeName: params.nodeName
	};

	const { data: nextTaskNodeListData } = await getNextTaskNodeInfoListApi({
		defCode: params.defCode,
		defVersion: params.defVersion,
		nodeId: params.nodeId
	});
	nextTaskNodeList.value = nextTaskNodeListData;

	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
