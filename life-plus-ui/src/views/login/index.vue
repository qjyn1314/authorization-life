<template>
  <div class="h-full">
    <el-row class="h-100%">
      <div class="absolute top-10px right-10px flex flex-items-center">
        <KoiLanguage></KoiLanguage>
        <KoiDark></KoiDark>
      </div>
      <el-col :lg="16" :md="12" :sm="15" :xs="0" class="flex items-center justify-center">
        <div class="login-background w-85% h-100%"></div>
        <div class="absolute text-center select-none">
          <el-image class="w-400px h-360px mb-50px animate-float <md:hidden <lg:w-360px h-320px" :src="bg"/>
          <div class="font-bold text-3xl chroma-text mb-6px text-center <lg:text-2xl <md:hidden">
            {{ $t("login.welcome") }} {{ loginTitle || "命运迷雾 管理平台" }}
          </div>
          <div class="chroma-text text-lg text-center <md:hidden">{{ $t("login.description") }}</div>
        </div>
        <!-- 备案号-->
        <!--
        <div class="beianhao select-none <md:hidden">
                  <a class="chroma-text" href="https://beian.miit.gov.cn/" target="_blank"
                    >{{ $t("login.beianhao") }}：豫ICP备2022022094号-1</a
                  >
                </div>-->
      </el-col>
      <el-col :lg="8" :md="12" :sm="9" :xs="24" class="dark:bg-#161616 bg-gray-100 flex items-center justify-center flex-col">
        <div class="flex items-center">
          <el-image class="rounded-full w-36px h-36px" :src="logo"/>
          <div class="ml-6px font-bold text-xl">{{ loginTitle || "命运迷雾 管理平台" }}</div>
        </div>
        <div class="flex items-center space-x-3 text-gray-400 mt-16px mb-16px">
          <span class="h-1px w-16 bg-gray-300 inline-block"></span>
          <span class="text-center">{{ $t("login.account") }}</span>
          <span class="h-1px w-16 bg-gray-300 inline-block"></span>
        </div>
        <!-- 输入框盒子 -->
        <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="w-260px">
          <el-form-item prop="loginName">
            <el-input type="text" :placeholder="$t('login.loginName')" :suffix-icon="User" v-model="loginForm.username"/>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              :placeholder="$t('login.password')"
              show-password
              :suffix-icon="Lock"
              v-model="loginForm.password"
            />
          </el-form-item>
          <el-form-item prop="securityCode">
            <el-input
              type="text"
              :placeholder="$t('login.security')"
              :suffix-icon="Open"
              v-model="loginForm.captchaCode"
              @keydown.enter="oauthSsoLogin"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-image class="w-100px h-30px" :src="loginForm.imageBase64" @click="genCaptcha"/>
            <el-button text size="small" class="ml-6px" @click="genCaptcha">
              <div class="text-gray-400 hover:text-#8B5CF6 select-none">{{ $t("login.blur") }}</div>
            </el-button>
          </el-form-item>
          <!-- 登录按钮 -->
          <el-form-item>
            <el-button
              type="primary"
              v-if="!loading"
              class="w-245px bg-[--el-color-primary]"
              round
              v-throttle:3000="oauthSsoLogin"
            >{{ $t("login.in") }}
            </el-button
            >
            <el-button type="primary" v-else class="w-245px bg-[--el-color-primary]" round :loading="loading">{{
                $t("login.center")
              }}
            </el-button>
          </el-form-item>
        </el-form>
        <!-- 备案号-->
        <!--
                <div class="beianhao select-none lg:hidden">
                  <a class="chroma-text" href="https://beian.miit.gov.cn/" target="_blank">
                  {{ $t("login.beianhao") }}：豫ICP备2022022094号-1
                  </a>
                </div>
        -->
      </el-col>
    </el-row>

    <KoiLoading></KoiLoading>
  </div>
</template>

<script lang="ts" setup>
import {Lock, Open, User} from "@element-plus/icons-vue";
// @ts-ignore
import {computed, onMounted, onUnmounted, reactive, ref} from "vue";
import type {FormInstance, FormRules} from "element-plus";
import {koiMsgError} from "@/utils/koi.ts";
import {getAssets, getLanguage} from "@/utils/index.ts";
import settings from "@/settings";
import KoiDark from "./components/KoiDark.vue";
import KoiLoading from "./components/KoiLoading.vue";
import KoiLanguage from "./components/KoiLanguage.vue";
import useGlobalStore from "@/stores/modules/global.ts";
import {getClient, pictureCode} from "@/api/system/user";
import useClientStore from "@/stores/modules/client.ts";
import useAuthStore from "@/stores/modules/auth.ts";

// 标题语言切换
const loginTitle = ref(settings.loginTitle);
loginTitle.value = computed(() => {
  return getLanguage(globalStore.language, settings.loginTitle, settings.loginEnTitle);
});

const globalStore = useGlobalStore();
const clientStore = useClientStore();
const authStore = useAuthStore();

/** 用户登录代码 */
const logo = getAssets("images/logo/logo.webp");
const bg = getAssets("images/login/bg.png");
const loginFormRef = ref<FormInstance>();
const loading = ref(false);

interface ILoginUser {
  username: string;
  password: string | number;
  captchaUuid: string;
  captchaCode: string | number;
  client_id: string | number;
  client_secret: string | number;
  redirect_uri: string | number;
  imageBase64: any
}

const loginForm = reactive<ILoginUser>({
  username: "",
  password: "",
  captchaUuid: "",
  captchaCode: "",
  client_id: "",
  client_secret: "",
  redirect_uri: "",
  imageBase64: ""
});

let loginRules: any = reactive<FormRules<ILoginUser>>({});
loginRules = computed(() => {
  if (globalStore.language === "en") {
    return reactive<FormRules<ILoginUser>>({
      username: [{required: true, message: "The user name cannot be empty", trigger: "blur"}],
      password: [{required: true, message: "The password cannot be empty", trigger: "blur"}],
      captchaCode: [{required: true, message: "The verification code cannot be empty", trigger: "blur"}]
    });
  } else {
    return reactive<FormRules<ILoginUser>>({
      username: [{required: true, message: "用户名不能为空", trigger: "blur"}],
      password: [{required: true, message: "密码不能为空", trigger: "blur"}],
      captchaCode: [{required: true, message: "验证码不能为空", trigger: "blur"}]
    });
  }
});

// 进入登录页面后请求
onMounted(() => {
  // 根据host获取当前登录所需要的client信息
  genClient();
  // 获取登录验证码
  genCaptcha();
  // 局部刷新定时器, 在页面停留几秒之后将重新获取验证码
  genCaptchaTimer();
});

//获取oauthClient信息
const genClient = async () => {
  // 使用URL API来获取域名
  const url = new URL(window.location.href);
  const params = {"domain": url.hostname}
  await getClient(params).then((res) => {
    //将请求到的client信息存放至localStorage中,为登录使用.
    loginForm.client_id = res.data.clientId
    loginForm.client_secret = res.data.clientSecret
    loginForm.redirect_uri = res.data.redirectUri
    clientStore.setClientInfo(res.data)
  });
}

/** 获取登录验证码 */
const genCaptcha = async () => {
  const res: any = await pictureCode("");
  if (!res) {
    return;
  }
  loginForm.captchaUuid = res.data.uuid;
  loginForm.imageBase64 = res.data.imageBase64;
};

const koiTimer = ref();
// 验证码定时器
const genCaptchaTimer = () => {
  koiTimer.value = setInterval(() => {
    // 执行刷新数据的方法
    genCaptcha();
  }, 345 * 1000);
};

onUnmounted(() => {
  // 清除局部刷新定时器
  clearInterval(koiTimer.value);
  koiTimer.value = null;
});

/** 单点登录 */
const oauthSsoLogin = () => {
  if (!loginFormRef.value) return;
  (loginFormRef.value as any).validate(async (valid: any, fields: any) => {
    if (valid) {
      loading.value = true;
      // 使用同步
      authStore.ssoLogin(loginForm);
      loading.value = false;
    } else {
      console.log("登录校验失败", fields);
      koiMsgError("校验失败，信息填写有误🌻");
    }
  });
};
</script>

<style lang="scss" scoped>
/** 备案号 */
.beianhao {
  position: absolute;
  bottom: 10px;
  left: auto;
  font-size: 12px;
  font-weight: bold;
}

.login-background {
  background: linear-gradient(155deg, #07070915 12%, var(--el-color-primary) 36%, #07070915 76%);
  filter: blur(100px);
}

.animate-float {
  animation: float 5s linear 0ms infinite;
}

@keyframes float {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
  100% {
    transform: translateY(0);
  }
}
</style>
