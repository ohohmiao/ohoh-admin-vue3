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
			</el-row>
			<el-row :gutter="16">
				<el-col :span="12" v-if="formProps.rowData.taskAssigntype == 1">
					<el-form-item label="多人决策方式" prop="multiassignRule" required>
						<el-radio-group v-model="formProps.rowData.multiassignRule" @change="handleMultiAssignRuleChange">
							<el-radio-button :value="0">少数服从多数</el-radio-button>
							<el-radio-button :value="1">以最后一个结果为准</el-radio-button>
							<el-radio-button :value="2">按比例</el-radio-button>
							<el-radio-button :value="3">按权重</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12" v-if="formProps.rowData.multiassignRule == 2">
					<el-form-item label="决策比例" prop="multiassignRatio" required>
						<el-input-number v-model="formProps.rowData.multiassignRatio" :min="1" :max="100" style="width: 100%">
							<template #suffix>
								<span>%</span>
							</template>
						</el-input-number>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="16" v-if="formProps.rowData.multiassignRule == 3">
				<el-col :span="24">
					<el-form-item label="办理人权重配置" prop="multiassignWeightjson" class="addchild-table" required>
						<el-button type="primary" plain class="addchild-btn">重置</el-button>
						<el-row :gutter="10" class="addchild-table-header">
							<el-col :span="12" class="addchild-table-header-col">办理人</el-col>
							<el-col :span="12" class="addchild-table-header-col">权重分</el-col>
						</el-row>
						<div
							v-for="(thizWeight, index) in formProps.rowData.multiassignWeightjson"
							:key="thizWeight.handlerId"
							class="addchild-table-content"
						>
							<el-row :gutter="10">
								<el-col :span="12">
									<el-form-item :prop="'formProps.rowData.multiassignWeightjson.' + index + '.handlerName'">{{
										thizWeight.handlerName
									}}</el-form-item>
								</el-col>
								<el-col :span="12">
									<el-form-item :prop="'formProps.rowData.multiassignWeightjson.' + index + '.weight'">
										<el-input-number v-model="thizWeight.weight" :min="1" :max="10"></el-input-number>
									</el-form-item>
								</el-col>
							</el-row>
						</div>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row :gutter="16">
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
import { WorkflowNode, addOrEditWorkflowNodeApi, getMultiAssignWeightListApi } from "@/api/modules/workflow/node";
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
	multiassignRule: [
		{
			type: "number",
			validator: (rule: any, value: any) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.taskAssigntype == 1) {
						if (value == null) {
							reject("请选择多人决策方式");
						} else {
							resolve("");
						}
					} else {
						resolve("");
					}
				});
			}
		}
	],
	multiassignRatio: [
		{
			type: "number",
			validator: (rule: any, value: any) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.multiassignRule == 2) {
						if (value == null) {
							reject("请输入决策比例");
						} else {
							resolve("");
						}
					} else {
						resolve("");
					}
				});
			}
		}
	],
	multiassignWeightjson: [
		{
			required: true,
			type: "array",
			message: "请配置办理人权重",
			defaultField: {
				type: "object",
				fields: {
					weight: [
						{
							type: "number",
							required: true,
							trigger: "change",
							message: "请输入权重分"
						}
					]
				}
			}
		}
	],
	taskReturntype: [{ required: true, message: "请选择退回执行方式" }],
	processlimitPermit: [{ required: true, message: "请选择允许指定办理期限" }],
	approvalPermit: [{ required: true, message: "请选择添加审批结果控件" }]
});

// 权重配置表格

const handleMultiAssignRuleChange = async (val: number) => {
	if (val == 3) {
		const { data } = await getMultiAssignWeightListApi({
			defCode: formProps.value.rowData.defCode || "-1",
			defVersion: formProps.value.rowData.defVersion || -1,
			nodeId: formProps.value.rowData.nodeId || "-1"
		});
		formProps.value.rowData.multiassignWeightjson = data;
	} else {
		formProps.value.rowData.multiassignWeightjson = undefined;
	}
};

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

<style scoped lang="scss">
.addchild-table {
	:deep(.el-form-item__content) {
		display: block;
	}
	.addchild-btn {
		margin-bottom: 8px;
	}
	.addchild-table-header {
		margin-left: 0 !important;
		background-color: #f5f5f5;
		.addchild-table-header-col {
			padding-top: 5px;
			padding-bottom: 5px;
			padding-left: 15px;
			text-align: center;
		}
	}
	.addchild-table-content {
		padding-top: 15px;
		:deep(.el-col) {
			text-align: center;
		}
	}
}
</style>
