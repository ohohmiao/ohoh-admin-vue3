<template>
	<el-dialog
		v-model="formVisible"
		destroy-on-close
		:close-on-click-modal="false"
		:size="450"
		:title="`${formProps.title}下一步办理人`"
	>
		<el-form
			ref="formRef"
			label-suffix=" :"
			label-width="120"
			:rules="formRules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="下一环节" prop="nextnodeId">
				<el-select v-model="formProps.rowData.nextnodeId">
					<el-option v-for="item in props.nextTaskNodeList" :key="item.value" :label="item.label" :value="item.value"></el-option>
				</el-select>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { defineProps, reactive, ref, withDefaults } from "vue";
import { WorkflowHandler } from "@/api/modules/workflow/handler";
import { ElMessage, FormInstance } from "element-plus";

const props = withDefaults(
	defineProps<{
		defCode: string;
		defVersion: number;
		nodeId: string;
		nextTaskNodeList: { label: string; value: string }[];
	}>(),
	{
		nextTaskNodeList: () => []
	}
);

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowHandler.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});

const formRules = reactive({
	nextnodeId: [{ required: true, message: "请选择下一环节" }]
});

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
	formProps.value.rowData.defCode = props.defCode;
	formProps.value.rowData.defVersion = props.defVersion;
	formProps.value.rowData.nodeId = props.nodeId;

	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
