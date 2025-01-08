<template>
	<el-card>
		<template #header>
			<el-button type="primary" plain @click="handleSubmit">保存</el-button>
		</template>
		<el-form ref="formRef" label-width="120" label-suffix=" :" :rules="rules" :model="formProps.rowData">
			<el-row :gutter="16">
				<el-col :span="12">
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
				</el-col>
				<el-col :span="12">
					<el-form-item label="流程名称" prop="defName">
						<el-input v-model="formProps.rowData.defName" placeholder="请输入流程名称" maxlength="30" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="流程编码" prop="defCode">
						<el-input
							v-model="formProps.rowData.defCode"
							placeholder="请输入流程编码"
							maxlength="30"
							clearable
							disabled
						></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="版本号" prop="defVersion">
						<el-input-number
							v-model="formProps.rowData.defVersion"
							:min="1"
							:max="9999"
							placeholder="请输入版本号"
							controls-position="right"
							style="width: 100%"
							disabled
						></el-input-number>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="排序" prop="defSort">
						<el-input-number
							v-model="formProps.rowData.defSort"
							:min="1"
							:max="9999"
							placeholder="请输入排序"
							controls-position="right"
							style="width: 100%"
						></el-input-number>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="发起范围"></el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="时限限制"></el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</el-card>
</template>

<script setup lang="ts">
import { ref, defineProps, onMounted, reactive } from "vue";
import { WorkflowDefType, WorkflowDef, getWorkflowDefTypeTreeApi, editWorkflowHisDeployApi } from "@/api/modules/workflow/def";
import * as eleValidate from "@/utils/eleValidate";
import { FormInstance, ElMessage } from "element-plus";

const props = defineProps<{ rowData: Partial<WorkflowDef.Form> }>();

interface FormProps {
	rowData: Partial<WorkflowDef.Form>;
}
const formProps = ref<FormProps>({
	rowData: {}
});
// 类别树数据
const defTypeTreeSelectDatas = ref<WorkflowDefType.TreeNode[]>([]);
// 类别树默认展开节点
const defTypeTreeSelectDefaultExpandKeys = ref<string[]>([]);

const rules = reactive({
	deftypeId: [{ required: true, message: "请选择类别" }],
	defName: [{ required: true, message: "请输入流程名称" }],
	defCode: [
		{ required: true, message: "请输入流程编码" },
		{ validator: eleValidate.checkLetterOrNumOrUnderline, message: "仅限输入字母、数字和下划线" }
	],
	defVersion: [{ required: true, message: "请输入版本号" }],
	defSort: [{ required: true, message: "请输入排序" }]
});

// 禁用流程类别树中的一级树节点
const handleDefTypeTreeCheckDisable = (params: WorkflowDefType.TreeNode[]) => {
	if (!params) return;
	params.forEach((param: WorkflowDefType.TreeNode) => {
		if (param.treeLevel == 1) {
			param.disabled = true;
		}
		handleDefTypeTreeCheckDisable(param.children || []);
	});
};

onMounted(async () => {
	const { data } = await getWorkflowDefTypeTreeApi();
	handleDefTypeTreeCheckDisable(data);
	// 默认展开一级的树节点
	data.forEach((item: { [key: string]: any }) => {
		if (item.parentId === "0") {
			defTypeTreeSelectDefaultExpandKeys.value.push(item.deftypeId);
		}
	});
	defTypeTreeSelectDatas.value = data;

	formProps.value.rowData = props.rowData;
});

// 修改基本信息
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			const { msg } = await editWorkflowHisDeployApi(formProps.value.rowData);
			ElMessage.success({ message: msg });
		} catch (e) {
			console.log(e);
		}
	});
};
</script>

<style scoped lang="scss"></style>
