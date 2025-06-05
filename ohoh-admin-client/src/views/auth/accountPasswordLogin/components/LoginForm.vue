<template>
	<el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" size="large">
		<el-form-item prop="username">
			<el-input v-model="loginForm.username" placeholder="账号">
				<template #prefix>
					<el-icon class="el-input__icon"><User /></el-icon>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item prop="password">
			<el-input type="password" v-model="loginForm.password" placeholder="密码" show-password autocomplete="new-password">
				<template #prefix>
					<el-icon class="el-input__icon"><Lock /></el-icon>
				</template>
			</el-input>
		</el-form-item>
		<el-form-item prop="code">
			<el-row :gutter="16">
				<el-col :span="20">
					<el-input v-model="loginForm.code" placeholder="验证码" maxlength="4">
						<template #prefix>
							<el-icon class="el-input__icon"><Key /></el-icon>
						</template>
					</el-input>
				</el-col>
				<el-col :span="4">
					<div class="login-code">
						<img :src="codeUrl" @click="getCode" title="看不清？点击换一张" alt="验证码" />
					</div>
				</el-col>
			</el-row>
		</el-form-item>
	</el-form>
	<div class="login-btn">
		<el-button :icon="CircleClose" round @click="resetForm(loginFormRef)" size="large">重置</el-button>
		<el-button :icon="UserFilled" round @click="login(loginFormRef)" size="large" type="primary" :loading="loading">
			登录
		</el-button>
	</div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { Login } from "@/api/interface";
import { ElNotification } from "element-plus";
import { loginApi, getLoginUserApi } from "@/api/modules/auth/accountPasswordLogin";
import { captchaImageApi } from "@/api/modules/captcha";
import { GlobalStore } from "@/stores";
import { TabsStore } from "@/stores/modules/tabs";
import { KeepAliveStore } from "@/stores/modules/keepAlive";
import { getTimeState } from "@/utils/util";
import { HOME_URL } from "@/config/config";
import { initDynamicRouter } from "@/routers/modules/dynamicRouter";
import { CircleClose, UserFilled, Lock, User, Key } from "@element-plus/icons-vue";
import type { ElForm } from "element-plus";
import smCrypto from "@/utils/smCrypto";

const router = useRouter();
const tabsStore = TabsStore();
const keepAlive = KeepAliveStore();
const globalStore = GlobalStore();

// 定义 formRef（校验规则）
type FormInstance = InstanceType<typeof ElForm>;
const loginFormRef = ref<FormInstance>();
const loginRules = reactive({
	username: [{ required: true, message: "请输入账号", trigger: "blur" }],
	password: [{ required: true, message: "请输入密码", trigger: "blur" }],
	code: [{ required: true, message: "请输入验证码", trigger: "blur" }]
});

const codeUrl = ref("");
const getCode = async () => {
	const { data } = await captchaImageApi();
	codeUrl.value = "data:image/jpg;base64," + data.img;
	loginForm.uuid = data.uuid;
	loginForm.code = "";
};

const loading = ref(false);
const loginForm = reactive<Login.ReqLoginForm>({ username: "coder", password: "123456", code: "", uuid: "" });
const login = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.validate(valid => {
		if (!valid) return;
		loading.value = true;
		try {
			// 1.执行登录接口
			loginApi({ ...loginForm, password: smCrypto.doSm2ClientEncrypt(loginForm.password) })
				.then(async resultData => {
					globalStore.setToken(resultData.data);

					const { data } = await getLoginUserApi();
					globalStore.setUserInfo(data);

					// 2.添加动态路由
					await initDynamicRouter();

					// 3.清空 tabs、keepAlive 保留的数据
					await tabsStore.closeMultipleTab();
					await keepAlive.setKeepAliveName();

					// 4.跳转到首页
					router.push(HOME_URL).then(() => {
						ElNotification({
							title: getTimeState(),
							message: "欢迎登录",
							type: "success",
							duration: 3000
						});
					});
				})
				.catch(() => {
					loginForm.password = "";
					getCode();
				});
		} finally {
			loading.value = false;
		}
	});
};

// resetForm
const resetForm = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.resetFields();
};

onMounted(() => {
	// 监听enter事件（调用登录）
	document.onkeydown = (e: any) => {
		e = window.event || e;
		if (e.code === "Enter" || e.code === "enter" || e.code === "NumpadEnter") {
			if (loading.value) return;
			login(loginFormRef.value);
		}
	};
	getCode();
});
</script>

<style scoped lang="scss">
@import "../index.scss";
</style>
<style rel="stylesheet/scss" lang="scss">
.login-code {
	img {
		cursor: pointer;
		vertical-align: middle;
		height: 38px;
	}
}
</style>
