<template>
	<div class="main-box">
		<table>
			<tbody>
				<tr>
					<!-- 左侧类别 -->
					<td class="classify_list" v-show="!switchOpen">
						<div>
							<div
								:class="curFirstLevelDefTypeId == item.deftypeId ? ['classify_list_item', 'on'] : ['classify_list_item']"
								v-for="item in firstLevelDefTypes"
								:key="item.deftypeId"
								@click="curFirstLevelDefTypeId = item.deftypeId!"
							>
								<el-text>{{ item.deftypeName }}</el-text>
							</div>
						</div>
					</td>
					<!-- 右侧可发起流程列表 -->
					<td class="section-box">
						<div v-for="[key, list] in curFlowDefMapEntries" :key="key" class="section">
							<div class="section-title">{{ key }}</div>
							<div class="button-grid">
								<div v-for="item in list" :key="item.defId" class="card-button" @click="handleClick(item)">
									{{ item.defName }}
								</div>
							</div>
						</div>
						<el-empty v-if="!curFlowDefMapEntries.length"></el-empty>
					</td>
				</tr>
			</tbody>
		</table>
		<div
			class="classify_arrow"
			:style="switchOpen ? '' : 'margin-left: 202px'"
			:title="switchOpen ? '查看' : '隐藏'"
			@click="switchOpen = !switchOpen"
		>
			<el-icon v-if="!switchOpen" style="background-color: #ddd"><ArrowLeft /></el-icon>
			<el-icon v-else style="background-color: #ddd"><ArrowRight /></el-icon>
		</div>
		<!-- 动态流程表单 -->
		<component :is="DynamicFlowForm" v-if="DynamicFlowForm" ref="dynamicFlowFormRef"></component>
	</div>
</template>

<script setup lang="ts" name="MyFlowManage">
import { ref, onMounted, watch, computed, defineAsyncComponent, shallowRef } from "vue";
import type { DefineComponent } from "vue";
import {
	getWorkflowTypeFirstLevelNodeListApi,
	getWorkflowIntitiableListApi,
	WorkflowDef,
	WorkflowDefType
} from "@/api/modules/workflow/def";
import { getWorkflowFlowInfoApi } from "@/api/modules/workflow/core";

const switchOpen = ref(false);

// 第一层级流程类别
const firstLevelDefTypes = ref<WorkflowDefType.Form[]>([]);
// 当前选中的流程类别
const curFirstLevelDefTypeId = ref("");
const curFlowDefMap = ref<Map<string, WorkflowDef.Form[]>>(new Map());
const curFlowDefMapEntries = computed(() => Object.entries(curFlowDefMap.value));

onMounted(async () => {
	const { data } = await getWorkflowTypeFirstLevelNodeListApi();
	firstLevelDefTypes.value = data;
	curFirstLevelDefTypeId.value = firstLevelDefTypes.value[0].deftypeId || "-1";
});

watch(
	() => curFirstLevelDefTypeId.value,
	async () => {
		const { data } = await getWorkflowIntitiableListApi({ deftypeId: curFirstLevelDefTypeId.value });
		curFlowDefMap.value = data;
	}
);

// 点击流程
const DynamicFlowForm = shallowRef();
const dynamicFlowFormRef = shallowRef();
// Vite自动扫描指定目录下的Vue文件
const dynamicFlowFormViews = import.meta.glob("@/views/workflow/workspace/**/*.vue");

const handleClick = async (item: any) => {
	const { data } = await getWorkflowFlowInfoApi({ defCode: item.defCode, defVersion: item.defVersion });
	const dynamicFlowFormPath = `/src/views/workflow/workspace${data.formPath}.vue`;
	const thizLoader = dynamicFlowFormViews[dynamicFlowFormPath];
	if (!thizLoader) {
		console.warn("表单组件未找到：", dynamicFlowFormPath);
		return;
	}
	DynamicFlowForm.value = defineAsyncComponent(thizLoader as () => Promise<DefineComponent>);

	// nextTick、Promise.resolve().then(() => {})等均didn't work...
	setTimeout(() => {
		dynamicFlowFormRef.value?.acceptParams();
	}, 100);
};
</script>

<style scoped lang="scss">
table {
	width: 100%;
	border-spacing: 0;
	border-collapse: collapse;
}
.classify_list {
	width: 200px;
	vertical-align: top;
	border-right: 1px solid #ddd;
	background-color: #fafafa;
}
.classify_list_item {
	height: 35px;
	line-height: 35px;
	padding-left: 34px;
	cursor: pointer;
}
.classify_list_item.on {
	background-color: #e1e3e6;
}
.classify_arrow {
	position: fixed;
	top: 300px;
	opacity: 0.75;
	padding: 0;
	z-index: 0;
	width: 16px;
	height: 16px;
	float: left;
	cursor: pointer;
}
.section-box {
	vertical-align: top;
	background-color: #fff;
	padding: 25px 35px;
}
.section {
	margin-bottom: 40px;
}
.section-title {
	font-weight: bold;
	color: var(--el-color-primary);
	border-left: 2px solid var(--el-color-primary);
	padding-left: 8px;
	margin-bottom: 16px;
}
.button-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 16px;
}
.card-button {
	padding: 10px 12px;
	border: 1px solid #d9d9d9;
	border-radius: 12px;
	cursor: pointer;
	background-color: #fff;
	box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
	transition: all 0.2s ease;
	min-width: 120px;
	text-align: center;
}
.card-button:hover {
	border-color: var(--el-color-primary-light-1);
	box-shadow: 0 2px 6px var(--el-color-primary-light-7);
}
</style>
