<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="450" :title="`${formProps.title}系统角色`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-form-item label="所属组织" prop="orgId">
				<template #label>
					<el-tooltip content="注：仅管理员能创建全局类别的角色！" placement="top">
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					所属组织：
				</template>
				<el-tree-select
					v-model="formProps.rowData.orgId"
					:data="authOrgTreeSelectDatas"
					:default-expanded-keys="authOrgTreeSelectDefaultExpandKeys"
					:props="{ label: 'name' }"
					node-key="id"
					check-strictly
					filterable
					@node-click="delete formProps.rowData.datascopeType"
				>
					<template #default="{ node, data }">
						<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
						<el-icon v-else><Document /></el-icon>
						<span>{{ node.label }}</span>
					</template>
				</el-tree-select>
			</el-form-item>
			<el-form-item label="角色名称" prop="roleName">
				<el-input v-model="formProps.rowData.roleName" placeholder="请输入角色名称" maxlength="30" clearable></el-input>
			</el-form-item>
			<el-form-item label="是否公共角色" prop="publicroleFlag" v-if="computedIsTopLevelOrg">
				<template #label>
					<el-tooltip placement="top">
						<template #content>即：是否全部人员都默认拥有该角色的权限。<br />注：仅全局类别的角色能配置是否公共角色！</template>
						<el-icon><QuestionFilled /></el-icon>
					</el-tooltip>
					是否公共角色：
				</template>
				<el-radio-group v-model="formProps.rowData.publicroleFlag">
					<el-radio-button :label="0">否</el-radio-button>
					<el-radio-button :label="1">是</el-radio-button>
				</el-radio-group>
			</el-form-item>
			<el-form-item label="排序" prop="roleSort" v-if="formProps.rowData.roleId">
				<el-input-number
					v-model="formProps.rowData.roleSort"
					:min="1"
					:max="9999"
					placeholder="请输入排序"
					controls-position="right"
					style="width: 100%"
				></el-input-number>
			</el-form-item>
			<el-form-item label="备注" prop="roleRemark">
				<el-input
					v-model="formProps.rowData.roleRemark"
					placeholder="请输入备注"
					type="textarea"
					:autosize="{ minRows: 5, maxRows: 8 }"
					maxlength="100"
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

<script setup lang="ts" name="SysRoleForm">
import { reactive, ref, computed } from "vue";
import { getAuthSysOrgTreeApi, SysOrg } from "@/api/modules/sys/org";
import { SysRole } from "@/api/modules/sys/role";
import { FormInstance, ElMessage } from "element-plus";
import { GlobalStore } from "@/stores";

const globalStore = GlobalStore();
const loginUserInfo = globalStore.userInfo;
const grantedDataScopes = loginUserInfo.grantedDataScopes;

const rules = reactive({
	orgId: [{ required: true, message: "请选择所属组织机构" }],
	roleName: [{ required: true, message: "请输入角色名称" }],
	publicroleFlag: [{ required: true, message: "请选择是否公共角色" }],
	roleSort: [{ required: true, message: "请输入排序" }]
});

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<SysRole.Form>;
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
// 默认展开的节点key
let authOrgTreeSelectDefaultExpandKeys = ref<string[]>([]);

// 是否顶级机构
const computedIsTopLevelOrg = computed(() => {
	return formProps.value.rowData.orgId === "0";
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

	// 获取授权的资源树形选择数据
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
			name: "全局角色",
			children: data
		}
	];
	handleOrgTreeCheckDisable(authOrgTreeSelectDatas.value);

	formVisible.value = true;
};

// 禁用未授权的机构树节点
const handleOrgTreeCheckDisable = (params: any) => {
	if (!params) return;
	params.forEach((param: { id: any; disabled: boolean; children: any }) => {
		if (!(loginUserInfo.superAdmin || (grantedDataScopes && grantedDataScopes.indexOf(param.id) !== -1))) {
			param.disabled = true;
		}
		handleOrgTreeCheckDisable(param.children);
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
