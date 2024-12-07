<template>
  <!-- 全屏 -->
  <div class="hover:bg-[rgba(0,0,0,0.06)] koi-icon w-32px h-100% flex flex-justify-center flex-items-center" @click="toggle">
    <el-tooltip :content="globalStore.isFullScreen === false ? $t('header.fullScreen') : $t('header.exitFullScreen')">
      <el-icon class="koi-icon" :size="18">
        <FullScreen v-if="!globalStore.isFullScreen" />
        <CloseBold v-else />
      </el-icon>
    </el-tooltip>
  </div>
</template>

<script setup lang="ts">
import { useFullscreen } from "@vueuse/core";
import { watch } from "vue";
import useGlobalStore from "@/stores/modules/global.ts";

const globalStore = useGlobalStore();
// @vueuse/core 处理是否全屏
const { isFullscreen, toggle } = useFullscreen();

watch(isFullscreen, () => {
  if (isFullscreen.value) {
    globalStore.setGlobalState("isFullScreen", true);
  } else {
    globalStore.setGlobalState("isFullScreen", false);
  }
});
</script>

<style lang="scss" scoped></style>
