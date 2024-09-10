<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}组织机构`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="上级组织机构" prop="parentId">
				<el-tree-select
					v-model="formProps.rowData.parentId"
					:data="authOrgTreeSelectDatas"
					:props="{ label: 'name', disabled: 'disabled' }"
					:default-expanded-keys="authOrgTreeSelectDefaultExpandKeys"
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
			<el-form-item label="名称" prop="orgName">
				<el-input v-model="formProps.rowData.orgName" placeholder="请输入名称" maxlength="60" clearable></el-input>
			</el-form-item>
			<el-form-item label="简称" prop="orgShortname">
				<el-input v-model="formProps.rowData.orgShortname" placeholder="请输入简称" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="编码" prop="orgCode">
				<template #label>
					<el-tooltip content="注：仅限输入字母！" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					编码：
				</template>
				<el-input v-model="formProps.rowData.orgCode" placeholder="请输入编码" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="组织级别" prop="orgLevel">
				<template #label>
					<el-tooltip content="以数值定义组织级别，数值越小级别越高。" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					组织级别：
				</template>
				<el-input-number
					v-model="formProps.rowData.orgLevel"
					:min="1"
					:max="9999"
					placeholder="请输入组织级别"
					controls-position="right"
					style="width: 100%"
				></el-input-number>
			</el-form-item>
			<el-form-item label="排序" prop="treeSort" v-if="formProps.rowData.orgId">
				<el-input-number
					v-model="formProps.rowData.treeSort"
					:min="1"
					:max="9999"
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

<script setup lang="ts" name="SysOrgForm">
import { reactive, ref } from "vue";
import { SysOrg, getAuthSysOrgTreeApi } from "@/api/modules/sys/org";
import { FormInstance, ElMessage } from "element-plus";
import * as eleValidate from "@/utils/eleValidate";
import { GlobalStore } from "@/stores";

const globalStore = GlobalStore();
const loginUserInfo = globalStore.userInfo;
const grantedDataScopes = loginUserInfo.grantedDataScopes;

const rules = reactive({
	parentId: [{ required: true, message: "请选择上级组织机构" }],
	orgName: [{ required: true, message: "请输入名称" }],
	orgCode: [{ validator: eleValidate.checkLetter, message: "仅限输入字母" }],
	orgLevel: [{ required: true, message: "请输入组织级别" }],
	treeSort: [{ required: true, message: "请输入排序" }]
});

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<SysOrg.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});
const authOrgTreeSelectDatas = ref<SysOrg.TreeNode[]>([]);
const authOrgTreeSelectDefaultExpandKeys = ref<string[]>([]);

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
	// 删除多余字段
	delete formProps.value.rowData.children;

	// 获取上级资源树形选择数据
	const { data } = await getAuthSysOrgTreeApi();
	// 默认展开一级的树节点
	data.forEach((item: { [key: string]: any }) => {
		if (item.parentId === "0") {
			authOrgTreeSelectDefaultExpandKeys.value.push(item.orgId);
		}
	});
	authOrgTreeSelectDatas.value = [
		{
			id: "0",
			name: "顶级",
			children: data
		}
	];
	handleOrgTreeCheckDisable(authOrgTreeSelectDatas.value);

	formVisible.value = true;
};

// 禁用未授权的机构树节点
const handleOrgTreeCheckDisable = (params: SysOrg.TreeNode[]) => {
	if (!params) return;
	params.forEach((param: SysOrg.TreeNode) => {
		if (!(loginUserInfo.superAdmin || (grantedDataScopes && grantedDataScopes.indexOf(param.id) !== -1))) {
			param.disabled = true;
		} else {
			const thizParamTreePath = param.treePath;
			if (thizParamTreePath && formProps.value.rowData.orgId) {
				const thizParamTreePaths = thizParamTreePath.split(".");
				if (thizParamTreePaths.includes(formProps.value.rowData.orgId)) {
					param.disabled = true;
				}
			}
		}
		handleOrgTreeCheckDisable(param.children || []);
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

defineExpose({
	acceptParams
});
</script>
