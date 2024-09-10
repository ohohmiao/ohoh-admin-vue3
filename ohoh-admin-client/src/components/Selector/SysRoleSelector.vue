<template>
	<div class="selector-dialog">
		<el-dialog v-model="selectorVisible" :title="selectorProps.title" @close="handleClose" width="70%" top="5vh">
			<div class="main-box">
				<TreeFilter
					ref="orgTreeFilter"
					id="orgId"
					label="orgName"
					:requestApi="selectorProps.treeApi"
					:defaultValue="roleTableInitParam.orgId"
					@change="changeOrgTreeFilter"
				>
					<template #default="{ row }">
						<el-icon v-if="row.data.children"><FolderOpened v-if="row.node.expanded" /><Folder v-else /></el-icon>
						<el-icon v-else><Document /></el-icon>
						<span>{{ row.node.label }}</span>
					</template>
				</TreeFilter>
				<div class="table-box">
					<ProTable
						ref="roleTable"
						:columns="roleTableCols"
						:requestApi="selectorProps.pageApi"
						:initParam="roleTableInitParam"
						:searchCol="{ xs: 1, sm: 1, md: 2, lg: 2, xl: 2 }"
						:toolButton="false"
						paginationLayout="total, prev, pager, next"
						@row-dblclick="handleTableDbClick"
					>
						<template #tableHeader="scope">
							<el-button plain type="primary" :icon="CirclePlus" @click="handleSelect(scope.selectedList)">添加</el-button>
						</template>
					</ProTable>
				</div>
				<div class="selected-table-box">
					<ProTable
						ref="selectedTable"
						:columns="selectedTableCols"
						:data="selectedTableDatas"
						rowKey="value"
						:toolButton="false"
						:pagination="false"
						@row-dblclick="handleSelectedTableDbClick"
					>
						<template #tableHeader="scope">
							<el-row>
								<el-col class="table-header-tip" :span="12"
									>已选择<span class="num">{{ selectedTableDatas.length }}</span
									>条</el-col
								>
								<el-col class="table-header-operate" :span="12"
									><el-button type="danger" plain :icon="Delete" @click="handleDelSelected(scope.selectedListIds)"
										>移除</el-button
									></el-col
								>
							</el-row>
						</template>
					</ProTable>
				</div>
			</div>
			<!-- 选择器按钮区域 -->
			<template #footer>
				<el-button @click="selectorVisible = false">取消</el-button>
				<el-button type="primary" @click="handleSubmit">确定</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="tsx" name="SysRoleSelector">
import { reactive, ref, nextTick } from "vue";
import { CirclePlus, Delete } from "@element-plus/icons-vue";
import TreeFilter from "@/components/TreeFilter/index.vue";
import ProTable from "@/components/ProTable/index.vue";
import { SysRole } from "@/api/modules/sys/role";
import { ColumnProps } from "@/components/ProTable/interface";
import { getAuthSysOrgTreeApi } from "@/api/modules/sys/org";
import { getAuthSysRolePageApi } from "@/api/modules/sys/role";
import { ElMessage } from "element-plus";

// 组件参数定义
interface SelectorProps {
	title?: string; //标题
	treeApi?: (params: any) => Promise<any>; //树形接口api
	pageApi?: (params: any) => Promise<any>; //表格接口api
	maxSelectNum?: number; //最大选择数 <=0 不限制
	notEmpty?: boolean; //是否允许不选，true=禁止不选false=允许不选
}
// 返回值定义
interface SelectorResult {
	label: string;
	value: string;
}

// 接受父组件参数，配置默认值
const selectorProps = withDefaults(defineProps<SelectorProps>(), {
	title: "请选择",
	treeApi: getAuthSysOrgTreeApi,
	pageApi: getAuthSysRolePageApi,
	maxSelectNum: 0,
	notEmpty: true
});

const selectorVisible = ref(false);

const orgTreeFilter = ref();
const roleTable = ref();
const selectedTable = ref();

const roleTableInitParam = reactive({ orgId: "" });

const changeOrgTreeFilter = (val: string) => {
	roleTableInitParam.orgId = val;
};

const roleTableCols: ColumnProps<SysRole.Form>[] = [
	{ type: "selection", fixed: "left", width: 50 },
	{
		prop: "orgName",
		label: "组织名称",
		width: 150,
		render: scope => {
			return scope.row.orgName ? scope.row.orgName : <el-tag>全局角色</el-tag>;
		}
	},
	{ prop: "roleName", label: "角色名称", search: { el: "input" } }
];

const selectedTableCols: ColumnProps<SysRole.Form>[] = [
	{ type: "selection", fixed: "left", width: 50 },
	{ prop: "label", label: "角色名称" }
];
const selectedTableDatas = ref<SelectorResult[]>([]);

// 新增选中
const handleSelect = (params: SysRole.Form[]) => {
	if (!params.length) {
		ElMessage.warning({ message: "请选择要添加的记录" });
		return false;
	}
	params.forEach(param => {
		if (!selectedTableDatas.value.find(data => data.value == param.roleId)) {
			selectedTableDatas.value.push({
				label: param.roleName,
				value: param.roleId
			});
		}
	});
	roleTable.value.clearSelection();
};
// 双击选中
const handleTableDbClick = (row: any) => {
	handleSelect([row]);
};

// 删除已选
const handleDelSelected = (ids: string[]) => {
	if (!ids.length) {
		ElMessage.warning({ message: "请选择要移除的记录" });
		return false;
	}
	selectedTableDatas.value = selectedTableDatas.value.filter(data => ids.indexOf(data.value) === -1);
	selectedTable.value.clearSelection();
};
// 双击移除
const handleSelectedTableDbClick = (row: any) => {
	handleDelSelected([row.value]);
};

// 接收父组件传过来的参数
const acceptParams = (params: { [key: string]: any }) => {
	nextTick(() => {
		roleTableInitParam.orgId = "";
		orgTreeFilter.value?.refreshTree(roleTableInitParam.orgId);
	});
	selectedTableDatas.value = params.selected || [];
	selectorVisible.value = true;
};

// 选中回调监听，触发父组件改变
type EmitProps = {
	(e: "select", val: { [key: string]: any }[]): void;
};
const emit = defineEmits<EmitProps>();

// 确定选择
const handleSubmit = () => {
	if (selectorProps.notEmpty && !selectedTableDatas.value.length) {
		ElMessage.warning({ message: "请选择" });
		return false;
	}
	if (selectorProps.maxSelectNum > 0) {
		if (selectedTableDatas.value.length > selectorProps.maxSelectNum) {
			ElMessage.warning({ message: `限制选择${selectorProps.maxSelectNum}条记录` });
			return false;
		}
	}
	emit("select", JSON.parse(JSON.stringify(selectedTableDatas.value)));
	selectorVisible.value = false;
};

// 关闭
const handleClose = () => {
	roleTable.value.searchParam = {};
	roleTable.value.clearSelection();
	selectedTable.value.clearSelection();
	selectorVisible.value = false;
};

// 暴露给父组件的参数和方法(外部需要什么，都可以从这里暴露出去)
defineExpose({
	acceptParams: acceptParams
});
</script>

<style lang="scss" scoped>
.selector-dialog :deep(.el-dialog__body) {
	padding: 5px 5px 0;
	.main-box {
		height: 480px;
		.filter {
			margin-right: 5px;
		}
		.table-box {
			width: calc(100% - 500px);
			.table-search {
				margin-bottom: 5px;
			}
		}
		.selected-table-box {
			width: 260px;
			margin-left: 5px;
			.table-header .header-button-lf {
				float: none;
				.table-header-tip {
					line-height: 32px;
					.num {
						margin: 0 5px;
						color: var(--el-color-primary);
					}
				}
				.table-header-operate {
					text-align: right;
				}
			}
		}
	}
}
</style>
