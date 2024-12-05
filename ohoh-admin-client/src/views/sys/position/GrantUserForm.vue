<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="550" title="给岗位授权用户">
		<el-form ref="formRef" label-suffix=" :" :rules="formRules" :model="formProps.rowData" label-width="60">
			<el-form-item label="岗位" prop="positionId">
				<el-tree-select
					v-model="formProps.rowData.positionId"
					:data="positionTreeSelectDatas"
					:props="{ label: 'name', disabled: 'disabled' }"
					:default-expanded-keys="positionTreeSelectDefaultExpandKeys"
					node-key="id"
					check-strictly
					filterable
					style="width: 100%"
					@change="handlePositionChange"
				>
					<template #default="{ node, data }">
						<el-icon v-if="data.children"><FolderOpened v-if="node.expanded" /><Folder v-else /></el-icon>
						<el-icon v-else><Document /></el-icon>
						<span>{{ node.label }}</span>
					</template>
				</el-tree-select>
			</el-form-item>
			<el-form-item label="用户" prop="positionUserList">
				<div class="selector-box">
					<div class="selector-box-top">
						<el-tooltip
							:content="postionUser.orgName"
							effect="light"
							placement="top"
							v-for="(postionUser, index) in formProps.rowData.positionUserList"
							:key="postionUser.propId"
						>
							<el-tag plain closable @close="handleDeletePositionUser(index)">{{ postionUser.userName }}</el-tag>
						</el-tooltip>
					</div>
					<div class="selector-box-bottom">
						<span class="selector-btn" @click="handleClearPositionUser"
							><el-icon><Delete /></el-icon>清空</span
						>
						<span class="selector-btn" @click="handleAddPositionUser"
							><el-icon><User /></el-icon>选择</span
						>
					</div>
				</div>
			</el-form-item>
		</el-form>
		<template #footer>
			<el-button @click="formVisible = false">取消</el-button>
			<el-button type="primary" @click="handleSubmit">确定</el-button>
		</template>
	</el-drawer>
	<!-- 授权岗位用户对话框 -->
	<SysCompositeSelector
		ref="grantPositionUserRef"
		title="给岗位授权用户"
		:notEmpty="false"
		:selectorTypes="[SelectorTypeEnum.USER]"
		:selectUserTypes="[SelectorUserTypeEnum.ORG]"
		@select="handleGrantPositionUserSelected"
	></SysCompositeSelector>
</template>

<script setup lang="ts" name="SysPositionGrantUserForm">
import { reactive, ref } from "vue";
import { User, Delete } from "@element-plus/icons-vue";
import {
	SysPosition,
	getAllSysPositionTreeApi,
	listSysPositionUserApi,
	grantUserSysPositionApi
} from "@/api/modules/sys/position";
import { ElMessage, FormInstance } from "element-plus";
import { SelectorTypeEnum, SelectorUserTypeEnum } from "@/components/Selector/interface";
import SysCompositeSelector from "@/components/Selector/SysCompositeSelector.vue";

// 组件参数定义
interface FormProps {
	[key: string]: any;
	rowData: {
		positionId: string;
		positionUserList?: SysPosition.PositionUserProp[];
	};
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	rowData: {
		positionId: "",
		positionUserList: []
	}
});
const positionTreeSelectDatas = ref<SysPosition.TreeNode[]>([]);
const positionTreeSelectDefaultExpandKeys = ref<string[]>([]);

const formRules = reactive({
	positionId: [{ required: true, message: "请选择岗位" }],
	positionUserList: [{ required: true, message: "请选择用户" }]
});

// 接收父组件传过来的参数
const acceptParams = async (params: FormProps) => {
	// 表单数据赋值
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
	// 获取岗位树
	const { data: postionTreeData } = await getAllSysPositionTreeApi();
	postionTreeData.forEach((item: { [key: string]: any }) => {
		if (item.parentId === "0") {
			positionTreeSelectDefaultExpandKeys.value.push(item.positionId);
		}
	});
	positionTreeSelectDatas.value = postionTreeData;
	// 获取岗位的用户列表
	if (formProps.value.rowData.positionId) {
		await handlePositionChange(formProps.value.rowData.positionId);
	}

	formVisible.value = true;
};

// 岗位下拉框变更
const handlePositionChange = async (val: string) => {
	if (val) {
		({ data: formProps.value.rowData.positionUserList } = await listSysPositionUserApi({ id: val }));
	} else {
		formProps.value.rowData.positionUserList = [];
	}
};

// 删除岗位用户
const handleDeletePositionUser = (index: number) => {
	formProps.value.rowData.positionUserList.splice(index, 1);
};

// 清空岗位用户
const handleClearPositionUser = () => {
	formProps.value.rowData.positionUserList = [];
};

// 新增岗位用户
const grantPositionUserRef = ref<InstanceType<typeof SysCompositeSelector> | null>(null);
const handleAddPositionUser = () => {
	const selectedUserData =
		formProps.value.rowData.positionUserList?.map(d => {
			return {
				value: d.orgId + "#" + d.userId,
				label: d.userName,
				type: SelectorTypeEnum.USER,
				parentLabel: d.orgName,
				parentValue: d.orgId,
				extendValue: d.propId
			};
		}) || [];
	grantPositionUserRef.value?.acceptParams({ selected: selectedUserData });
};
const handleGrantPositionUserSelected = (datas: { [key: string]: any }[]) => {
	formProps.value.rowData.positionUserList = datas.map(d => {
		return {
			orgId: d.parentValue,
			orgName: d.parentLabel,
			propId: d.extendValue,
			userId: d.value,
			userName: d.label
		};
	});
	formRef.value?.validateField("positionUserList");
};

// 提交数据
const formRef = ref<FormInstance>();
const handleSubmit = () => {
	formRef.value!.validate(async valid => {
		if (!valid) return;
		try {
			const { msg } = await grantUserSysPositionApi({
				positionId: formProps.value.rowData.positionId,
				propIds: formProps.value.rowData.positionUserList.map(d => d.propId)
			});
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
.selector-box {
	width: 100%;
	height: 390px;
	padding: 5px 5px 0;
	border: 1px solid #dcdfe6;
	border-radius: 4px;
	.selector-box-top {
		height: 360px;
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
