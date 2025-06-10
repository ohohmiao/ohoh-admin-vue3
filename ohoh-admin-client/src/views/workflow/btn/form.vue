<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}流程按钮`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="按钮文本" prop="btnText">
				<el-input v-model="formProps.rowData.btnText" placeholder="请输入按钮文本" maxlength="10" clearable></el-input>
			</el-form-item>
			<el-form-item label="按钮颜色" prop="btnColor">
				<el-color-picker v-model="formProps.rowData.btnColor" />
			</el-form-item>
			<el-form-item label="执行方法" prop="btnFun">
				<template #label>
					<el-tooltip content="注：仅限输入字母！" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					执行方法：
				</template>
				<el-input v-model="formProps.rowData.btnFun" placeholder="请输入执行方法" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="排序" prop="btnSort" v-if="formProps.rowData.btnId">
				<el-input-number
					v-model="formProps.rowData.btnSort"
					:min="1"
					:max="99999"
					placeholder="请输入排序"
					controls-position="right"
					style="width: 100%"
				></el-input-number>
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
import { WorkflowBtn } from "@/api/modules/workflow/btn";
import { FormInstance, ElMessage } from "element-plus";
import * as eleValidate from "@/utils/eleValidate";

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<WorkflowBtn.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});

const rules = reactive({
	btnText: [{ required: true, message: "请输入按钮文本" }],
	btnColor: [{ required: true, message: "请选择按钮颜色" }],
	btnFun: [
		{ required: true, message: "请输入执行方法" },
		{ validator: eleValidate.checkLetter, message: "仅限输入字母" }
	],
	btnSort: [{ required: true, message: "请输入排序" }]
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
