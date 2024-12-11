<template>
	<el-drawer v-model="formVisible" destroy-on-close :size="550" title="系统日志">
		<el-descriptions :column="1" border style="margin-bottom: 20px">
			<el-descriptions-item label="日志类别">
				<el-tag
					:type="
						['primary', 'danger', 'success', 'info'][(formProps.rowData.logType != undefined ? formProps.rowData.logType : 1) - 1]
					"
					>{{
						["操作日志", "异常日志", "登录日志", "登出日志"][
							(formProps.rowData.logType != undefined ? formProps.rowData.logType : 1) - 1
						]
					}}</el-tag
				>
			</el-descriptions-item>
			<el-descriptions-item label="日志名称">{{ formProps.rowData.logName }}</el-descriptions-item>
			<el-descriptions-item label="执行详情" v-if="formProps.rowData.logDetail">{{
				formProps.rowData.logDetail
			}}</el-descriptions-item>
			<el-descriptions-item label="IP">{{ formProps.rowData.operateIp }}</el-descriptions-item>
			<el-descriptions-item label="浏览器">{{ formProps.rowData.operateBrowser }}</el-descriptions-item>
			<el-descriptions-item label="操作系统">{{ formProps.rowData.operateOs }}</el-descriptions-item>
			<el-descriptions-item label="操作者">{{ formProps.rowData.operateUser }}</el-descriptions-item>
			<el-descriptions-item label="操作时间">{{ formProps.rowData.operateTime }}</el-descriptions-item>
			<el-descriptions-item label="请求uri">{{ formProps.rowData.requestUri }}</el-descriptions-item>
		</el-descriptions>
		<el-space direction="vertical" style="width: 100%" alignment="normal" v-if="!computedIsLoginAccessLog">
			请求参数：
			<div class="hljs-container">
				<highlightjs language="JSON" :autodetect="false" :code="formProps.rowData.paramJson || '无'"></highlightjs>
			</div>
		</el-space>
		<el-space direction="vertical" style="width: 100%" alignment="normal" v-if="!computedIsLoginAccessLog">
			返回结果：
			<div class="hljs-container">
				<highlightjs language="JSON" :autodetect="false" :code="formProps.rowData.resultJson || '无'"></highlightjs>
			</div>
		</el-space>
		<template #footer>
			<el-button @click="formVisible = false">关闭</el-button>
		</template>
	</el-drawer>
</template>

<script setup lang="ts" name="SysLogInfo">
import { ref, computed } from "vue";
import { SysLog } from "@/api/modules/sys/log";

interface FormProps {
	[key: string]: any;
	rowData: Partial<SysLog.Form>;
	getTableList?: () => void;
}

const formVisible = ref(false);
const formProps = ref<FormProps>({
	rowData: {}
});

// 是否访问日志计算属性
const computedIsLoginAccessLog = computed(() => {
	return formProps.value.rowData.logType && (formProps.value.rowData.logType === 3 || formProps.value.rowData.logType === 4);
});

// 接收父组件传过来的参数
const acceptParams = (params: FormProps) => {
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

	formVisible.value = true;
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss">
.hljs-container pre {
	width: 500px;
}
</style>
