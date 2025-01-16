<template>
	<el-dialog v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}流程事件`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView">确定</el-button>
		</template>
	</el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { WorkflowEvent } from "@/api/modules/workflow/event";
import { FormInstance } from "element-plus";

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowEvent.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});

const rules = reactive({});

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();

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

	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
