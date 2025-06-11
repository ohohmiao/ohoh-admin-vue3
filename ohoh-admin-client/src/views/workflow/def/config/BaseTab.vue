<template>
	<el-card>
		<template #header>
			<el-button type="primary" @click="handleSubmit">保存</el-button>
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
				<el-col :span="12"></el-col>
				<el-col :span="24">
					<el-form-item label="发起范围" prop="initiatorScope">
						<el-radio-group v-model="formProps.rowData.initiatorScope">
							<el-radio-button :value="0">全体人员</el-radio-button>
							<el-radio-button :value="1">指定人员</el-radio-button>
						</el-radio-group>
					</el-form-item>
					<el-form-item label="指定人员" prop="targetInitiators" v-if="formProps.rowData.initiatorScope == 1" required>
						<div class="selector-box">
							<div class="selector-box-top">
								<el-tag
									v-for="(item, index) in formProps.rowData.targetInitiators"
									:key="index"
									:type="['primary', 'success', 'info', 'warning'][[SelectorTypeEnum.USER, SelectorTypeEnum.ORG, SelectorTypeEnum.POSITION, SelectorTypeEnum.CONTACT].indexOf(formProps.rowData.targetInitiators![index].referRestype)]"
									plain
									closable
									@close="formProps.rowData.targetInitiators?.splice(index, 1)"
									>{{ item.referResname }}</el-tag
								>
							</div>
							<div class="selector-box-bottom">
								<span
									class="selector-btn"
									@click="formProps.rowData.targetInitiators?.splice(0, formProps.rowData.targetInitiators.length)"
									><el-icon><Delete /></el-icon>清空</span
								>
								<span class="selector-btn" @click="openInitiatorSelector"
									><el-icon><Pointer /></el-icon>选择</span
								>
							</div>
						</div>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="时限限制" prop="processLimittype">
						<el-radio-group v-model="formProps.rowData.processLimittype">
							<el-radio-button :value="0">不限制</el-radio-button>
							<el-radio-button :value="1">按工作日</el-radio-button>
							<el-radio-button :value="2">按自然日</el-radio-button>
							<el-radio-button :value="3">按小时</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item
						:label="computedProcessLimitTypeName"
						prop="processLimitvalue"
						v-if="formProps.rowData.processLimittype != undefined && formProps.rowData.processLimittype != 0"
						required
					>
						<el-input-number
							v-model="formProps.rowData.processLimitvalue"
							:min="1"
							:max="999"
							:placeholder="'请输入' + computedProcessLimitTypeName"
							controls-position="right"
							style="width: 100%"
						></el-input-number>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
		<!-- 选择发起指定人员对话框 -->
		<SysCompositeSelector
			ref="selectorRef"
			title="请选择指定人员"
			:selectorTypes="[SelectorTypeEnum.USER, SelectorTypeEnum.ORG, SelectorTypeEnum.POSITION]"
			:selectUserTypes="[SelectorUserTypeEnum.ORG, SelectorUserTypeEnum.POSITION]"
			@select="handleTargetInitiatorsSelected"
		>
		</SysCompositeSelector>
	</el-card>
</template>

<script setup lang="ts">
import { ref, defineProps, onMounted, reactive, computed } from "vue";
import { WorkflowDefType, WorkflowDef, getWorkflowDefTypeTreeApi, editWorkflowHisDeployApi } from "@/api/modules/workflow/def";
import * as eleValidate from "@/utils/eleValidate";
import { FormInstance, ElMessage } from "element-plus";
import { Delete, Pointer } from "@element-plus/icons-vue";
import SysCompositeSelector from "@/components/Selector/SysCompositeSelector.vue";
import { SelectorTypeEnum, SelectorUserTypeEnum } from "@/components/Selector/interface";

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
	defSort: [{ required: true, message: "请输入排序" }],
	initiatorScope: [{ required: true, message: "请选择发起范围" }],
	targetInitiators: [
		{
			type: "array",
			validator: (rule: any, value: any) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.initiatorScope == 1) {
						if (value == undefined || !value.length) {
							reject("请选择指定人员");
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
	processLimittype: [{ required: true, message: "请选择时限限制" }],
	processLimitvalue: [
		{
			type: "number",
			validator: (rule: any, value: any) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.processLimittype != 0) {
						if (!value) {
							reject("请输入" + computedProcessLimitTypeName.value);
						} else {
							resolve("");
						}
					} else {
						resolve("");
					}
				});
			}
		}
	]
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

// 时限限制类别名称
const computedProcessLimitTypeName = computed(() => (formProps.value.rowData.processLimittype == 3 ? "小时数" : "天数"));

// 发起范围指定人员选择器
const selectorRef = ref<InstanceType<typeof SysCompositeSelector> | null>(null);
const openInitiatorSelector = () => {
	selectorRef.value?.acceptParams({
		selected: formProps.value.rowData.targetInitiators?.map(item => {
			return {
				type: item.referRestype,
				value: item.referResid,
				label: item.referResname
			};
		})
	});
};
// 发起范围指定人员选择回调
const handleTargetInitiatorsSelected = (datas: { [key: string]: any }[]) => {
	formProps.value.rowData.targetInitiators = datas.map(item => {
		return {
			referRestype: item.type,
			referResid: item.value,
			referResname: item.label
		};
	});
};

onMounted(async () => {
	const { data } = await getWorkflowDefTypeTreeApi();
	handleDefTypeTreeCheckDisable(data);
	// 默认展开一级的树节点
	data?.forEach((item: { [key: string]: any }) => {
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

<style scoped lang="scss">
.selector-box {
	width: 100%;
	height: 95px;
	padding: 5px 5px 0;
	border: 1px solid #dcdfe6;
	border-radius: 4px;
	.selector-box-top {
		height: 65px;
		overflow-y: auto;
		.el-scrollbar__wrap {
			overflow: hidden;
		}
		.el-tag {
			margin-right: 8px;
			margin-bottom: 3px;
		}
	}
	.selector-box-bottom {
		height: 20px;
		margin-top: 5px;
		line-height: 20px;
		text-align: right;
		.selector-btn {
			cursor: pointer;
		}
		.selector-btn:nth-child(n + 1) {
			margin-left: 15px;
		}
	}
}
</style>
