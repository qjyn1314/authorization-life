<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <Logo :layout="globalStore.layout"></Logo>
      <Header class="header"></Header>
    </el-header>
    <el-container class="layout-container-aside">
      <el-aside
        class="layout-aside transition-all"
        :style="{ width: !globalStore.isCollapse ? globalStore.menuWidth + 'px' : settings.asideMenuCollapseWidth }"
      >
        <el-scrollbar class="layout-scrollbar">
          <!-- :unique-opened="true" 子菜单不能同时展开 -->
          <el-menu
            :default-active="activeMenu"
            :collapse="globalStore.isCollapse"
            :collapse-transition="false"
            :uniqueOpened="globalStore.uniqueOpened"
            :router="false"
            :class="menuAnimate"
          >
            <AsideSubMenu :menuList="menuList"></AsideSubMenu>
          </el-menu>
        </el-scrollbar>
      </el-aside>
      <el-container class="flex flex-col">
        <!-- 路由页面 -->
        <Main></Main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import settings from "@/settings.ts";
import Logo from "@/layouts/components/Logo/index.vue";
import Header from "@/layouts/components/Header/index.vue";
import AsideSubMenu from "@/layouts/components/Menu/AsideSubMenu.vue";
import Main from "@/layouts/components/Main/index.vue";
import { ref, computed } from "vue";
import { useRoute } from "vue-router";
import useAuthStore from "@/stores/modules/auth.ts";
import useGlobalStore from "@/stores/modules/global.ts";

const route = useRoute();
const authStore = useAuthStore();
const globalStore = useGlobalStore();

console.log("左侧动态路由", authStore.showMenuList);
// 动态绑定左侧菜单animate动画
const menuAnimate = ref(settings.menuAnimate);
const menuList = computed(() => authStore.showMenuList);
const activeMenu = computed(() => (route.meta.activeMenu ? route.meta.activeMenu : route.path) as string);
// const menuHoverCollapse = ref(settings.asideMenuHoverCollapse);
</script>

<style lang="scss" scoped>
.layout-container {
  width: 100vw;
  height: 100vh;
  .layout-container-aside {
    overflow: hidden;
    .layout-aside {
      z-index: $layout-aside-z-index; // 左侧菜单层级
      padding-right: $aside-menu-padding-right; // 左侧布局右边距[用于悬浮和选中更明显]
      padding-left: $aside-menu-padding-left; // 左侧布局左边距[用于悬浮和选中更明显]
      border-right: none;
      box-shadow: $aside-menu-box-shadow; // 左侧布局右边框阴影
      background-color: var(--el-menu-bg-color);
    }
  }
  .layout-header {
    display: flex;
    height: $aside-header-height;
    overflow: hidden;
    background-color: var(--el-header-bg-color);
    .header {
      flex: 1; // 剩余空间全占满
    }
  }
}
.layout-scrollbar {
  width: 100%;
  height: calc(100vh - $aside-header-height);
}

// 去除菜单右侧边框
.el-menu {
  border-right: none;
}
</style>

<style lang="scss">
/** 菜单悬浮折叠宽度 */
.el-menu--collapse {
  width: calc(var(--el-menu-icon-width) + var(--el-menu-base-level-padding)) !important;
}
</style>
