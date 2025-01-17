<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="系统资源列表"
			rowKey="id"
			:indent="30"
			:columns="columns"
			:requestApi="getSysResTreeApi"
			:initParam="initParam"
			:pagination="false"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
		>
			<!-- 表格 header 按钮 -->
			<template #tableHeader>
				<el-button type="primary" :icon="CirclePlus" @click="openForm('新增')" v-auth="'add'">新增</el-button>
			</template>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" link :icon="EditPen" @click="openForm('编辑', scope.row)" v-auth="'edit'">编辑</el-button>
				<el-button type="primary" link :icon="Delete" @click="handleDelete(scope.row)" v-auth="'delete'">删除</el-button>
			</template>
		</ProTable>
		<!-- 系统资源表单 -->
		<SysResForm ref="formRef" />
	</div>
</template>

<script setup lang="tsx" name="SysResMange">
import { reactive, ref, resolveComponent, h } from "vue";
import { CirclePlus, EditPen, Delete } from "@element-plus/icons-vue";
import { ColumnProps } from "@/components/ProTable/interface";
import ProTable from "@/components/ProTable/index.vue";
import { SysRes, getSysResTreeApi, addSysResApi, editSysResApi, deleteSysResApi } from "@/api/modules/sys/res";
import SysResForm from "./form.vue";
import { useHandleData } from "@/hooks/useHandleData";
import { useAuthButtons } from "@/hooks/useAuthButtons";

const { authButtons } = useAuthButtons();
// 获取 ProTable 元素，调用其获取刷新数据方法（还能获取到当前查询参数，方便导出携带参数）
const proTable = ref();

// 如果表格需要初始化请求参数，直接定义传给 ProTable(之后每次请求都会自动带上该参数，此参数更改之后也会一直带上，改变此参数会自动刷新表格数据)
const initParam = reactive({});

// 表格配置项
const columns: ColumnProps<SysRes.Form>[] = [
	{ prop: "resName", label: "资源名称", search: { el: "input" } },
	{
		prop: "resIcon",
		label: "资源图标",
		render: scope => {
			return scope.row.resIcon ? <el-icon> {h(resolveComponent(scope.row.resIcon))} </el-icon> : "--";
		}
	},
	{
		prop: "resType",
		label: "资源类别",
		tag: true,
		search: { el: "select" },
		enum: [
			{ label: "菜单", value: 0, tagType: "success" },
			{ label: "按钮", value: 1, tagType: "primary" }
		]
	},
	/*{
		prop: "menuType",
		label: "菜单类别",
		tag: true,
		enum: [
			{ label: "目录", value: 0, tagType: "success" },
			{ label: "组件", value: 1, tagType: "" },
			{ label: "外链", value: 2, tagType: "warning" }
		]
	},*/
	{ prop: "resCode", label: "资源编码" },
	{ prop: "resPath", label: "组件路径" },
	{ prop: "treeSort", label: "排序" }
];
if (authButtons.includes("edit") || authButtons.includes("delete")) {
	columns.push({ prop: "operation", label: "操作", width: 270 });
}

// 删除
const handleDelete = async (params: SysRes.Form) => {
	if (!params.resId) return;
	await useHandleData(deleteSysResApi, { id: params.resId }, `删除【${params.resName}】资源`);
	proTable.value.getTableList();
};

// 打开form（查看、新增、编辑）
const formRef = ref<InstanceType<typeof SysResForm> | null>(null);
const openForm = (title: string, rowData: Partial<SysRes.Form> = {}) => {
	const params = {
		title,
		rowData: { ...JSON.parse(JSON.stringify(rowData)) },
		isView: title === "查看",
		api: title === "新增" ? addSysResApi : title === "编辑" ? editSysResApi : undefined,
		getTableList: proTable.value.getTableList
	};
	formRef.value?.acceptParams(params);
};
</script>
