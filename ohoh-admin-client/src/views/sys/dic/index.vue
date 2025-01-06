<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			:requestApi="getSysDicTypeTreeApi"
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
								<el-dropdown-item @click="openDicTypeForm('新增', row.data)" v-if="BUTTONS.addType">新增子类别</el-dropdown-item>
								<el-dropdown-item v-if="BUTTONS.editType && row.data.id" @click="openDicTypeForm('编辑', row.data)"
									>编辑</el-dropdown-item
								>
								<el-dropdown-item v-if="BUTTONS.deleteType && row.data.id" @click="handleDeleteDicType(row.data)"
									>删除</el-dropdown-item
								>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</span>
			</template>
		</TreeFilter>
		<div class="table-box">
			<ProTable
				ref="proTable"
				title="系统字典列表"
				rowKey="dicId"
				:columns="columns"
				:requestApi="getSysDicPageApi"
				:initParam="initParam"
				:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			>
				<!-- 表格 header 按钮 -->
				<template #tableHeader="scope">
					<el-button type="primary" :icon="CirclePlus" @click="openDicForm('新增')" v-auth="'add'">新增</el-button>
					<el-button
						type="danger"
						:icon="Delete"
						plain
						@click="handleBatchDeleteDic(scope.selectedListIds)"
						:disabled="!scope.isSelected"
						v-auth="'delete'"
					>
						批量删除
					</el-button>
				</template>
				<!-- 表格操作 -->
				<template #operation="scope">
					<el-button type="primary" link :icon="EditPen" @click="openDicForm('编辑', scope.row)" v-auth="'edit'">编辑</el-button>
					<el-button type="primary" link :icon="Delete" @click="handleDeleteDic(scope.row)" v-auth="'delete'">删除</el-button>
				</template>
			</ProTable>
		</div>
		<!-- 字典类别表单 -->
		<DicTypeForm ref="dicTypeFormRef"></DicTypeForm>
		<!-- 字典表单 -->
		<DicForm ref="dicFormRef"></DicForm>
	</div>
</template>

<script setup lang="tsx" name="SysDicManage">
import { reactive, ref } from "vue";
import { Operation, CirclePlus, EditPen, Delete } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import {
	getSysDicTypeTreeApi,
	getSysDicPageApi,
	SysDic,
	SysDicType,
	addSysDicTypeApi,
	editSysDicTypeApi,
	deleteSysDicTypeApi,
	addSysDicApi,
	editSysDicApi,
	deleteSysDicApi
} from "@/api/modules/sys/dic";
import { ColumnProps } from "@/components/ProTable/interface";
import DicTypeForm from "./DicTypeForm.vue";
import DicForm from "./DicForm.vue";
import { ElMessage } from "element-plus";
import { useHandleData } from "@/hooks/useHandleData";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const { BUTTONS, authButtons } = useAuthButtons();
const treeFilter = ref();
const proTable = ref();

const initParam = reactive({ parentId: "" });
const changeTreeFilter = (val: string) => {
	initParam.parentId = val;
};

const columns: ColumnProps<SysDic.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{
		prop: "dictypeName",
		label: "字典类别",
		width: 120,
		render: scope => {
			return <el-tag>{scope.row.dictypeName}</el-tag>;
		}
	},
	{ prop: "dicName", label: "字典名称", search: { el: "input" } },
	{ prop: "dicCode", label: "字典值", width: 200 },
	{ prop: "dicSort", label: "排序", width: 80 }
];
if (authButtons.includes("edit") || authButtons.includes("delete")) {
	columns.push({ prop: "operation", label: "操作", width: 270, fixed: "right" });
}

// 打开字典类别表单
const dicTypeFormRef = ref<InstanceType<typeof DicTypeForm> | null>(null);
const openDicTypeForm = (title: string, rowData: Partial<SysDicType.TreeNode> = {}) => {
	let dicTypeData: Partial<SysDicType.TreeNode> = {};
	if (title === "新增") {
		dicTypeData.parentId = rowData.id !== "" ? rowData.id : "0";
	} else {
		dicTypeData = JSON.parse(JSON.stringify(rowData));
	}
	const params = {
		title,
		rowData: { ...dicTypeData },
		isView: title === "查看",
		api: title === "新增" ? addSysDicTypeApi : title === "编辑" ? editSysDicTypeApi : undefined,
		getTableList: async () => {
			await treeFilter.value.refreshTree(initParam.parentId);
			proTable.value.getTableList();
		}
	};
	dicTypeFormRef.value?.acceptParams(params);
};

// 删除字典类别
const handleDeleteDicType = async (params: SysDicType.TreeNode) => {
	if (!params.dictypeId) return;
	await useHandleData(deleteSysDicTypeApi, { id: params.dictypeId }, `删除【${params.dictypeName}】系统字典类别`);
	await treeFilter.value.refreshTree(initParam.parentId);
	proTable.value.getTableList();
};

// 打开字典表单
const dicFormRef = ref<InstanceType<typeof DicForm> | null>(null);
const openDicForm = (title: string, rowData: Partial<SysDic.Form> = {}) => {
	if (title == "新增") {
		if (initParam.parentId == "") {
			ElMessage.warning({ message: "请在左侧选择对应的字典类别" });
			return;
		}
		const selDicTypeNode = treeFilter.value.treeRef.getNode(initParam.parentId);
		rowData.dictypeId = selDicTypeNode.data.dictypeId;
		rowData.dictypeName = selDicTypeNode.data.dictypeName;
	}
	const params = {
		title,
		rowData: { ...rowData },
		isView: title === "查看",
		api: title === "新增" ? addSysDicApi : title === "编辑" ? editSysDicApi : undefined,
		getTableList: proTable.value.getTableList
	};
	dicFormRef.value?.acceptParams(params);
};

// 删除字典
const handleDeleteDic = async (params: SysDic.Form) => {
	await useHandleData(deleteSysDicApi, { id: [params.dicId] }, `删除【${params.dicName}】系统字典`);
	proTable.value.getTableList();
};

// 批量删除字典
const handleBatchDeleteDic = async (id: string[]) => {
	await useHandleData(deleteSysDicApi, { id }, `删除所选系统字典`);
	proTable.value.clearSelection();
	proTable.value.getTableList();
};
</script>
