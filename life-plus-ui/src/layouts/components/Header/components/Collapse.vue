<template>
  <div class="hover:bg-[rgba(0,0,0,0.06)] koi-icon w-36px h-36px rounded-md flex flex-justify-center flex-items-center" @click="changeCollapseIcon">
    <el-icon :size="20">
      <SvgIcon name="koi-menu-left" width="20px" height="20px" v-if="!globalStore.isCollapse"></SvgIcon>
      <SvgIcon name="koi-menu-right" width="20px" height="20px" v-if="globalStore.isCollapse"></SvgIcon>
    </el-icon>
  </div>
</template>

<script setup lang="ts">
import useGlobalStore from "@/stores/modules/global.ts";
const globalStore = useGlobalStore();
// 切换图标
const changeCollapseIcon = () => {
  // 定义图标切换变量(true-折叠，false-展开)
  globalStore.isCollapse = globalStore.setCollapse(globalStore.isCollapse);
};
</script>

<style lang="scss" scoped></style>
