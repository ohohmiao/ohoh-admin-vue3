<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="700" :title="`${formProps.title}系统用户`">
		<el-form
			ref="formRef"
			label-position="top"
			label-suffix=" :"
			:rules="rules"
			:disabled="formProps.isView"
			:model="formProps.rowData"
			:hide-required-asterisk="formProps.isView"
		>
			<el-row :gutter="16">
				<el-col :span="12">
					<el-form-item label="姓名" prop="userName">
						<el-input v-model="formProps.rowData.userName" placeholder="请输入姓名" maxlength="30" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="账号" prop="userAccount">
						<el-input v-model="formProps.rowData.userAccount" placeholder="请输入账号" maxlength="30" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="性别" prop="userGender">
						<el-radio-group v-model="formProps.rowData.userGender">
							<el-radio-button :value="1">男</el-radio-button>
							<el-radio-button :value="0">女</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="手机号码" prop="userMobile">
						<el-input v-model="formProps.rowData.userMobile" placeholder="请输入手机号码" maxlength="11" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="电子邮箱" prop="userEmail">
						<el-input v-model="formProps.rowData.userEmail" placeholder="请输入电子邮箱" maxlength="100" clearable></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="状态" prop="userStatus">
						<el-radio-group v-model="formProps.rowData.userStatus">
							<el-radio-button :value="1">启用</el-radio-button>
							<el-radio-button :value="0">禁用</el-radio-button>
						</el-radio-group>
					</el-form-item>
				</el-col>
			</el-row>
			<el-form-item label="所在机构" prop="orgList" class="addchild-table">
				<el-button type="primary" :icon="CirclePlus" class="addchild-btn" @click="handleAddUserOrg" v-if="!formProps.isView"
					>新增</el-button
				>
				<el-row :gutter="10" class="addchild-table-header">
					<el-col :span="5" class="addchild-table-header-col">机构</el-col>
					<el-col :span="5" class="addchild-table-header-col">岗位</el-col>
					<el-col :span="5" class="addchild-table-header-col">是否主机构</el-col>
					<el-col :span="5" class="addchild-table-header-col">在机构内用户排序</el-col>
					<el-col :span="4" class="addchild-table-header-col" v-if="!formProps.isView">操作</el-col>
				</el-row>
				<div v-for="(thizOrg, index) in formProps.rowData.orgList" class="addchild-table-content" :key="thizOrg.propRecordid">
					<el-row :gutter="10">
						<el-col :span="5">
							<el-form-item :prop="'formProps.rowData.orgList.' + index + '.propRecordid'">
								<el-tree-select
									v-model="thizOrg.propRecordid"
									:data="orgTreeSelectDatas"
									:props="{ label: 'name', disabled: 'disabled' }"
									node-key="id"
									check-strictly
									filterable
									v-if="isAuthDataScope(thizOrg.propRecordid)"
								>
									<template #default="{ node, data }">
										<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
										<el-icon v-else><Document /></el-icon>
										<span>{{ node.label }}</span>
									</template>
								</el-tree-select>
								<el-input v-model="thizOrg.propRecordname" readonly v-else></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item :prop="'formProps.rowData.orgList.' + index + '.propExtendid'">
								<el-tree-select
									v-model="thizOrg.propExtendid"
									:data="positionTreeSelectDatas"
									:props="{ label: 'name', disabled: 'disabled' }"
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
						<el-col :span="5">
							<el-form-item :prop="'formProps.rowData.orgList.' + index + '.defaultFlag'">
								<el-select v-model="thizOrg.defaultFlag" :disabled="!isAuthDataScope(thizOrg.propRecordid)">
									<el-option :value="1" label="是"></el-option>
									<el-option :value="0" label="否"></el-option>
								</el-select>
							</el-form-item>
						</el-col>
						<el-col :span="5">
							<el-form-item :prop="'formProps.rowData.orgList.' + index + '.propSort'">
								<el-input-number
									v-model="thizOrg.propSort"
									:min="1"
									:max="9999"
									controls-position="right"
									style="width: 100%"
									:disabled="!isAuthDataScope(thizOrg.propRecordid)"
								></el-input-number>
							</el-form-item>
						</el-col>
						<el-col :span="4" v-if="!formProps.isView">
							<el-button
								type="danger"
								size="small"
								plain
								@click="handleDeleteUserOrg(index)"
								:disabled="!isAuthDataScope(thizOrg.propRecordid)"
								>删除</el-button
							>
						</el-col>
					</el-row>
				</div>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" v-show="!formProps.isView" @click="handleSubmit">确定</el-button>
		</template>
	</el-drawer>
</template>

<script setup lang="ts" name="SysUserForm">
import { reactive, ref } from "vue";
import { CirclePlus } from "@element-plus/icons-vue";
import { SysUser } from "@/api/modules/sys/user";
import { FormInstance, ElMessage } from "element-plus";
import * as eleValidate from "@/utils/eleValidate";
import { getAuthSysOrgTreeApi, SysOrg } from "@/api/modules/sys/org";
import { getAllSysPositionTreeApi, SysPosition } from "@/api/modules/sys/position";
import { GlobalStore } from "@/stores";

const globalStore = GlobalStore();
const loginUserInfo = globalStore.userInfo;
const grantedDataScopes = loginUserInfo.grantedDataScopes;

const rules = reactive({
	userName: [{ required: true, message: "请输入姓名" }],
	userAccount: [
		{ required: true, message: "请输入账号" },
		{ validator: eleValidate.checkLetterOrNumOrUnderline, message: "仅限输入字母/数字/下划线" }
	],
	userGender: [{ required: true, message: "请选择性别" }],
	userMobile: [{ validator: eleValidate.checkMobileNum, message: "手机号码格式有误" }],
	userEmail: [{ validator: eleValidate.checkEmail, message: "电子邮箱格式有误" }],
	orgList: [
		{
			required: true,
			type: "array",
			message: "请填写至少一条所在机构信息",
			defaultField: {
				type: "object",
				fields: {
					propRecordid: [
						{
							type: "string",
							required: true,
							trigger: "change",
							message: "请选择机构"
						}
					],
					defaultFlag: [
						{
							type: "number",
							required: true,
							trigger: "change",
							message: "请选择是否主机构"
						}
					],
					propSort: [
						{
							type: "number",
							required: true,
							trigger: "blur",
							message: "请输入在机构内用户排序"
						}
					]
				}
			}
		}
	]
});

interface FormProps {
	[key: string]: any;
	title: string;
	isView: boolean;
	rowData: Partial<SysUser.Form>;
	api?: (params: any) => Promise<any>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = reactive<FormProps>({
	title: "",
	isView: false,
	rowData: {}
});
let orgTreeSelectDatas = reactive<SysOrg.TreeNode[]>([]);
let positionTreeSelectDatas = reactive<SysPosition.TreeNode[]>([]);

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	Object.keys(params).forEach(key => {
		if (key === "rowData") {
			// 存在默认赋值的rowData，不覆盖
			if (params[key] && Object.keys(params[key]).length) {
				formProps[key] = params[key];
			} else {
				formProps[key] = Object.assign({}, params[key], {
					// 表单字段默认值
					userStatus: 1
				});
			}
		} else {
			formProps[key] = params[key];
		}
	});

	// 获取授权的资源树形选择数据
	const { data: orgTreeData } = await getAuthSysOrgTreeApi();
	handleOrgTreeCheckDisable(orgTreeData);
	orgTreeSelectDatas = orgTreeData;

	// 获取岗位树形数据
	({ data: positionTreeSelectDatas } = await getAllSysPositionTreeApi());

	formVisible.value = true;
};

const isAuthDataScope = (orgId: string) => {
	if (!orgId) return true;
	return loginUserInfo.superAdmin || (grantedDataScopes && grantedDataScopes.indexOf(orgId) !== -1);
};
// 禁用未授权的机构树节点
const handleOrgTreeCheckDisable = (params: any) => {
	if (!params) return;
	params.forEach((param: { id: any; disabled: boolean; children: any }) => {
		if (!isAuthDataScope(param.id)) {
			param.disabled = true;
		}
		handleOrgTreeCheckDisable(param.children);
	});
};

// 新增所属组织机构
const handleAddUserOrg = () => {
	if (!formProps.rowData.orgList) {
		formProps.rowData.orgList = [];
	}
	if (formProps.rowData.orgList.length) {
		formRef.value
			?.validateField("orgList")
			.then(() => {
				formProps.rowData.orgList!.push({
					propRecordid: ""
				});
			})
			.catch(() => {});
	} else {
		formProps.rowData.orgList!.push({
			propRecordid: ""
		});
	}
	formRef.value?.clearValidate();
};

// 删除所属组织机构
const handleDeleteUserOrg = (index: number) => {
	formProps.rowData.orgList!.splice(index, 1);
	formRef.value?.clearValidate();
};

// 根据节点id 查找树节点
const getTreeNodeByNodeId = (treeDatas: { [key: string]: any }[], id: string) => {
	let array: { [key: string]: any }[] = [];
	array = array.concat(treeDatas);
	while (array.length) {
		let temp = array.shift();
		if (temp?.children) {
			array = array.concat(temp.children);
		}
		if (temp!.id === id) {
			return temp;
		}
	}
};

// 提交数据（新增/编辑）
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			// 验证所属组织机构参数
			const thizOrgList = formProps.rowData.orgList;
			const mainOrgList = thizOrgList?.filter(org => org.defaultFlag === 1);
			if (mainOrgList!.length != 1) {
				ElMessage.warning({ message: "请至少设定一个主机构，且仅允许设置一个" });
				return;
			}
			// 验证组织和岗位关系：岗位级别>=组织级别
			let thizLegalOrgPositionRel = true;
			thizOrgList?.forEach(org => {
				if (!org.propExtendid) return true;
				let thizOrgLevel = getTreeNodeByNodeId(orgTreeSelectDatas, org.propRecordid)!.orgLevel;
				let thizPositionLevel = getTreeNodeByNodeId(positionTreeSelectDatas, org.propExtendid)!.positionLevel;
				if (thizOrgLevel > thizPositionLevel) {
					thizLegalOrgPositionRel = false;
				}
			});
			if (!thizLegalOrgPositionRel) {
				ElMessage.warning({ message: "存在非法的组织岗位配置，岗位级别必须大于或等于组织级别" });
				return;
			}
			thizOrgList?.forEach(org => {
				org.propTablename = "SYS_ORG";
			});
			// 调用保存或更新接口
			const { msg } = await formProps.api!(formProps.rowData);
			ElMessage.success({ message: msg });
			formProps.getTableList!();
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
