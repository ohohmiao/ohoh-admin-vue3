<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="700" :title="`${formProps.title}系统资源`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-tabs v-model="activeTab">
				<el-tab-pane label="基本信息" name="baseTab">
					<el-row :gutter="16">
						<el-col :span="12">
							<el-form-item label="上级资源" prop="parentId">
								<el-tree-select
									v-model="formProps.rowData.parentId"
									:data="resTreeSelectDatas"
									:default-expanded-keys="resTreeDefaultExpandKeys"
									:props="{ label: 'name' }"
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
							<el-form-item label="资源类别" prop="resType">
								<el-radio-group v-model="formProps.rowData.resType">
									<el-radio-button :value="0">菜单</el-radio-button>
									<el-radio-button :value="1">按钮</el-radio-button>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="资源名称" prop="resName">
								<el-input v-model="formProps.rowData.resName" placeholder="请输入资源名称" maxlength="30" clearable></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="12">
							<el-form-item label="资源编码" prop="resCode">
								<template #label>
									<el-tooltip content="注：仅限输入字母！" placement="top">
										<el-icon><QuestionFilled /></el-icon>
									</el-tooltip>
									资源编码：
								</template>
								<el-input v-model="formProps.rowData.resCode" placeholder="请输入资源编码" maxlength="30" clearable></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="12" v-if="formProps.rowData.resType === 0">
							<el-form-item label="菜单图标" prop="resIcon">
								<SelectIcon v-model:iconValue="formProps.rowData.resIcon" placeholder="请选择菜单图标"></SelectIcon>
							</el-form-item>
						</el-col>
						<el-col :span="12" v-if="formProps.rowData.resType === 0">
							<el-form-item label="菜单类别" prop="menuType">
								<el-radio-group v-model="formProps.rowData.menuType">
									<el-radio-button :value="0">目录</el-radio-button>
									<el-radio-button :value="1">组件</el-radio-button>
									<el-radio-button :value="2">外链</el-radio-button>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col
							:span="12"
							v-if="formProps.rowData.resType === 0 && (formProps.rowData.menuType === 1 || formProps.rowData.menuType === 2)"
						>
							<el-form-item label="是否隐藏" prop="hideFlag">
								<el-radio-group v-model="formProps.rowData.hideFlag">
									<el-radio-button :value="0">否</el-radio-button>
									<el-radio-button :value="1">是</el-radio-button>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col :span="12" v-if="formProps.rowData.resType === 0 && formProps.rowData.menuType === 1">
							<el-form-item label="是否全屏显示" prop="fullscreenFlag">
								<el-radio-group v-model="formProps.rowData.fullscreenFlag">
									<el-radio-button :value="0">否</el-radio-button>
									<el-radio-button :value="1">是</el-radio-button>
								</el-radio-group>
							</el-form-item>
						</el-col>
						<el-col :span="12" v-if="formProps.rowData.resType === 0 && formProps.rowData.menuType === 1">
							<el-form-item label="组件路径" prop="resPath" required>
								<template #label>
									<el-tooltip content="注：前面必须有反斜杠！" placement="top">
										<el-icon><QuestionFilled /></el-icon>
									</el-tooltip>
									组件路径：
								</template>
								<el-input v-model="formProps.rowData.resPath" placeholder="请输入组件路径" maxlength="100" clearable>
									<template #prepend>src/views</template>
									<template #append>.vue</template>
								</el-input>
							</el-form-item>
						</el-col>
						<el-col :span="12" v-if="formProps.rowData.resType === 0 && formProps.rowData.menuType === 2">
							<el-form-item label="外链url" prop="resPath" required>
								<template #label>
									<el-tooltip content="注：http(s)://开头！" placement="top">
										<el-icon><QuestionFilled /></el-icon>
									</el-tooltip>
									外链url：
								</template>
								<el-input v-model="formProps.rowData.resPath" placeholder="请输入外链url" maxlength="100" clearable></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="12" v-if="formProps.rowData.resId">
							<el-form-item label="排序" prop="treeSort">
								<el-input-number
									v-model="formProps.rowData.treeSort"
									:min="1"
									:max="9999"
									placeholder="请输入排序"
									controls-position="right"
									style="width: 100%"
								></el-input-number>
							</el-form-item>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="资源url" name="urlTab">
					<el-form-item label="" prop="urlList" class="addchild-table">
						<el-button type="primary" :icon="CirclePlus" class="addchild-btn" @click="handleAddResUrl" v-if="!formProps.isView"
							>新增</el-button
						>
						<el-row :gutter="10" class="addchild-table-header">
							<el-col :span="20" class="addchild-table-header-col">url</el-col>
							<el-col :span="4" class="addchild-table-header-col" v-if="!formProps.isView">操作</el-col>
						</el-row>
						<div v-for="(thizUrl, thizUrlIndex) in formProps.rowData.urlList" class="addchild-table-content" :key="thizUrlIndex">
							<el-row :gutter="10">
								<el-col :span="20">
									<el-form-item :prop="'formProps.rowData.urlList[' + thizUrlIndex + ']'">
										<el-select v-model="formProps.rowData.urlList[thizUrlIndex]" filterable clearable>
											<el-option
												v-for="permissionUrl in permissionUrlList"
												:label="permissionUrl"
												:value="permissionUrl"
												:key="permissionUrl"
											></el-option>
										</el-select>
									</el-form-item>
								</el-col>
								<el-col :span="4" v-if="!formProps.isView">
									<el-button type="danger" size="small" plain @click="handleDeleteResUrl(thizUrlIndex)">删除</el-button>
								</el-col>
							</el-row>
						</div>
					</el-form-item>
				</el-tab-pane>
			</el-tabs>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-drawer>
</template>

<script setup lang="ts" name="SysResForm">
import { reactive, ref } from "vue";
import { CirclePlus } from "@element-plus/icons-vue";
import { SysRes, getSysResTreeApi, listSysResUrlByResIdApi, listSaPcPermissionUrlApi } from "@/api/modules/sys/res";
import { FormInstance, ElMessage } from "element-plus";
import * as eleValidate from "@/utils/eleValidate";
import SelectIcon from "@/components/SelectIcon/index.vue";

const activeTab = ref("baseTab");

const rules = reactive({
	parentId: [{ required: true, message: "请选择上级资源" }],
	resType: [{ required: true, message: "请选择资源类别" }],
	resName: [{ required: true, message: "请输入资源名称" }],
	resCode: [
		{ required: true, message: "请输入资源编码" },
		{ validator: eleValidate.checkLetter, message: "仅限输入字母" }
	],
	menuType: [{ required: true, message: "请选择菜单类别" }],
	resIcon: [{ required: true, message: "请选择菜单图标" }],
	hideFlag: [{ required: true, message: "请选择是否隐藏" }],
	fullscreenFlag: [{ required: true, message: "请选择是否全屏显示" }],
	resPath: [
		{
			type: "string",
			validator: (rule: any, value: string) => {
				return new Promise((resolve, reject) => {
					if (formProps.value.rowData.resType == 0) {
						if (formProps.value.rowData.menuType == 1) {
							if (!value) {
								reject("请输入组件路径");
							} else {
								resolve("");
							}
						} else if (formProps.value.rowData.menuType == 2) {
							if (!value) {
								reject("请输入外链url");
							} else {
								if (!eleValidate.checkUrl(rule, value)) {
									reject("外链url格式有误");
								} else {
									resolve("");
								}
							}
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
	treeSort: [{ required: true, message: "请输入排序" }],
	urlList: [
		{
			required: false,
			type: "array",
			defaultField: [
				{
					required: true,
					type: "string",
					message: "请输入资源url"
				}
			]
		}
	]
});

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<SysRes.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});
const resTreeSelectDatas = ref<SysRes.TreeNode[]>([]);
// 默认展开的节点key
const resTreeDefaultExpandKeys = ref<string[]>([]);
// SaPcCheckPermission注解的权限url集合
const permissionUrlList = ref<string[]>([]);

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
					resType: 0,
					menuType: 1,
					hideFlag: 0,
					fullscreenFlag: 0
				});
			}
		} else {
			formProps.value[key] = params[key];
		}
	});
	// 删除多余字段
	delete formProps.value.rowData.children;

	// 获取上级资源树形选择数据
	const { data: resTreeResultData } = await getSysResTreeApi({});
	// 默认展开一级的树节点
	resTreeResultData.forEach((item: { [key: string]: any }) => {
		if (item.parentId === "0") {
			resTreeDefaultExpandKeys.value.push(item.resId);
		}
	});
	resTreeSelectDatas.value = [
		{
			id: "0",
			name: "顶级",
			children: resTreeResultData
		}
	];
	// 编辑情况，禁用当前编辑的节点及其孩子
	if (formProps.value.title == "编辑") {
		handleResTreeCheckDisable(resTreeSelectDatas.value);
	}
	// 查询资源url集合
	if (formProps.value.rowData.resId) {
		({ data: formProps.value.rowData.urlList } = await listSysResUrlByResIdApi({ id: formProps.value.rowData.resId }));
	}
	// 查询SaPcCheckPermission注解的权限url集合
	({ data: permissionUrlList.value } = await listSaPcPermissionUrlApi());

	// 基本信息面板
	activeTab.value = "baseTab";

	formVisible.value = true;
};

const handleResTreeCheckDisable = (params: SysRes.TreeNode[]) => {
	if (!params) return;
	params.forEach((param: SysRes.TreeNode) => {
		const thizParamTreePath = param.treePath;
		if (thizParamTreePath && formProps.value.rowData.resId) {
			const thizParamTreePaths = thizParamTreePath.split(".");
			if (thizParamTreePaths.includes(formProps.value.rowData.resId)) {
				param.disabled = true;
			}
		}
		handleResTreeCheckDisable(param.children || []);
	});
};

// 新增资源url
const handleAddResUrl = () => {
	if (!formProps.value.rowData.urlList) {
		formProps.value.rowData.urlList = [];
	}
	if (formProps.value.rowData.urlList.length) {
		formRef.value
			?.validateField("urlList")
			.then(() => {
				formProps.value.rowData.urlList!.push("");
			})
			.catch(() => {});
	} else {
		formProps.value.rowData.urlList.push("");
	}
};

// 删除资源url
const handleDeleteResUrl = (index: number) => {
	formProps.value.rowData.urlList?.splice(index, 1);
	formRef.value?.validateField("urlList");
};

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) {
			formRef.value
				?.validateField("urlList")
				.then(() => {
					activeTab.value = "baseTab";
				})
				.catch(() => {
					activeTab.value = "urlTab";
				});
			return;
		}
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
