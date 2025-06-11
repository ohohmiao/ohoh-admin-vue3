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
			label-width="140"
			:rules="formRules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="下一环节" prop="nextnodeId">
				<el-select v-model="formProps.rowData.nextnodeId" @change="handleNextNodeSelectChange">
					<el-option v-for="item in props.nextTaskNodeList" :key="item.value" :label="item.label" :value="item.value"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="办理人类型" prop="handlerType">
				<el-select v-model="formProps.rowData.handlerType">
					<el-option label="指定人员" :value="0"></el-option>
					<el-option label="指定接口" :value="1"></el-option>
					<el-option label="自行选择" :value="2"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="指定人员" prop="targetReferResList" v-if="formProps.rowData.handlerType == 0" required>
				<div class="selector-box">
					<div class="selector-box-top">
						<el-tag
							v-for="(item, index) in formProps.rowData.targetReferResList"
							:key="index"
							:type="['primary', 'success', 'info', 'warning'][[SelectorTypeEnum.USER, SelectorTypeEnum.ORG, SelectorTypeEnum.POSITION, SelectorTypeEnum.CONTACT].indexOf(formProps.rowData.targetReferResList![index].referRestype)]"
							plain
							closable
							@close="formProps.rowData.targetReferResList?.splice(index, 1)"
							>{{ item.referResname }}</el-tag
						>
					</div>
					<div class="selector-box-bottom">
						<span
							class="selector-btn"
							@click="formProps.rowData.targetReferResList?.splice(0, formProps.rowData.targetReferResList.length)"
							><el-icon><Delete /></el-icon>清空</span
						>
						<span class="selector-btn" @click="openTargetReferResSelector"
							><el-icon><Pointer /></el-icon>选择</span
						>
					</div>
				</div>
			</el-form-item>
			<el-form-item label="指定接口" prop="interfaceCode" v-if="formProps.rowData.handlerType == 1" required>
				<el-select v-model="formProps.rowData.interfaceCode" filterable>
					<el-option v-for="item in interfaceDicDatas" :key="item.value" :label="item.label" :value="item.value"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item
				label="过滤规则"
				prop="filterRule"
				v-if="formProps.rowData.handlerType == 0 || formProps.rowData.handlerType == 1"
			>
				<el-select v-model="formProps.rowData.filterRule" filterable clearable>
					<el-option v-for="item in filterRuleDicDatas" :key="item.value" :label="item.label" :value="item.value"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="多人审核方式" prop="multiHandletype">
				<el-radio-group v-model="formProps.rowData.multiHandletype">
					<el-radio-button label="并审" :value="0"></el-radio-button>
					<el-radio-button label="串审" :value="1"></el-radio-button>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="允许重选办理人" prop="reselectPermit" v-if="formProps.rowData.handlerType != 2">
				<el-radio-group v-model="formProps.rowData.reselectPermit">
					<el-radio-button label="禁止" :value="0"></el-radio-button>
					<el-radio-button label="允许" :value="1"></el-radio-button>
				</el-radio-group>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-dialog>
	<!-- 选择指定人员对话框 -->
	<SysCompositeSelector
		ref="selectorRef"
		title="请选择指定人员"
		:selectorTypes="[SelectorTypeEnum.USER, SelectorTypeEnum.ORG, SelectorTypeEnum.POSITION]"
		:selectUserTypes="[SelectorUserTypeEnum.ORG, SelectorUserTypeEnum.POSITION]"
		@select="handleTargetReferResSelected"
	>
	</SysCompositeSelector>
</template>

<script setup lang="ts">
import { defineProps, reactive, ref, withDefaults, onMounted } from "vue";
import { WorkflowHandler } from "@/api/modules/workflow/handler";
import { ElMessage, FormInstance } from "element-plus";
import { Delete, Pointer } from "@element-plus/icons-vue";
import SysCompositeSelector from "@/components/Selector/SysCompositeSelector.vue";
import { SelectorTypeEnum, SelectorUserTypeEnum } from "@/components/Selector/interface";
import { getSysDicSelectApi } from "@/api/modules/sys/dic";

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
	nextnodeId: [{ required: true, message: "请选择下一环节" }],
	handlerType: [{ required: true, message: "请选择办理人类型" }],
	targetReferResList: [
		{
			type: "array",
			validator: (rule: any, value: any) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.handlerType == 0) {
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
	interfaceCode: [
		{
			type: "string",
			validator: (rule: any, value: string) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.handlerType == 1) {
						if (!value) {
							reject("请选择指定接口");
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
	multiHandletype: [{ required: true, message: "请选择多人审核方式" }],
	reselectPermit: [
		{
			type: "number",
			validator: (rule: any, value: number) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.handlerType == 0 || formProps.value.rowData.handlerType == 1) {
						if (value == null) {
							reject("请选择是否允许重选办理人");
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

// 下一环节下拉框变更
const handleNextNodeSelectChange = (val: string) => {
	formProps.value.rowData.nextnodeName = props.nextTaskNodeList.find(item => item.value === val)?.label;
};

// 指定接口字典
const interfaceDicDatas = ref<{ label: string; value: string }[]>([]);
// 过滤规则字典
const filterRuleDicDatas = ref<{ label: string; value: string }[]>([]);

onMounted(async () => {
	const { data: interfaceDic } = await getSysDicSelectApi({ dictypeCode: "FlowHandlerConfigInterfaceList" });
	interfaceDicDatas.value = interfaceDic;
	const { data: ruleDic } = await getSysDicSelectApi({ dictypeCode: "FlowHandlerConfigFilterRule" });
	filterRuleDicDatas.value = ruleDic;
});

// 打开指定人员选择对话框
const selectorRef = ref<InstanceType<typeof SysCompositeSelector> | null>(null);
const openTargetReferResSelector = () => {
	selectorRef.value?.acceptParams({
		selected: formProps.value.rowData.targetReferResList?.map(item => {
			return {
				type: item.referRestype,
				value: item.referResid,
				label: item.referResname
			};
		})
	});
};
// 指定人员选择回调
const handleTargetReferResSelected = (datas: { [key: string]: any }[]) => {
	formProps.value.rowData.targetReferResList = datas.map(item => {
		return {
			referRestype: item.type,
			referResid: item.value,
			referResname: item.label
		};
	});
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
					handlerType: 0,
					multiHandletype: 0,
					reselectPermit: 0
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
