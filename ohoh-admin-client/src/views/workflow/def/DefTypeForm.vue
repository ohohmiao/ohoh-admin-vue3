<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}流程定义类别`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="上级类别" prop="parentId">
				<el-tree-select
					v-model="formProps.rowData.parentId"
					:data="defTypeTreeSelectDatas"
					:props="{ label: 'name' }"
					node-key="id"
					check-strictly
					filterable
					disabled
				>
					<template #default="{ node, data }">
						<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
						<el-icon v-else><Document /></el-icon>
						<span>{{ node.label }}</span>
					</template>
				</el-tree-select>
			</el-form-item>
			<el-form-item label="类别名称" prop="deftypeName">
				<el-input v-model="formProps.rowData.deftypeName" placeholder="请输入类别名称" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="类别编码" prop="deftypeCode">
				<template #label>
					<el-tooltip content="注：仅限输入字母、数字和下划线！" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					类别编码：
				</template>
				<el-input v-model="formProps.rowData.deftypeCode" placeholder="请输入类别编码" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="排序" prop="treeSort" v-if="formProps.rowData.deftypeId">
				<el-input-number
					v-model="formProps.rowData.treeSort"
					:min="1"
					:max="9999"
					placeholder="请输入排序"
					controls-position="right"
					style="width: 100%"
				></el-input-number>
			</el-form-item>
			<el-form-item label="备注" prop="deftypeRemark">
				<el-input
					v-model="formProps.rowData.deftypeRemark"
					placeholder="请输入备注"
					type="textarea"
					:autosize="{ minRows: 5, maxRows: 8 }"
					maxlength="100"
					clearable
				></el-input>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-drawer>
</template>

<script setup lang="ts" name="WorkflowDefTypeForm">
import { reactive, ref } from "vue";
import { WorkflowDefType, getWorkflowDefTypeTreeApi } from "@/api/modules/workflow/def";
import * as eleValidate from "@/utils/eleValidate";
import { FormInstance, ElMessage } from "element-plus";

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowDefType.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});
const defTypeTreeSelectDatas = ref<WorkflowDefType.TreeNode[]>([]);

const rules = reactive({
	parentId: [{ required: true, message: "请选择上级类别" }],
	deftypeName: [{ required: true, message: "请输入类别名称" }],
	deftypeCode: [
		{ required: true, message: "请输入类别编码" },
		{ validator: eleValidate.checkLetterOrNumOrUnderline, message: "仅限输入字母、数字和下划线" }
	],
	treeSort: [{ required: true, message: "请输入排序" }]
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
	// 删除多余字段
	delete formProps.value.rowData.children;

	const { data } = await getWorkflowDefTypeTreeApi();
	defTypeTreeSelectDatas.value = [
		{
			id: "0",
			name: "顶级",
			children: data
		}
	];

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
