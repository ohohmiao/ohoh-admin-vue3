<template>
	<div class="table-box">
		<ProTable
			ref="proTable"
			title="系统日志列表"
			:columns="columns"
			:requestApi="getSysLogPageApi"
			:searchCol="{ xs: 1, sm: 1, md: 2, lg: 3, xl: 3 }"
		>
			<!-- 表格操作 -->
			<template #operation="scope">
				<el-button type="primary" link :icon="View" @click="openForm(scope.row)">详情</el-button>
			</template>
		</ProTable>
		<!-- 系统日志详情 -->
		<SysLogInfo ref="formRef"></SysLogInfo>
	</div>
</template>

<script setup lang="ts" name="SysLogManage">
import { ref } from "vue";
import { View } from "@element-plus/icons-vue";
import ProTable from "@/components/ProTable/index.vue";
import { SysLog, getSysLogPageApi } from "@/api/modules/sys/log";
import SysLogInfo from "./info.vue";
import { ColumnProps } from "@/components/ProTable/interface";

const proTable = ref();

// 表格配置项
const columns: ColumnProps<SysLog.Form>[] = [
	{
		prop: "logType",
		label: "日志类别",
		width: 110,
		tag: true,
		enum: [
			{ label: "登录日志", value: 3, tagType: "success" },
			{ label: "登出日志", value: 4, tagType: "info" },
			{ label: "操作日志", value: 1 },
			{ label: "异常日志", value: 2, tagType: "danger" }
		],
		search: { el: "select" }
	},
	{ prop: "logName", label: "日志名称", search: { el: "input" } },
	{ prop: "operateIp", label: "IP" },
	{ prop: "operateBrowser", label: "浏览器" },
	{ prop: "operateOs", label: "操作系统" },
	{ prop: "operateUser", label: "操作者" },
	{ prop: "operateTime", label: "操作时间" },
	{ prop: "operation", label: "操作", width: 140 }
];

const formRef = ref<InstanceType<typeof SysLogInfo> | null>(null);
const openForm = (rowData: Partial<SysLog.Form> = {}) => {
	const plainRowData = JSON.parse(JSON.stringify(rowData));
	const params = {
		rowData: { ...plainRowData },
		getTableList: proTable.value.getTableList
	};
	formRef.value?.acceptParams(params);
};
</script>
