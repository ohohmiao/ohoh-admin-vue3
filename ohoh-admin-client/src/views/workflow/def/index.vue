<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			:requestApi="getWorkflowDefTypeTreeApi"
			id="id"
			label="name"
			:defaultValue="initParam.parentId"
			@change="changeTreeFilter"
		>
			<template #default="{ row }">
				<span :title="row.node.label">{{ row.node.label }}</span>
				<span class="actions" v-if="BUTTONS.addType || BUTTONS.editType || BUTTONS.deleteType">
					<el-dropdown>
						<el-link :icon="Operation"></el-link>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item
									@click="openDefTypeForm('新增', row.data)"
									v-if="BUTTONS.addType && (row.data.treeLevel == undefined || row.data.treeLevel <= 1)"
									>新增子类别</el-dropdown-item
								>
								<el-dropdown-item v-if="BUTTONS.editType && row.data.id" @click="openDefTypeForm('编辑', row.data)"
									>编辑</el-dropdown-item
								>
								<el-dropdown-item v-if="BUTTONS.deleteType && row.data.id" @click="handleDeleteDefType(row.data)"
									>删除</el-dropdown-item
								>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</span>
			</template>
		</TreeFilter>
		<div class="table-box"></div>
		<!-- 字典类别表单 -->
		<DefTypeForm ref="defTypeFormRef"></DefTypeForm>
	</div>
</template>

<script setup lang="ts" name="WorkflowDefManage">
import { reactive, ref } from "vue";
import { Operation } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import {
	WorkflowDefType,
	getWorkflowDefTypeTreeApi,
	addWorkflowDefTypeApi,
	editWorkflowDefTypeApi,
	deleteWorkflowDefTypeApi
} from "@/api/modules/workflow/def";
import DefTypeForm from "./DefTypeForm.vue";
import { useHandleData } from "@/hooks/useHandleData";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const { BUTTONS } = useAuthButtons();

// 获取 ProTable、TreeFilter 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const treeFilter = ref();
//const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({ parentId: "" });

// 树形筛选切换
const changeTreeFilter = (val: string) => {
	initParam.parentId = val;
};

// 打开流程定义类别表单
const defTypeFormRef = ref<InstanceType<typeof DefTypeForm> | null>(null);
const openDefTypeForm = (title: string, rowData: Partial<WorkflowDefType.TreeNode> = {}) => {
	let defTypeData: Partial<WorkflowDefType.TreeNode> = {};
	if (title === "新增") {
		defTypeData.parentId = rowData.id !== "" ? rowData.id : "0";
	} else {
		defTypeData = JSON.parse(JSON.stringify(rowData));
	}
	const params = {
		title,
		rowData: { ...defTypeData },
		isView: title === "查看",
		api: title === "新增" ? addWorkflowDefTypeApi : title === "编辑" ? editWorkflowDefTypeApi : undefined,
		getTableList: async () => {
			await treeFilter.value.refreshTree(initParam.parentId);
			//proTable.value.getTableList();
		}
	};
	defTypeFormRef.value?.acceptParams(params);
};

// 删除字典类别
const handleDeleteDefType = async (params: WorkflowDefType.TreeNode) => {
	if (!params.deftypeId) return;
	await useHandleData(deleteWorkflowDefTypeApi, { id: params.deftypeId }, `删除【${params.deftypeName}】流程定义类别`);
	await treeFilter.value.refreshTree(initParam.parentId);
	//proTable.value.getTableList();
};
</script>
