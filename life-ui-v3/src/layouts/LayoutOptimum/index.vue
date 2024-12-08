<template>
  <!-- 分栏布局 -->
  <el-container class="layout-container">
    <el-aside
      class="layout-aside transition-all"
      :style="{ width: !globalStore.isCollapse ? globalStore.menuWidth + 'px' : settings.columnMenuCollapseWidth }"
      v-if="subMenuList.length != 0"
    >
      <Logo :isCollapse="globalStore.isCollapse" :layout="globalStore.layout"></Logo>
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
          <ColumnSubMenu :menuList="subMenuList"></ColumnSubMenu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="layout-header">
        <div class="header">
          <div class="header-left">
            <!-- 左侧菜单展开和折叠图标 -->
            <Collapse></Collapse>
            <div class="layout-row m-l-12px">
              <el-scrollbar>
                <div class="flex flex-wrap">
                  <div
                    v-for="item in menuList"
                    :key="item.path"
                    class="left-row line-clamp-1"
                    :class="{
                      'is-active': columnActive === item.path || `/${columnActive.split('/')[1]}` === item.path
                    }"
                    @click="handleSubMenu(item)"
                  >
                    <KoiGlobalIcon v-if="item.meta.icon" :name="item.meta.icon" size="18"></KoiGlobalIcon>
                    <span class="title">{{ getLanguage(globalStore.language, item.meta?.title, item.meta?.enName) }}</span>
                  </div>
                </div>
              </el-scrollbar>
            </div>
          </div>
          <!-- 工具栏 -->
          <Toolbar></Toolbar>
        </div>
      </el-header>
      <!-- 路由页面 -->
      <Main></Main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import settings from "@/settings.ts";
import Logo from "@/layouts/components/Logo/index.vue";
import Collapse from "@/layouts/components/Header/components/Collapse.vue";
import Toolbar from "@/layouts/components/Header/components/Toolbar.vue";
import ColumnSubMenu from "@/layouts/components/Menu/ColumnSubMenu.vue";
import Main from "@/layouts/components/Main/index.vue";
import { ref, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import useAuthStore from "@/stores/modules/auth.ts";
import { getLanguage } from "@/utils/index.ts";
import useGlobalStore from "@/stores/modules/global.ts";

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const globalStore = useGlobalStore();

console.log("上左布局左侧动态路由", authStore.showMenuList);

// 动态绑定左侧菜单animate动画
const menuAnimate = ref(settings.menuAnimate);

/** 隐藏静态路由中isHide == '1'的数据 */
const menuList = computed(() => authStore.showMenuList.filter((item: any) => item.meta.isHide == "1"));

const columnActive = ref("");
const subMenuList = ref([]);

watch(
  () => [menuList, route],
  () => {
    // 当前菜单没有数据直接 return
    if (!menuList.value.length) return;
    columnActive.value = route.path;
    const menuItem = menuList.value.filter((item: any) => {
      // 刷新浏览器，一级路由就会变成点击的二级路由，所以需要加上`/${route.path.split("/")[1]}` 进行获取，否则就没有默认选中的颜色。
      return route.path === item.path || `/${route.path.split("/")[1]}` === item.path;
    });
    // 若获取的路由没有子菜单，则赋值为空。
    if (!menuItem[0].children?.length) return (subMenuList.value = []);
    // 若有子菜单则赋值给子菜单变量。
    subMenuList.value = menuItem[0].children;
  },
  {
    deep: true,
    immediate: true
  }
);

/** 点击加载子菜单数据 */
const handleSubMenu = (item: any) => {
  columnActive.value = item.path;
  if (item.children?.length) {
    // 该一级菜单，若是有子菜单，就会给第二个分栏菜单赋值。
    // router.push(item.path); // 加这个，点击最左侧菜单会重定向第一个子菜单。
    subMenuList.value = item.children;
    return;
  }
  // 若是没有子菜单，则给子菜单变量赋值为空，并且跳转路由。例如：首页。
  subMenuList.value = [];
  router.push(item.path);
};

const activeMenu = computed(() => (route.meta.activeMenu ? route.meta.activeMenu : route.path) as string);

const rowMenuColor = computed(() => {
  if (globalStore.asideInverted && globalStore.headerInverted) return "white";
  if (globalStore.asideInverted) return "black";
  if (globalStore.headerInverted) return "white";
});
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  justify-content: space-between;
  height: $aside-header-height;
  .header-left {
    display: flex;
    align-items: center;
    overflow: hidden;
    white-space: nowrap;
  }
}

.layout-row {
  display: flex;
  height: 100%;
  user-select: none;
  background-color: var(--el-header-bg-color);

  .left-row {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    max-width: 220px;
    height: 100%;
    padding: 6px 4px 6px 4px;
    margin-left: 4px;
    margin-top: 2px;
    color: #000;
    @apply dark:text-#E5E3EA;
    cursor: pointer;
    border: 1px solid transparent;
    color: v-bind(rowMenuColor);
    .el-icon {
      font-size: 18px;
    }
    .title {
      margin-top: 8px;
      font-size: 12px;
      font-weight: $aside-menu-font-weight;
      line-height: 14px;
      text-align: center;
      letter-spacing: 2px;
    }
    &:hover {
      // color: var(--el-color-primary);
      color: var(--el-header-optimum-hover-color);
      // background: var(--el-color-primary-light-9);
      // background: var(--el-menu-hover-bg-color);
      background: var(--el-header-optimum-hover-bg-color);
      // border: 2px dashed var(--el-color-primary);
      border: 1px solid var(--el-header-optimum-border-color);
      border-radius: 4px;
    }
    &.is-active {
      // color: var(--el-color-primary);
      color: var(--el-header-optimum-active-color);
      // background: var(--el-color-primary-light-8);
      // background: var(--el-menu-active-bg-color);
      background: var(--el-header-optimum-active-bg-color);
      // border: 2px dashed var(--el-color-primary);
      border: 1px solid var(--el-header-optimum-border-color);
      border-radius: 4px;
    }
  }
}

.layout-container {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  .layout-aside {
    z-index: $layout-aside-z-index; // 左侧菜单层级
    padding-right: $column-menu-padding-right; // 左侧布局右边距[用于悬浮和选中更明显]
    padding-left: $column-menu-padding-left; // 左侧布局左边距[用于悬浮和选中更明显]
    background-color: var(--el-menu-bg-color);
    border-right: none;
    box-shadow: $aside-menu-box-shadow; // 双栏左侧布局菜单右边框阴影
  }
  .layout-header {
    height: $aside-header-height;
    background-color: var(--el-header-bg-color);
  }
  .layout-main {
    box-sizing: border-box;
    padding: 0;
    overflow-x: hidden;
    background-color: var(--el-bg-color);
  }
}
.layout-scrollbar {
  width: 100%;
  height: calc(100vh - $aside-header-height);
}

/** 去除菜单右侧边框 */
.el-menu {
  border-right: none;
}
</style>
