<template>
  <!-- 设置 -->
  <div
    class="hover:bg-[rgba(0,0,0,0.06)] koi-icon w-32px h-100% flex flex-justify-center flex-items-center m-r-12px"
    @click="handleThemeDialog"
  >
    <el-tooltip :content="$t('header.settings')">
      <el-icon class="koi-icon" :size="20">
        <Setting />
      </el-icon>
    </el-tooltip>
  </div>
</template>

<script setup lang="ts">
import mittBus from "@/utils/mittBus.ts";

/** 打开主题配置 */
const handleThemeDialog = () => {
   mittBus.emit("handleThemeConfig");
};
</script>

<style lang="scss" scoped></style>
