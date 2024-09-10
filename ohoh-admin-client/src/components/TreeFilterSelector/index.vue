<template>
	<el-drawer v-model="selectorVisible" destroy-on-close :size="450" :title="title || '请选择'">
		<el-form ref="formRef" label-position="top" label-suffix=" :" :rules="rules" :model="formProps.rowData">
			<!-- 表单域字段插槽 -->
			<slot name="formItem"></slot>
			<!-- 主选择区域 -->
			<el-form-item :label="itemTitle || '选项'" prop="selected" v-if="formProps.isTreeFieldShow">
				<el-row v-if="allowChangeCheckStrictly">
					<el-col :span="24">
						<el-checkbox v-model="checkStrictlyVal" label="父子联动（选中父节点，自动选择子节点）" />
					</el-col>
				</el-row>
				<el-row :gutter="16">
					<el-col :span="12">
						<el-form-item>
							<el-checkbox label="展开/折叠" @change="handleTreeExpandAllOrNot" />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item>
							<el-checkbox label="全选/全不选" @change="handleTreeSelectAllOrNot" />
						</el-form-item>
					</el-col>
				</el-row>
				<div class="card filter">
					<el-input v-model="filterText" placeholder="输入关键字进行过滤" clearable />
					<el-scrollbar :style="{ height: `calc(100% - 56px)` }">
						<el-tree
							ref="treeRef"
							:default-expanded-keys="defaultExpandedKeys"
							:node-key="idField"
							:data="treeData"
							show-checkbox
							:check-strictly="!checkStrictlyVal"
							:expand-on-click-node="false"
							check-on-click-node
							:props="defaultTreeNodeProps"
							:filter-node-method="filterNode"
							:default-checked-keys="formProps.rowData.selected"
							@check="handleCheckChange"
						>
							<template #default="scope">
								<span class="el-tree-node__label">
									<slot name="treeNode" :row="scope">
										{{ scope.node.label }}
									</slot>
								</span>
							</template>
						</el-tree>
					</el-scrollbar>
				</div>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="selectorVisible = false">取消</el-button>
			<el-button type="primary" @click="handleSubmit">确定</el-button>
		</template>
	</el-drawer>
</template>

<script setup lang="ts" name="treeFilterSelector">
import { ref, watch, onBeforeMount, reactive } from "vue";
import { ElTree, FormInstance } from "element-plus";
import Node from "element-plus/es/components/tree/src/model/node";

// 接收父组件参数并设置默认值
interface TreeFilterSelectorProps {
	requestApi?: (data?: any) => Promise<any>; // 请求分类数据的 api ==> 非必传
	data?: Node[]; // 分类数据，如果有分类数据，则不会执行 api 请求 ==> 非必传
	title?: string; // treeFilter 标题 ==> 非必传
	itemTitle?: string; // 选项标题 ==> 非必传
	idField?: string; // 选择的id ==> 非必传，默认为 “id”
	labelField?: string; // 显示的label ==> 非必传，默认为 “label”
	isNotEmpty?: boolean; // 是否必选 ==> true=必选false=非必选。非必传，默认true
	allowChangeCheckStrictly?: boolean; // 是否严格的遵循父子不互相关联的做法 ==> 非必传，默认为 false
	checkDisableHookFun?: Function; // 禁用树节点复选框插件函数
}

const props = withDefaults(defineProps<TreeFilterSelectorProps>(), {
	idField: "id",
	labelField: "label",
	isNotEmpty: true,
	allowChangeCheckStrictly: false
});

const defaultTreeNodeProps = {
	children: "children",
	label: props.labelField,
	disabled: "disabled"
};

const rules = reactive({
	selected: [{ type: "array", required: props.isNotEmpty, trigger: "change", message: `${props.itemTitle || "选项"}不能为空` }]
});

interface FormProps {
	[key: string]: any;
	isTreeFieldShow?: boolean;
	rowData: {
		selected: string[];
	};
	api?: (params: any, selected: string[]) => Promise<any>;
}

const formProps = ref<FormProps>({
	isTreeFieldShow: true,
	rowData: {
		selected: []
	}
});

const selectorVisible = ref(false);

const treeRef = ref<InstanceType<typeof ElTree>>();
const treeData = ref<Node[]>([]);
const checkStrictlyVal = ref(false);

// 默认展开的节点key
let defaultExpandedKeys = ref<string[]>([]);

onBeforeMount(async () => {
	filterText.value = "";
	if (props.requestApi) {
		const { data } = await props.requestApi!();
		// 默认展开一级的树节点
		data?.forEach((item: { [key: string]: any }) => {
			if (item.parentId === "0") {
				defaultExpandedKeys.value.push(item[props.idField]);
			}
		});
		treeData.value = data;
	}
	if (props.data?.length) {
		treeData.value = props.data;
	}
});

watch(
	() => props.data,
	() => {
		if (props.data?.length) {
			// 默认展开一级的树节点
			props.data.forEach((item: { [key: string]: any }) => {
				if (item.parentId === "0") {
					defaultExpandedKeys.value.push(item[props.idField]);
				}
			});
			treeData.value = props.data;
		}
	},
	{ deep: true, immediate: true }
);

const filterText = ref("");
watch(filterText, val => {
	treeRef.value!.filter(val);
});

// 过滤
const filterNode = (value: string, data: { [key: string]: any }, node: any) => {
	if (!value) return true;
	let parentNode = node.parent,
		labels = [node.label],
		level = 1;
	while (level < node.level) {
		labels = [...labels, parentNode.label];
		parentNode = parentNode.parent;
		level++;
	}
	return labels.some(label => label.indexOf(value) !== -1);
};

// 展开/折叠
const handleTreeExpandAllOrNot = (checked: boolean) => {
	formRef.value?.clearValidate();
	recurseExpandOrNotTreeNode(treeData.value, checked);
};
const recurseExpandOrNotTreeNode = (tree: { [p: string]: any }[], expandOrNot: boolean) => {
	tree.forEach(node => {
		treeRef.value!.getNode(node[props.idField]).expanded = expandOrNot;
		if (node.children) {
			recurseExpandOrNotTreeNode(node.children, expandOrNot);
		}
	});
};

// 全选/全不选
const handleTreeSelectAllOrNot = (checked: boolean) => {
	formRef.value?.clearValidate();
	if (checked) {
		treeRef.value?.setCheckedNodes(treeData.value);
	} else {
		treeRef.value?.setCheckedKeys([]);
	}
};

// 多选
const handleCheckChange = () => {
	formRef.value?.clearValidate();
};

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
	formProps.value = params;
	checkStrictlyVal.value = !props.allowChangeCheckStrictly;
	props.checkDisableHookFun && props.checkDisableHookFun(treeData.value);
	selectorVisible.value = true;
};

// 确定选择
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formProps.value.rowData.selected = treeRef.value ? treeRef.value.getCheckedKeys().map(checkedKey => String(checkedKey)) : [];
	formRef.value!.validate(async valid => {
		if (!valid) return;
		await formProps.value.api!(
			formProps.value.rowData,
			treeRef.value?.getCheckedKeys() ? treeRef.value.getCheckedKeys().map(treeKey => String(treeKey)) : []
		);
		selectorVisible.value = false;
	});
};

// 暴露给父组件使用
defineExpose({ acceptParams, formProps });
</script>

<style scoped lang="scss">
@import "./index.scss";
</style>
