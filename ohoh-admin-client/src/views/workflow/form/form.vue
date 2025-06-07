<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="550" :title="`${formProps.title}流程表单`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="类别" prop="deftypeId">
				<el-tree-select
					v-model="formProps.rowData.deftypeId"
					:data="defTypeTreeSelectDatas"
					:props="{ label: 'name' }"
					:default-expanded-keys="defTypeTreeSelectDefaultExpandKeys"
					node-key="id"
					check-strictly
					filterable
				>
					<template #default="{ node, data }">
						<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
						<el-icon v-else><Document /></el-icon>
						<span>{{ node.label }}</span>
					</template>
				</el-tree-select>
			</el-form-item>
			<el-form-item label="表单名称" prop="formName">
				<el-input v-model="formProps.rowData.formName" placeholder="请输入表单名称" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="表单路径" prop="formPath" required>
				<template #label>
					<el-tooltip content="注：前面必须有反斜杠！" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					表单路径：
				</template>
				<el-input v-model="formProps.rowData.formPath" placeholder="请输入表单路径" maxlength="100" clearable>
					<template #prepend>src/views/workflow/workspace</template>
					<template #append>.vue</template>
				</el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-drawer>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { WorkflowDefType, getWorkflowDefTypeTreeApi } from "@/api/modules/workflow/def";
import { WorkflowForm } from "@/api/modules/workflow/form";
import { FormInstance, ElMessage } from "element-plus";

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowForm.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});
// 类别树数据
const defTypeTreeSelectDatas = ref<WorkflowDefType.TreeNode[]>([]);
// 类别树默认展开节点
const defTypeTreeSelectDefaultExpandKeys = ref<string[]>([]);

const rules = reactive({
	deftypeId: [{ required: true, message: "请选择类别" }],
	formName: [{ required: true, message: "请输入表单名称" }],
	formPath: [{ required: true, message: "请输入表单路径" }]
});

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	Object.keys(params).forEach(key => {
		if (key === "rowData") {
			// 存在默认赋值的rowData，不覆盖
			if (params[key] && Object.keys(params[key]).length) {
				formProps.value[key] = params[key];
			} else {
				formProps.value[key] = Object.assign({}, params[key], {
					//表单字段默认值
				});
			}
		} else {
			formProps.value[key] = params[key];
		}
	});

	const { data } = await getWorkflowDefTypeTreeApi();
	// 默认展开一级的树节点
	data?.forEach((item: { [key: string]: any }) => {
		if (item.parentId === "0") {
			defTypeTreeSelectDefaultExpandKeys.value.push(item.deftypeId);
		}
	});
	defTypeTreeSelectDatas.value = data;

	formVisible.value = true;
};

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			const { msg } = await formProps.value.api!(formProps.value.rowData);
			ElMessage.success({ message: msg });
			formProps.value.getTableList!();
			formVisible.value = false;
		} catch (e) {
			console.log(e);
		}
	});
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
