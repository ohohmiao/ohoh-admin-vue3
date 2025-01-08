<template>
	<el-drawer
		v-model="formVisible"
		destroy-on-close
		:size="700"
		:title="`[${formProps.defCode}]历史版本列表`"
		@closed="handleDialogClose"
	>
		<div class="table-box">
			<ProTable
				ref="proTable"
				title="历史版本列表"
				:columns="columns"
				:requestApi="getWorkflowHisDeployListApi"
				:initParam="{ defCode: formProps.defCode }"
				:pagination="false"
				:toolButton="false"
				:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			>
				<!-- 表格操作 -->
				<template #operation="scope">
					<el-button type="primary" link :icon="Setting" @click="openDefConfigTabs(scope.row.defCode, scope.row.defVersion)"
						>配置</el-button
					>
				</template>
			</ProTable>
		</div>
	</el-drawer>
	<!-- 流程配置对话框 -->
	<DefConfigTabs ref="defConfigTabsRef"></DefConfigTabs>
</template>

<script setup lang="ts">
import { ref } from "vue";
import ProTable from "@/components/ProTable/index.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { getWorkflowHisDeployListApi, WorkflowDef } from "@/api/modules/workflow/def";
import { Setting } from "@element-plus/icons-vue";
import DefConfigTabs from "@/views/workflow/def/DefConfigTabs.vue";

interface FormProps {
	[key: string]: any;
	defCode: string;
	getTableList: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	defCode: "",
	getTableList: () => {}
});

const proTable = ref();
// 表格配置项
const columns: ColumnProps<WorkflowDef.Form>[] = [
	{ prop: "defName", label: "流程名称" },
	{ prop: "defVersion", label: "版本号", width: 150, search: { el: "input" } },
	{ prop: "operation", label: "操作", width: 150 }
];

// 对话框关闭动画结束时的回调
const handleDialogClose = () => {
	formProps.value.getTableList();
};

const acceptParams = (params: FormProps) => {
	formProps.value = params;

	formVisible.value = true;
};

const defConfigTabsRef = ref<InstanceType<typeof DefConfigTabs>>();
const openDefConfigTabs = (defCode: string, defVersion: number) => {
	const params = {
		title: `[${defCode}-${defVersion}]流程配置`,
		rowData: {
			defCode: defCode,
			defVersion: defVersion
		},
		getTableList: proTable.value.getTableList
	};
	defConfigTabsRef.value?.acceptParams(params);
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss">
.table-box {
	width: 100%;
}
</style>
