<template>
	<div class="selector-dialog">
		<el-dialog v-model="selectorVisible" :title="selectorProps.title" @close="handleClose" top="5vh" destroy-on-close>
			<!-- 一级类别 -->
			<el-space :spacer="verticalSpacer" v-if="selectorProps.selectorTypes.length > 1">
				<template v-if="selectorProps.selectorTypes.indexOf(SelectorTypeEnum.USER) !== -1">
					<el-button
						v-if="curSelType === SelectorTypeEnum.USER"
						type="primary"
						size="small"
						@click="handleSelType(SelectorTypeEnum.USER)"
						>选人员</el-button
					>
					<el-button v-else link size="small" @click="handleSelType(SelectorTypeEnum.USER)">选人员</el-button>
				</template>
				<template v-if="selectorProps.selectorTypes.indexOf(SelectorTypeEnum.ORG) !== -1">
					<el-button
						v-if="curSelType === SelectorTypeEnum.ORG"
						type="primary"
						size="small"
						@click="handleSelType(SelectorTypeEnum.ORG)"
						>选部门</el-button
					>
					<el-button v-else link size="small" @click="handleSelType(SelectorTypeEnum.ORG)">选部门</el-button>
				</template>
				<template v-if="selectorProps.selectorTypes.indexOf(SelectorTypeEnum.POSITION) !== -1">
					<el-button
						v-if="curSelType === SelectorTypeEnum.POSITION"
						type="primary"
						size="small"
						@click="handleSelType(SelectorTypeEnum.POSITION)"
						>选岗位</el-button
					>
					<el-button v-else link size="small" @click="handleSelType(SelectorTypeEnum.POSITION)">选岗位</el-button>
				</template>
				<template v-if="selectorProps.selectorTypes.indexOf(SelectorTypeEnum.CONTACT) !== -1">
					<el-button
						v-if="curSelType === SelectorTypeEnum.CONTACT"
						type="primary"
						size="small"
						@click="handleSelType(SelectorTypeEnum.CONTACT)"
						>选联系组</el-button
					>
					<el-button v-else link size="small" @click="handleSelType(SelectorTypeEnum.CONTACT)">选联系组</el-button>
				</template>
			</el-space>
			<el-divider class="selector-tab-divider" v-if="selectorProps.selectorTypes.length > 1" />
			<!-- 二级类别 -->
			<el-space
				:spacer="verticalSpacer"
				v-show="curSelType === SelectorTypeEnum.USER && selectorProps.selectUserTypes.length > 1"
			>
				<template v-if="selectorProps.selectUserTypes.indexOf(SelectorUserTypeEnum.ORG) !== -1">
					<el-button
						v-if="curSelUserType === SelectorUserTypeEnum.ORG"
						type="primary"
						size="small"
						@click="handleSelUserType(SelectorUserTypeEnum.ORG)"
						>按部门</el-button
					>
					<el-button v-else link size="small" @click="handleSelUserType(SelectorUserTypeEnum.ORG)">按部门</el-button>
				</template>
				<template v-if="selectorProps.selectUserTypes.indexOf(SelectorUserTypeEnum.POSITION) !== -1">
					<el-button
						v-if="curSelUserType === SelectorUserTypeEnum.POSITION"
						type="primary"
						size="small"
						@click="handleSelUserType(SelectorUserTypeEnum.POSITION)"
						>按岗位</el-button
					>
					<el-button v-else link size="small" @click="handleSelUserType(SelectorUserTypeEnum.POSITION)">按岗位</el-button>
				</template>
				<template v-if="selectorProps.selectUserTypes.indexOf(SelectorUserTypeEnum.CONTACT) !== -1">
					<el-button
						v-if="curSelUserType === SelectorUserTypeEnum.CONTACT"
						type="primary"
						size="small"
						@click="handleSelUserType(SelectorUserTypeEnum.CONTACT)"
						>按联系组</el-button
					>
					<el-button v-else link size="small" @click="handleSelUserType(SelectorUserTypeEnum.CONTACT)">按联系组</el-button>
				</template>
			</el-space>
			<el-divider
				class="selector-tab-divider"
				v-show="curSelType === SelectorTypeEnum.USER && selectorProps.selectUserTypes.length > 1"
			/>
			<!-- 主操作区域 -->
			<el-row class="selector-box">
				<!-- 待选 -->
				<el-col :span="11" class="selector-operate-box">
					<el-input v-model="selectDataFilterText" placeholder="请输入关键字进行搜索" :prefix-icon="Search" clearable />
					<el-scrollbar :style="{ height: `calc(100% - 37px)` }">
						<!-- 选人员-按部门 -->
						<el-tree
							ref="orgUserTreeRef"
							v-if="curSelType === SelectorTypeEnum.USER && curSelUserType === SelectorUserTypeEnum.ORG"
							:data="orgUserTreeData"
							:props="defaultTreeNodeProps"
							node-key="id"
							:default-expanded-keys="orgUserTreeDefaultExpandKeys"
							highlight-current
							:filter-node-method="handleFilterUserTreeNode"
						>
							<template #default="scope">
								<span
									@dblclick="() => scope.data.type === 'user' && handleSelectData(scope.data)"
									:title="scope.node.label"
									class="el-tree-node__label"
								>
									<el-icon v-if="scope.data.type === SelectorTypeEnum.ORG"><OfficeBuilding /></el-icon>
									<el-icon v-else-if="scope.data.type === SelectorTypeEnum.USER"><User /></el-icon>
									<span>{{ scope.node.label }}</span>
								</span>
							</template>
						</el-tree>
						<!-- 选人员-按岗位 -->
						<el-tree
							ref="positionUserTreeRef"
							v-if="curSelType === SelectorTypeEnum.USER && curSelUserType === SelectorUserTypeEnum.POSITION"
							:data="positionUserTreeData"
							:props="defaultTreeNodeProps"
							node-key="id"
							:default-expanded-keys="positionUserTreeDefaultExpandKeys"
							highlight-current
							:filter-node-method="handleFilterUserTreeNode"
						>
							<template #default="scope">
								<span
									@dblclick="() => scope.data.type === 'user' && handleSelectData(scope.data)"
									:title="scope.node.label"
									class="el-tree-node__label"
								>
									<el-icon v-if="scope.data.type === SelectorTypeEnum.POSITION"><Briefcase /></el-icon>
									<el-icon v-else-if="scope.data.type === SelectorTypeEnum.USER"><User /></el-icon>
									<span>{{ scope.node.label }}</span>
								</span>
							</template>
						</el-tree>
						<!-- 选部门 -->
						<el-tree
							ref="orgTreeRef"
							v-if="curSelType === SelectorTypeEnum.ORG"
							:data="orgTreeData"
							:props="defaultTreeNodeProps"
							node-key="id"
							:default-expanded-keys="orgTreeDefaultExpandKeys"
							highlight-current
							:filter-node-method="handleFilterTreeNode"
						>
							<template #default="scope">
								<span @dblclick="() => handleSelectData(scope.data)" :title="scope.node.label" class="el-tree-node__label">
									<el-icon><OfficeBuilding /></el-icon>
									<span>{{ scope.node.label }}</span>
								</span>
							</template>
						</el-tree>
						<!-- 选岗位 -->
						<el-tree
							ref="positionTreeRef"
							v-if="curSelType === SelectorTypeEnum.POSITION"
							:data="positionTreeData"
							:props="defaultTreeNodeProps"
							node-key="id"
							:default-expanded-keys="positionTreeDefaultExpandKeys"
							highlight-current
							:filter-node-method="handleFilterTreeNode"
						>
							<template #default="scope">
								<span @dblclick="() => handleSelectData(scope.data)" :title="scope.node.label" class="el-tree-node__label">
									<el-icon><Briefcase /></el-icon>
									<span>{{ scope.node.label }}</span>
								</span>
							</template>
						</el-tree>
					</el-scrollbar>
				</el-col>
				<!-- 操作按钮 -->
				<el-col :span="2" class="selector-btn-box">
					<div class="selector-btn-box-top">
						<el-button :icon="ArrowRight" @click="handleSingleSelect" :disabled="computedSingleSelBtnDisabled"></el-button>
						<el-button :icon="ArrowLeft" @click="handleSingleDisSelect" :disabled="computedSingleDisSelBtnDisabled"></el-button>
					</div>
					<div class="selector-btn-box-bottom">
						<el-button :icon="DArrowRight" @click="handleAllSelect" :disabled="computedAllSelBtnDisabled"></el-button>
						<el-button :icon="DArrowLeft" @click="handleAllDisSelect" :disabled="computedAllDisSelBtnDisabled"></el-button>
					</div>
				</el-col>
				<!-- 已选 -->
				<el-col :span="11" class="selector-selected-box">
					<el-autocomplete
						ref="selectedDataFilterRef"
						v-model="selectedDataFilterText"
						style="width: 100%"
						popper-class="selector-selected-autocomplete"
						placeholder="请输入关键字进行搜索"
						:prefix-icon="Search"
						:trigger-on-focus="false"
						:fetch-suggestions="handleSearchSelectedDataAsync"
						@select="handleDelSelectedData"
						fit-input-width
						clearable
					>
						<template #default="{ item }">
							<div class="name" :title="item.label">
								<el-icon v-if="item.type === SelectorTypeEnum.ORG"><OfficeBuilding /></el-icon>
								<el-icon v-else-if="item.type === SelectorTypeEnum.USER"><User /></el-icon>
								<span>{{ item.label }}</span>
							</div>
							<span class="company" :title="item.parentLabel">{{ item.parentLabel || " " }}</span>
						</template>
					</el-autocomplete>
					<el-table
						ref="selectedTableRef"
						height="90%"
						:data="selectedTableData"
						:show-header="false"
						stripe
						highlight-current-row
						row-key="value"
						:current-row-key="selectedTableCurRowKey"
						@row-click="row => (selectedTableCurRowKey = row.value)"
						@row-dblclick="handleDelSelectedData"
					>
						<el-table-column prop="label">
							<template #default="scope">
								<el-icon>
									<User v-if="scope.row.type === SelectorTypeEnum.USER" />
									<OfficeBuilding v-else-if="scope.row.type === SelectorTypeEnum.ORG" />
									<Briefcase v-else-if="scope.row.type === SelectorTypeEnum.POSITION" />
								</el-icon>
								<span style="margin-left: 5px">{{ scope.row.label }}</span>
							</template>
						</el-table-column>
						<el-table-column
							prop="parentLabel"
							width="120"
							v-if="selectorProps.selectorTypes.indexOf(SelectorTypeEnum.USER) !== -1"
						/>
						<!--<template #empty><span></span></template>-->
					</el-table>
				</el-col>
			</el-row>
			<el-divider class="selector-bottom-divider" />
			<!-- 选择器按钮区域 -->
			<template #footer>
				<el-row>
					<el-col class="selector-selected-tip">
						<span
							>已选择<span class="num">{{ selectedTableData.length }}</span
							>条</span
						>
					</el-col>
				</el-row>
				<el-row>
					<el-col>
						<el-button @click="selectorVisible = false">取消</el-button>
						<el-button type="primary" @click="handleSubmit">确定</el-button>
					</el-col>
				</el-row>
			</template>
		</el-dialog>
	</div>
</template>

<script setup lang="ts" name="SysCompositeSelector">
import { computed, h, nextTick, ref, VNode, watch } from "vue";
import { ArrowLeft, ArrowRight, Briefcase, DArrowLeft, DArrowRight, OfficeBuilding, Search, User } from "@element-plus/icons-vue";
import { getAuthOrgUserTreeApi, getPositionUserTreeApi } from "@/api/modules/sys/user";
import { getAuthSysOrgTreeApi } from "@/api/modules/sys/org";
import { getAllSysPositionTreeApi } from "@/api/modules/sys/position";
import { SelectorTypeEnum, SelectorUserTypeEnum } from "@/components/Selector/interface";
import { ElAutocomplete, ElDivider, ElMessage, ElTable, ElTree } from "element-plus";
import { GlobalStore } from "@/stores";

const globalStore = GlobalStore();
const loginUserInfo = globalStore.userInfo;
const verticalSpacer: VNode = h(ElDivider, { direction: "vertical" });

// 组件参数定义
interface SelectorProps {
	title?: string; //标题
	selectorTypes?: SelectorTypeEnum[]; //选择器类型，SelectorTypeEnum数组 => 默认选人员
	selectUserTypes?: SelectorUserTypeEnum[]; //用户选择维度，SelectUserTypeEnum数组 => 默认按部门、按岗位、按联系组
	orgUserTreeApi?: () => Promise<any>; //部门用户树形接口api，用于选人员-按部门 => 默认当前登录用户所授权的机构用户树
	positionUserTreeApi?: () => Promise<any>; //岗位用户树形接口api，用于选人员-按岗位
	contactUserTreeApi?: () => Promise<any>; //联系组用户树形接口api，用于选人员-按联系组
	orgTreeApi?: () => Promise<any>; //部门树形接口api，用于选部门 => 默认当前登录用户所授权的机构树
	positionTreeApi?: () => Promise<any>; //岗位树形接口api，用于选岗位
	contactPageApi?: () => Promise<any>; //联系组接口api，用于选联系组
	maxSelectNum?: number; //最大选择数 <=0 不限制 => 默认0
	notEmpty?: boolean; //是否允许不选，true=禁止不选false=允许不选 =>默认true
	checkDataScopeStrictly?: boolean; //是否严格判断数据级权限 => 默认true
}
// 返回值定义
interface SelectorResult {
	type: string;
	label: string;
	value: string;
	parentLabel: string;
	parentValue: string;
	extendValue: string;
}

// 接受父组件参数，配置默认值
const selectorProps = withDefaults(defineProps<SelectorProps>(), {
	title: "请选择",
	selectorTypes: () => [SelectorTypeEnum.USER, SelectorTypeEnum.ORG, SelectorTypeEnum.POSITION, SelectorTypeEnum.CONTACT],
	selectUserTypes: () => [SelectorUserTypeEnum.ORG, SelectorUserTypeEnum.POSITION, SelectorUserTypeEnum.CONTACT],
	orgUserTreeApi: getAuthOrgUserTreeApi,
	positionUserTreeApi: getPositionUserTreeApi,
	orgTreeApi: getAuthSysOrgTreeApi,
	positionTreeApi: getAllSysPositionTreeApi,
	maxSelectNum: 0,
	notEmpty: true,
	checkDataScopeStrictly: true
});

const selectorVisible = ref(false);

const defaultTreeNodeProps = {
	children: "children",
	label: "name"
};

// 当前选中的大类
const curSelType = ref<string>("");
// 当前选中的选人员类型
const curSelUserType = ref<string>("");
const handleSelType = (param: string) => {
	if (param != curSelType.value) {
		if (param != SelectorTypeEnum.USER) {
			curSelUserType.value = selectorProps.selectUserTypes[0];
		}
		curSelType.value = param;
		getSelectorData();
	}
};
const handleSelUserType = (param: string) => {
	if (param != curSelUserType.value) {
		curSelUserType.value = param;
		getSelectorData();
	}
};

const orgUserTreeData = ref<{ [key: string]: any }[] | null>(null);
const orgUserTreeDefaultExpandKeys = ref<{ [key: string]: any }>([]);
const positionUserTreeData = ref<{ [key: string]: any }[] | null>(null);
const positionUserTreeDefaultExpandKeys = ref<{ [key: string]: any }>([]);
const orgTreeData = ref<{ [key: string]: any }[] | null>(null);
const orgTreeDefaultExpandKeys = ref<{ [key: string]: any }>([]);
const positionTreeData = ref<{ [key: string]: any }[] | null>(null);
const positionTreeDefaultExpandKeys = ref<{ [key: string]: any }>([]);

// 获取选择器数据
const getSelectorData = async () => {
	// 选人员->按部门树
	if (!orgUserTreeData.value) {
		if (curSelType.value === SelectorTypeEnum.USER && curSelUserType.value === SelectorUserTypeEnum.ORG) {
			const { data } = await selectorProps.orgUserTreeApi();
			data.forEach((item: { [key: string]: any }) => {
				if (item.parentId === "0") {
					orgUserTreeDefaultExpandKeys.value.push(item.id);
				}
			});
			orgUserTreeData.value = data;
		}
	}
	// 选人员->按岗位树
	if (!positionUserTreeData.value) {
		if (curSelType.value === SelectorTypeEnum.USER && curSelUserType.value === SelectorUserTypeEnum.POSITION) {
			const { data } = await selectorProps.positionUserTreeApi();
			data.forEach((item: { [key: string]: any }) => {
				if (item.parentId === "0") {
					positionUserTreeDefaultExpandKeys.value.push(item.id);
				}
			});
			positionUserTreeData.value = data;
		}
	}
	// 选部门树
	if (!orgTreeData.value) {
		if (curSelType.value === SelectorTypeEnum.ORG) {
			const { data } = await selectorProps.orgTreeApi();
			data.forEach((item: { [key: string]: any }) => {
				if (item.parentId === "0") {
					orgTreeDefaultExpandKeys.value.push(item.id);
				}
			});
			orgTreeData.value = data;
		}
	}
	// 选岗位树
	if (!positionTreeData.value) {
		if (curSelType.value === SelectorTypeEnum.POSITION) {
			const { data } = await selectorProps.positionTreeApi();
			data.forEach((item: { [key: string]: any }) => {
				if (item.parentId === "0") {
					positionTreeDefaultExpandKeys.value.push(item.id);
				}
			});
			positionTreeData.value = data;
		}
	}
};

// 已选数据
const selectedTableData = ref<SelectorResult[]>([]);
const selectedTableRef = ref<InstanceType<typeof ElTable>>();
const selectedTableCurRowKey = ref<string>();

watch(
	() => selectorProps.selectorTypes,
	() => {
		curSelType.value = selectorProps.selectorTypes[0];
		if (curSelType.value === SelectorTypeEnum.USER) {
			curSelUserType.value = selectorProps.selectUserTypes[0];
		}
	},
	{ deep: true, immediate: true }
);

// 操作按钮
const computedSingleSelBtnDisabled = computed(() => {
	let disabled = true;
	switch (curSelType.value) {
		case SelectorTypeEnum.USER:
			switch (curSelUserType.value) {
				case SelectorUserTypeEnum.ORG:
					disabled = !orgUserTreeRef.value?.getCurrentKey();
					break;
				case SelectorUserTypeEnum.POSITION:
					disabled = !positionUserTreeRef.value?.getCurrentKey();
					break;
			}
			break;
		case SelectorTypeEnum.ORG:
			disabled = !orgTreeRef.value?.getCurrentKey();
			break;
		case SelectorTypeEnum.POSITION:
			disabled = !positionTreeRef.value?.getCurrentKey();
			break;
	}
	return disabled;
});
const computedSingleDisSelBtnDisabled = computed(() => {
	return !selectedTableCurRowKey.value;
});
const computedAllSelBtnDisabled = computed(() => {
	return getCurNotSelectedDatas().length === 0;
});
const computedAllDisSelBtnDisabled = computed(() => {
	return !selectedTableData.value.filter(data => data.type === curSelType.value).length;
});

const handleSingleSelect = () => {
	const curNotSelDatas = getCurNotSelectedDatas();
	let curNodeKey: any = null;
	switch (curSelType.value) {
		case SelectorTypeEnum.USER:
			switch (curSelUserType.value) {
				case SelectorUserTypeEnum.ORG:
					curNodeKey = orgUserTreeRef.value!.getCurrentKey();
					curNotSelDatas.forEach(data => {
						const thizPath = data.path.split(".");
						if (curNodeKey.indexOf("#") === -1) {
							if (thizPath.indexOf(curNodeKey) !== -1) {
								handleSelectData(data);
							}
						} else {
							if (thizPath.indexOf(curNodeKey.split("#")[0]) !== -1 && thizPath.indexOf(curNodeKey.split("#")[1]) !== -1) {
								handleSelectData(data);
							}
						}
					});
					break;
				case SelectorUserTypeEnum.POSITION:
					curNodeKey = positionUserTreeRef.value!.getCurrentKey();
					curNotSelDatas.forEach(data => {
						const thizPath = data.path.split(".");
						if (curNodeKey.indexOf("#") === -1) {
							if (thizPath.indexOf(curNodeKey) !== -1) {
								handleSelectData(data);
							}
						} else {
							if (thizPath.indexOf(curNodeKey.split("#")[0]) !== -1 && thizPath.indexOf(curNodeKey.split("#")[1]) !== -1) {
								handleSelectData(data);
							}
						}
					});
					break;
			}
			break;
		case SelectorTypeEnum.ORG:
			curNodeKey = orgTreeRef.value!.getCurrentKey();
			curNotSelDatas.forEach(data => {
				const thizPath = data.path.split(".");
				if (thizPath.indexOf(curNodeKey) !== -1) {
					handleSelectData(data);
				}
			});
			break;
		case SelectorTypeEnum.POSITION:
			curNodeKey = positionTreeRef.value!.getCurrentKey();
			curNotSelDatas.forEach(data => {
				const thizPath = data.path.split(".");
				if (thizPath.indexOf(curNodeKey) !== -1) {
					handleSelectData(data);
				}
			});
			break;
	}
};
const handleSingleDisSelect = () => {
	handleDelSelectedData(selectedTableData.value.find(data => data.value === selectedTableCurRowKey.value));
};
const handleAllSelect = () => {
	const curNotSelDatas = getCurNotSelectedDatas();
	curNotSelDatas.forEach(data => handleSelectData(data));
};
const handleAllDisSelect = () => {
	selectedTableData.value = selectedTableData.value.filter(data => data.type !== curSelType.value);
};

// 获取当前待选树中还未被选中的节点
const getCurNotSelectedDatas = () => {
	let result: { [key: string]: any }[] = [];
	switch (curSelType.value) {
		case SelectorTypeEnum.USER:
			switch (curSelUserType.value) {
				case SelectorUserTypeEnum.ORG:
					recurseGetTreeDatas(orgUserTreeData.value, SelectorTypeEnum.USER, result);
					break;
				case SelectorUserTypeEnum.POSITION:
					recurseGetTreeDatas(positionUserTreeData.value, SelectorTypeEnum.USER, result);
					break;
			}
			break;
		case SelectorTypeEnum.ORG:
			recurseGetTreeDatas(orgTreeData.value, SelectorTypeEnum.ORG, result);
			break;
		case SelectorTypeEnum.POSITION:
			recurseGetTreeDatas(positionTreeData.value, SelectorTypeEnum.POSITION, result);
			break;
	}
	return result;
};
// 递归组装当前待选树中未被选中的节点
const recurseGetTreeDatas = (treeDatas: { [key: string]: any }[] | null, type: string, result: { [key: string]: any }[]) => {
	treeDatas?.forEach(data => {
		if (type === SelectorTypeEnum.USER) {
			if (data.type === "user") {
				if (!isExistInSelectedTable(data.id).result) {
					result.push({
						id: data.id,
						name: data.name,
						parentName: data.parentName,
						path: data.treePath
					});
				}
			}
		} else {
			if (!isExistInSelectedTable(data.id).result) {
				result.push({
					id: data.id,
					name: data.name,
					path: data.treePath
				});
			}
		}
		recurseGetTreeDatas(data.children, type, result);
	});
};

// 接收父组件传过来的参数
const acceptParams = async (params: { [key: string]: any }) => {
	await getSelectorData();
	selectedTableData.value = [];
	if (params && params.selected) {
		for (const param of params.selected) {
			const checkResult = isExistInSelectedTable(param.value);
			if (!checkResult.result) {
				selectedTableData.value.push({ ...param, value: checkResult.data });
			}
		}
	}
	selectorVisible.value = true;
};

// 判断待选节点id是否在已选择的表格中
const isExistInSelectedTable = (id: string) => {
	let thizSelectedId = id;
	if (curSelType.value === SelectorTypeEnum.USER) {
		if (thizSelectedId.includes("#")) {
			thizSelectedId = thizSelectedId.split("#")[1];
		}
	}
	let isExist = false;
	selectedTableData.value.forEach(data => {
		if (data.type == curSelType.value && data.value == thizSelectedId) {
			isExist = true;
		}
	});
	return {
		result: isExist,
		data: thizSelectedId
	};
};

const selectDataFilterText = ref("");
const selectedDataFilterText = ref("");
const selectedDataFilterRef = ref<InstanceType<typeof ElAutocomplete>>();
watch(selectDataFilterText, val => {
	if (val) {
		orgUserTreeRef.value?.filter(val);
		orgTreeRef.value?.filter(val);
	} else {
		refreshTreeState();
	}
});

const handleSearchSelectedDataAsync = (queryString: string, cb: any) => {
	let results = queryString ? selectedTableData.value.filter(data => data.label.indexOf(queryString) !== -1) : [];
	cb(results);
};

const orgUserTreeRef = ref<InstanceType<typeof ElTree>>();
const positionUserTreeRef = ref<InstanceType<typeof ElTree>>();
const orgTreeRef = ref<InstanceType<typeof ElTree>>();
const positionTreeRef = ref<InstanceType<typeof ElTree>>();

// 待选人员树节点筛选
const handleFilterUserTreeNode = (value: string, data: { [key: string]: any }) => {
	if (value == null) {
		if (data.type != "user") {
			return true;
		}
		return !isExistInSelectedTable(data.id).result;
	}
	return data.name.includes(value);
};
// 待选树节点筛选
const handleFilterTreeNode = (value: string, data: { [key: string]: any }) => {
	if (value == null) {
		return !isExistInSelectedTable(data.id).result;
	}
	return data.name.includes(value);
};
// 手动刷新待选数据树状态
const refreshTreeState = () => {
	nextTick(() => {
		orgUserTreeRef.value?.filter(null);
		positionUserTreeRef.value?.filter(null);
		orgTreeRef.value?.filter(null);
		positionTreeRef.value?.filter(null);

		orgUserTreeRef.value?.setCurrentKey(undefined);
		positionUserTreeRef.value?.setCurrentKey(undefined);
		orgTreeRef.value?.setCurrentKey(undefined);
		positionTreeRef.value?.setCurrentKey(undefined);

		selectedTableCurRowKey.value = undefined;
		selectedTableRef.value?.setCurrentRow(undefined);

		selectDataFilterText.value = "";
		selectedDataFilterText.value = "";
		if (selectedDataFilterRef.value) selectedDataFilterRef.value!.suggestions = [];
	});
};
watch(
	() => selectedTableData,
	() => {
		refreshTreeState();
	},
	{ deep: true, immediate: true }
);
watch(
	() => curSelType,
	() => {
		refreshTreeState();
	},
	{ deep: true, immediate: true }
);
watch(
	() => curSelUserType,
	() => {
		refreshTreeState();
	},
	{ deep: true, immediate: true }
);

// 添加选择的数据
const handleSelectData = (param: { [key: string]: any }) => {
	const checkResult = isExistInSelectedTable(param.id);
	if (!checkResult.result) {
		if (selectorProps.checkDataScopeStrictly) {
			//选部门情况，判断是否数据范围权限
			if (curSelType.value === SelectorTypeEnum.ORG) {
				if (loginUserInfo && !loginUserInfo.superAdmin) {
					const grantedDataScopes = loginUserInfo.grantedDataScopes;
					let isGrantedDataScope = grantedDataScopes && grantedDataScopes.indexOf(param.id) !== -1;
					if (!isGrantedDataScope) {
						return false;
					}
				}
			}
		}
		const insertRow: SelectorResult = {
			value: checkResult.data,
			label: param.name,
			type: curSelType.value,
			parentLabel: param.parentName,
			parentValue: param.parentId,
			extendValue: param.extendValue || ""
		};
		selectedTableData.value.push(insertRow);
	}
};

// 移除选择的数据
const handleDelSelectedData = (row: SelectorResult | undefined) => {
	if (!row) return;
	selectedTableData.value.splice(
		selectedTableData.value.findIndex(data => data.value === row.value),
		1
	);
};

// 选中回调监听，触发父组件改变
type EmitProps = {
	(e: "select", val: { [key: string]: any }[]): void;
};
const emit = defineEmits<EmitProps>();

// 确定选择
const handleSubmit = () => {
	if (selectorProps.notEmpty && !selectedTableData.value.length) {
		ElMessage.warning({ message: "请选择" });
		return false;
	}
	if (selectorProps.maxSelectNum > 0) {
		if (selectedTableData.value.length > selectorProps.maxSelectNum) {
			ElMessage.warning({ message: `限制选择${selectorProps.maxSelectNum}条记录` });
			return false;
		}
	}
	emit("select", JSON.parse(JSON.stringify(selectedTableData.value)));
	selectorVisible.value = false;
};

// 关闭
const handleClose = () => {
	selectorVisible.value = false;
};

// 暴露给父组件的参数和方法(外部需要什么，都可以从这里暴露出去)
defineExpose({
	acceptParams: acceptParams
});
</script>

<style scoped lang="scss">
.selector-dialog :deep(.el-dialog__body) {
	padding: 10px 20px 0;
	.selector-tab-divider {
		margin: 5px 0;
	}
	.selector-box {
		.selector-operate-box {
			height: 365px;
			padding: 0 10px 0 0;
			.el-input {
				margin: 0 0 5px;
			}
		}
		.selector-btn-box {
			display: grid;
			border-right: 1px var(--el-border-color) var(--el-border-style);
			border-left: 1px var(--el-border-color) var(--el-border-style);
			.selector-btn-box-top,
			.selector-btn-box-bottom {
				display: grid;
				grid-row-gap: 2em;
				align-content: center;
				justify-items: center;
			}
		}
		.selector-selected-box {
			height: 366px;
			padding: 0 0 0 10px;
			.el-input {
				margin: 0 0 5px;
			}
		}
	}
	.selector-bottom-divider {
		margin: 0;
	}
}
.selector-dialog :deep(.el-dialog__footer) {
	padding-top: 0;
	.selector-selected-tip {
		height: 32px;
		font-size: 14px;
		line-height: 32px;
		text-align: right;
		.num {
			margin: 0 5px;
			color: var(--el-color-primary);
		}
	}
}
.selector-selected-autocomplete {
	li {
		padding: 7px;
		line-height: normal;
		.name {
			overflow: hidden;
			line-height: 24px;
			text-overflow: ellipsis;
		}
		.company {
			font-size: 12px;
			line-height: 12px;
			color: #b4b4b4;
		}
	}
}
.selector-btn-box :deep(.el-button) {
	margin-left: 0;
}
</style>
