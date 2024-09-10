<template>
	<div class="main-box">
		<TreeFilter
			ref="treeFilter"
			id="positionId"
			label="positionName"
			:requestApi="getAllSysPositionTreeApi"
			:defaultValue="initParam.parentId"
			@change="changeTreeFilter"
		>
			<template #default="{ row }">
				<span :title="row.node.label">{{ row.node.label }}</span>
				<span class="actions" v-if="BUTTONS.add || (BUTTONS.edit && row.data.id) || (BUTTONS.delete && row.data.id)">
					<el-dropdown>
						<el-link :icon="Operation"></el-link>
						<template #dropdown>
							<el-dropdown-menu>
								<el-dropdown-item @click="openPositionForm('新增', row.data)" v-if="BUTTONS.add">新增子岗位</el-dropdown-item>
								<el-dropdown-item v-if="BUTTONS.edit && row.data.id" @click="openPositionForm('编辑', row.data)"
									>编辑</el-dropdown-item
								>
								<el-dropdown-item v-if="BUTTONS.delete && row.data.id" @click="handleDeletePosition(row.data)"
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
				title="岗位用户列表"
				rowKey="propId"
				:columns="columns"
				:requestApi="getSysPositionUserPageApi"
				:initParam="initParam"
				:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
			>
				<!-- 表格 header 按钮 -->
				<template #tableHeader="scope">
					<el-button type="primary" :icon="CirclePlus" @click="openGrantUserForm" v-auth="'grantUser'">授权用户</el-button>
					<el-button
						type="danger"
						:icon="Delete"
						plain
						@click="handleBatchDeletePositionUser(scope.selectedListIds)"
						:disabled="!scope.isSelected"
						v-auth="'grantUser'"
					>
						批量删除
					</el-button>
				</template>
				<!-- 表格操作 -->
				<template #operation="scope">
					<el-button type="primary" link :icon="Delete" @click="handleDeletePositionUser(scope.row)" v-auth="'grantUser'"
						>删除</el-button
					>
				</template>
			</ProTable>
		</div>
		<!-- 岗位信息表单 -->
		<SysPositionForm ref="positionFormRef"></SysPositionForm>
		<!-- 授权岗位用户表单 -->
		<SysPositionGrantUserForm ref="grantUserFormRef"></SysPositionGrantUserForm>
	</div>
</template>

<script setup lang="ts" name="SysPositionManage">
import { reactive, ref } from "vue";
import { Operation, CirclePlus, Delete } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import {
	getAllSysPositionTreeApi,
	SysPosition,
	addSysPositionApi,
	editSysPositionApi,
	deleteSysPositionApi,
	getSysPositionUserPageApi,
	revokeUserSysPositionApi
} from "@/api/modules/sys/position";
import SysPositionForm from "./form.vue";
import SysPositionGrantUserForm from "./GrantUserForm.vue";
import { ColumnProps } from "@/components/ProTable/interface";
import { useHandleData } from "@/hooks/useHandleData";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const { BUTTONS, authButtons } = useAuthButtons();
// 获取 ProTable、TreeFilter 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const treeFilter = ref();
const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({ parentId: "" });

// 树形筛选切换
const changeTreeFilter = (val: string) => {
	initParam.parentId = val;
};

const columns: ColumnProps<SysPosition.Form>[] = [
	{ type: "selection", fixed: "left", width: 80 },
	{ prop: "positionName", label: "岗位名称", width: 170 },
	{ prop: "orgName", label: "所属组织", width: 170 },
	{ prop: "userAccount", label: "用户账号", width: 170 },
	{ prop: "userName", label: "用户姓名", search: { el: "input" } }
];
if (authButtons.includes("grantUser")) {
	columns.push({ prop: "operation", label: "操作", width: 170, fixed: "right" });
}

// 打开岗位信息表单
const positionFormRef = ref<InstanceType<typeof SysPositionForm> | null>(null);
const openPositionForm = (title: string, rowData: Partial<SysPosition.TreeNode> = {}) => {
	let positionData: Partial<SysPosition.TreeNode> = {};
	if (title === "新增") {
		positionData.parentId = rowData.id !== "" ? rowData.id : "0";
	} else {
		positionData = JSON.parse(JSON.stringify(rowData));
	}
	const params = {
		title,
		rowData: { ...positionData },
		isView: title === "查看",
		api: title === "新增" ? addSysPositionApi : title === "编辑" ? editSysPositionApi : undefined,
		getTableList: async () => {
			await treeFilter.value.refreshTree(initParam.parentId);
			proTable.value.getTableList();
		}
	};
	positionFormRef.value?.acceptParams(params);
};

// 删除岗位
const handleDeletePosition = async (params: SysPosition.TreeNode) => {
	if (!params.positionId) return;
	await useHandleData(deleteSysPositionApi, { id: params.positionId }, `删除【${params.positionName}】岗位`);
	await treeFilter.value.refreshTree(initParam.parentId);
	proTable.value.getTableList();
};

// 打开授权岗位用户表单
const grantUserFormRef = ref<InstanceType<typeof SysPositionGrantUserForm> | null>(null);
const openGrantUserForm = () => {
	const params = {
		rowData: {
			positionId: initParam.parentId || ""
		},
		getTableList: proTable.value.getTableList
	};
	grantUserFormRef.value?.acceptParams(params);
};

// 批量删除岗位用户
const handleBatchDeletePositionUser = async (id: string[]) => {
	await useHandleData(revokeUserSysPositionApi, { id }, "删除所选的岗位用户关系");
	proTable.value.clearSelection();
	proTable.value.getTableList();
};

// 删除岗位用户
const handleDeletePositionUser = async (params: SysPosition.PositionUserProp) => {
	await useHandleData(revokeUserSysPositionApi, { id: [params.propId] }, "删除所选的岗位用户关系");
	proTable.value.getTableList();
};
</script>
