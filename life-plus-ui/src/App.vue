<template>
  <el-config-provider :locale="locale" :size="dimension">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup lang="ts">
import { onMounted, nextTick, computed } from "vue";
import { useI18n } from "vue-i18n";
import { getBrowserLang } from "@/utils/index.ts";
import { useTheme } from "@/utils/theme.ts";
import en from "element-plus/es/locale/lang/en";
import zhCn from "element-plus/es/locale/lang/zh-cn";
import { autoRefresh } from "@/utils/autoUpdate.ts";

import useGlobalStore from "@/stores/modules/global.ts";
const globalStore = useGlobalStore();

const dimension = computed(() => globalStore.dimension);
const { initThemeConfig } = useTheme();

// 初始化语言
const i18n = useI18n();
onMounted(() => {
  // 初始化主题配置
  handleThemeConfig();
  // 初始化语言配置
  handleI18nConfig();
  // 自动检测更新
  handleAutoUpdate();
  // 开发环境打印项目名称
  console.log(
    `%c 命运迷雾 %c V1.0.0 `,
    "padding: 2px 1px; border-radius: 3px 0 0 3px; color: #fff; background: #6169FF; font-weight: bold;",
    "padding: 2px 1px; border-radius: 0 3px 3px 0; color: #fff; background: #42c02e; font-weight: bold;"
  );
});

// 语言配置
const locale = computed(() => {
  if (globalStore.language == "zh") return zhCn;
  if (globalStore.language == "en") return en;
  return getBrowserLang() == "zh" ? zhCn : en;
});

// 初始化语言配置
const handleI18nConfig = () => {
  const language = globalStore.language ?? getBrowserLang();
  i18n.locale.value = language;
  globalStore.setGlobalState("language", language);
};

// 初始化主题配置
const handleThemeConfig = () => {
  nextTick(() => {
    initThemeConfig();
  });
};

// 自动检测更新
const handleAutoUpdate = () => {
  nextTick(() => {
    if (import.meta.env.VITE_ENV === "production") autoRefresh();
  });
};
</script>

<style scoped></style>
