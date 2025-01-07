<template>
	<div class="def-config-dialog">
		<el-dialog
			v-model="formVisible"
			title="流程配置"
			fullscreen
			:close-on-click-modal="false"
			destroy-on-close
			@closed="handleDialogClose"
		>
			<el-tabs v-model="activeName" tab-position="left">
				<el-tab-pane label="基本信息" name="baseTab">
					<DefConfigBaseTab :row-data="formProps.rowData"></DefConfigBaseTab>
				</el-tab-pane>
				<el-tab-pane label="表单绑定" name="formTab">表单绑定内容</el-tab-pane>
				<el-tab-pane label="事件绑定" name="eventTab">事件绑定内容</el-tab-pane>
				<el-tab-pane label="按钮绑定" name="buttonTab"></el-tab-pane>
				<el-tab-pane label="环节配置" name="nodeTab">
					<DefConfigNodeTab
						:def-xml="formProps.rowData.defXml"
						:def-code="formProps.rowData.defCode"
						:def-version="formProps.rowData.defVersion"
					></DefConfigNodeTab>
				</el-tab-pane>
			</el-tabs>
		</el-dialog>
	</div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { getWorkflowHisDeployApi, WorkflowDef } from "@/api/modules/workflow/def";
import DefConfigBaseTab from "./config/BaseTab.vue";
import DefConfigNodeTab from "./config/NodeTab.vue";

interface FormProps {
	rowData: Partial<WorkflowDef.Form>;
	getTableList: () => void;
}

const formVisible = ref(false);
const activeName = ref();
const formProps = ref<FormProps>({
	rowData: {},
	getTableList: () => {}
});

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	formProps.value.getTableList = params.getTableList;
	activeName.value = "baseTab";

	// 根据流程编码和版本号，获取流程定义
	const { data } = await getWorkflowHisDeployApi({
		defCode: params.rowData.defCode || "",
		defVersion: params.rowData.defVersion || 1
	});
	formProps.value.rowData = data;

	formVisible.value = true;
};

// 对话框关闭动画结束时的回调
const handleDialogClose = () => {
	formProps.value.getTableList();
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss">
.def-config-dialog {
	:deep(.el-dialog__header) {
		border-bottom: none;
	}
	.el-tabs--left :deep(.el-tabs__content) {
		height: 100%;
		.el-tab-pane {
			height: calc(100vh - 86px);
		}
	}
}
</style>
