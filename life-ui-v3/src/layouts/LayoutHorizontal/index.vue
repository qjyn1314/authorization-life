<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <div class="flex flex-items-center">
        <Logo :layout="globalStore.layout"></Logo>
        <el-scrollbar>
          <el-menu mode="horizontal" :default-active="activeMenu" :router="false" :class="menuAnimate">
            <HorizontalSubMenu :menuList="menuList" />
          </el-menu>
        </el-scrollbar>
      </div>

      <Toolbar></Toolbar>
    </el-header>

    <el-main class="layout-main">
      <Main></Main>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import settings from "@/settings.ts";
import Logo from "@/layouts/components/Logo/index.vue";
import Toolbar from "@/layouts/components/Header/components/Toolbar.vue";
import HorizontalSubMenu from "@/layouts/components/Menu/HorizontalSubMenu.vue";
import Main from "@/layouts/components/Main/index.vue";
import { ref, computed } from "vue";
import { useRoute } from "vue-router";
import useAuthStore from "@/stores/modules/auth.ts";
import useGlobalStore from "@/stores/modules/global.ts";

const route = useRoute();
const authStore = useAuthStore();
const globalStore = useGlobalStore();

console.log("横向布局左侧动态路由", authStore.showMenuList);

// 动态绑定左侧菜单animate动画
const menuAnimate = ref(settings.menuAnimate);
const menuList = computed(() => authStore.showMenuList);

const activeMenu = computed(() => (route.meta.activeMenu ? route.meta.activeMenu : route.path) as string);
</script>

<style lang="scss" scoped>
.layout-container {
  width: 100vw;
  height: 100vh;
  :deep(.layout-header) {
    box-sizing: border-box;
    padding: 0 20px 0 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: $aside-header-height;
    background-color: var(--el-header-bg-color);
    user-select: none;
    .el-menu {
      display: flex;
      flex-wrap: wrap;
      border-bottom: none;
      .el-menu-item.is-active {
        color: #ffffff !important;
      }
      .el-sub-menu.is-active {
        background-color: var(--el-color-primary);
      }
      .is-active {
        background-color: var(--el-color-primary);
        border-bottom-color: var(--el-color-primary);
        &::before {
          width: 0;
        }
        .el-sub-menu__title {
          color: #ffffff !important;
          background-color: var(--el-color-primary);
          border-bottom-color: var(--el-color-primary);
          i {
            color: #ffffff !important;
          }
        }
      }
    }
  }
  .layout-main {
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    padding: 0 15px 0 0;
    overflow-x: hidden;
    background-color: var(--el-bg-color);
  }
}
</style>
