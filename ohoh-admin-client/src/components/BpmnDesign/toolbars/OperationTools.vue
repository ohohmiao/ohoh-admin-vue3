<template>
	<el-button-group>
		<el-button type="primary" plain>打开文件<input type="file" style="display: none" /></el-button>
		<el-popover placement="bottom" trigger="hover">
			<template #reference>
				<el-button type="primary" plain>导出文件</el-button>
			</template>
			<template #default>
				<div class="button-list_column">
					<el-button type="primary" @click="downloadProcessAsBpmn()">导出为Bpmn</el-button>
					<el-button type="primary" @click="downloadProcessAsXml()">导出为XML</el-button>
					<el-button type="primary" @click="downloadProcessAsSvg()">导出为SVG</el-button>
				</div>
			</template>
		</el-popover>
		<el-popover placement="bottom" trigger="hover">
			<template #reference>
				<el-button type="primary" plain>预览文件</el-button>
			</template>
			<div class="button-list_column">
				<el-button type="primary" @click="openXMLPreviewModel()">预览为XML</el-button>
				<el-button type="primary" @click="openJSONPreviewModel()">预览为JSON</el-button>
			</div>
		</el-popover>
	</el-button-group>
</template>

<script setup lang="ts">
const downloadProcessAsBpmn = () => {};
const downloadProcessAsXml = () => {};
const downloadProcessAsSvg = () => {};

const openXMLPreviewModel = () => {};
const openJSONPreviewModel = () => {};
</script>
