<template>
	<el-card>
		<template #header>
			<el-button type="primary" @click="handleSubmit">保存</el-button>
		</template>
		<el-form ref="formRef" label-width="145" label-suffix=" :" :rules="rules" :model="formProps.rowData">
			<el-row :gutter="16">
				<el-col :span="12">
					<el-form-item label="任务指派类别" prop="taskAssigntype">
						<el-radio-group v-model="formProps.rowData.taskAssigntype">
							<el-radio-button :value="0">单人任务</el-radio-button>
							<el-radio-button :value="1">多人任务</el-radio-button>
							<el-radio-button :value="2">自由任务</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="退回执行方式" prop="taskReturntype">
						<el-radio-group v-model="formProps.rowData.taskReturntype">
							<el-radio-button :value="0">按流程图执行</el-radio-button>
							<el-radio-button :value="1">直接返回</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="允许指定办理期限" prop="processlimitPermit">
						<el-radio-group v-model="formProps.rowData.processlimitPermit">
							<el-radio-button :value="0">禁止</el-radio-button>
							<el-radio-button :value="1">允许</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="添加审批结果控件" prop="approvalPermit">
						<el-radio-group v-model="formProps.rowData.approvalPermit">
							<el-radio-button :value="0">不添加</el-radio-button>
							<el-radio-button :value="1">添加</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</el-card>
</template>

<script setup lang="ts">
import { defineProps, ref, reactive, onMounted } from "vue";
import { WorkflowNode, addOrEditWorkflowNodeApi } from "@/api/modules/workflow/node";
import { FormInstance, ElMessage } from "element-plus";

const props = defineProps<{ rowData: Partial<WorkflowNode.Form> }>();

interface FormProps {
	rowData: Partial<WorkflowNode.Form>;
}
const formProps = ref<FormProps>({
	rowData: {}
});

const rules = reactive({
	taskAssigntype: [{ required: true, message: "请选择任务指派类别" }],
	taskReturntype: [{ required: true, message: "请选择退回执行方式" }],
	processlimitPermit: [{ required: true, message: "请选择允许指定办理期限" }],
	approvalPermit: [{ required: true, message: "请选择添加审批结果控件" }]
});

onMounted(async () => {
	formProps.value.rowData = props.rowData;
});

// 配置环节属性
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			const { msg } = await addOrEditWorkflowNodeApi(formProps.value.rowData);
			ElMessage.success({ message: msg });
		} catch (e) {
			console.log(e);
		}
	});
};
</script>

<style scoped lang="scss"></style>
