<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}系统字典`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="字典类别" prop="dictypeId">
				<el-tag>{{ formProps.rowData.dictypeName }}</el-tag>
			</el-form-item>
			<el-form-item label="字典名称" prop="dicName">
				<el-input v-model="formProps.rowData.dicName" placeholder="请输入字典名称" maxlength="60" clearable></el-input>
			</el-form-item>
			<el-form-item label="字典值" prop="dicCode">
				<template #label>
					<el-tooltip content="注：仅限输入字母、数字或小数点！" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					字典值：
				</template>
				<el-input v-model="formProps.rowData.dicCode" placeholder="请输入字典值" maxlength="60" clearable></el-input>
			</el-form-item>
			<el-form-item label="排序" prop="dicSort" v-if="formProps.rowData.dicId">
				<el-input-number
					v-model="formProps.rowData.dicSort"
					:min="1"
					:max="9999"
					placeholder="请输入排序"
					controls-position="right"
					style="width: 100%"
				></el-input-number>
			</el-form-item>
			<el-form-item label="扩展字段" prop="extendField">
				<el-input
					v-model="formProps.rowData.extendField"
					placeholder="请输入扩展字段"
					type="textarea"
					:autosize="{ minRows: 3, maxRows: 5 }"
					maxlength="200"
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

<script setup lang="ts" name="SysDicForm">
import { reactive, ref } from "vue";
import { SysDic } from "@/api/modules/sys/dic";
import * as eleValidate from "@/utils/eleValidate";
import { FormInstance, ElMessage } from "element-plus";

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<SysDic.Form>;
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
	dictypeId: [{ required: true, message: "请选择字典类别" }],
	dicName: [{ required: true, message: "请输入字典名称" }],
	dicCode: [
		{ required: true, message: "请输入字典值" },
		{ validator: eleValidate.checkLetterOrNumOrDot, message: "仅限输入字母、数字或小数点" }
	],
	dicSort: [{ required: true, message: "请输入排序" }],
	dicPropList: [
		{
			required: false,
			type: "array",
			defaultField: {
				type: "object",
				fields: {
					propName: [
						{
							type: "string",
							required: true,
							trigger: "blur",
							message: "请输入属性名称"
						}
					],
					propCode: [
						{
							type: "string",
							required: true,
							trigger: "code",
							message: "请输入属性编码"
						},
						{
							validator: eleValidate.checkLetterOrNum,
							message: "属性编码仅限输入字母和数字"
						}
					]
				}
			}
		}
	]
});

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
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
