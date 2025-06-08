<template><div></div></template>

<script setup lang="ts">
import { defineExpose } from "vue";

const acceptParams = () => {
	console.info("invoked....");
};

defineExpose({
	acceptParams
});
</script>

<style scoped lang="scss"></style>
