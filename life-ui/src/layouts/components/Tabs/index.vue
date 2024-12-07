<template>
  <el-tabs v-model="activeTab" type="card" class="koi-tabs" @tab-remove="removeTab" @tab-click="clickToggleTab" @contextmenu.prevent="handleTabsMenuParent($event)">
    <!-- :closable="true" 显示关闭图标 -->
    <el-tab-pane v-for="item in tabList" :key="item.path" :label="item.title" :name="item.path" :closable="item.closeIcon">
      <!-- 加载图标 -->
      <template #label>
        <div class="flex flex-justify-center flex-items-center select-none" @contextmenu.prevent="handleTabsMenuChildren(item.path, $event)">
          <KoiGlobalIcon class="m-r-2px" v-show="item.icon" :name="item.icon" size="16"></KoiGlobalIcon>
          <div>{{ getLanguage(globalStore.language, item?.title, item?.enName) }}</div>
        </div>
      </template>
    </el-tab-pane>
  </el-tabs>

  <div>
    <TabMenu ref="tabMenuRef"></TabMenu>
  </div>
</template>

<script setup lang="ts">
import TabMenu from "@/layouts/components/Tabs/components/TabMenu.vue";
// @ts-ignore
import Sortable from "sortablejs";
import { koiMsgWarning } from "@/utils/koi.ts";
import { TabsPaneContext } from "element-plus";
import { ref, watch, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { HOME_URL } from "@/config/index.ts";

import useTabsStore from "@/stores/modules/tabs.ts";
import useAuthStore from "@/stores/modules/auth.ts";
import { getLanguage } from "@/utils/index.ts";
import useGlobalStore from "@/stores/modules/global.ts";

const globalStore = useGlobalStore();

// 获取当前路由
const route = useRoute();
// 路由跳转
const router = useRouter();
// 获取选项卡仓库
const tabsStore = useTabsStore();
// 获取权限仓库
const authStore = useAuthStore();

/** 页面创建后，立即初始化选项卡 AND 拖拽函数 */
onMounted(() => {
  addTab(); // 添加选项卡[进入根页面，立即添加首页]
  setActiveTab(); // 设置激活选项卡[进入根页面，立即激活首页]
  initTabs(); // 进入根页面，初始化需要固定的页面
  tabsDrop(); // 初始化拖拽功能
});

/** 监听当前路由，路由path发生变化添加选项卡数据 */
watch(
  () => route.fullPath,
  () => {
    if (route.meta.isFull == "0") return;
    // 2、激活选中的选项卡
    setActiveTab();
    // 3、添加选项卡
    addTab();
  }
);

/** 1、初始化需要固定的 tabs[isAffix[配置固定tabs项]，在进入系统的时候，获取对应权限菜单数据，如果里面有固定tabs配置项，则进行添加] */
const initTabs = () => {
  authStore.menuList.forEach((item: any) => {
    if (item.meta.isAffix == "0" && item.meta.isHide == "1" && item.meta.isFull == "1") {
      const tabsParams = {
        icon: item.meta.icon,
        title: item.meta.title,
        enName: item.meta?.enName,
        path: item.path,
        name: item.name,
        closeIcon: false,
        isKeepAlive: item.meta.isKeepAlive
      };
      tabsStore.addTab(tabsParams);
    }
  });
};

/** 获取选项卡数据 */
const tabList = computed(() => {
  return tabsStore.getTabs;
});

/** 2、添加后激活选项卡 */
const activeTab = ref(route.fullPath);
const setActiveTab = () => {
  activeTab.value = route.fullPath;
};

/** 3、添加选项卡 */
const addTab = () => {
  // 解构路由数据
  const { meta, fullPath } = route;
  // 构造选项卡数据
  const tab = {
    icon: meta.icon,
    title: meta.title as string,
    enName: meta?.enName as string,
    path: fullPath,
    name: route.name as string,
    closeIcon: route.meta.isAffix == "1", // true则显示关闭图标
    isKeepAlive: route.meta.isKeepAlive
  };
  if (fullPath == HOME_URL) {
    // 如果是首页的话，就固定不可关闭。
    tab.closeIcon = false;
  }
  // 添加到选项卡仓库里面
  tabsStore.addTab(tab);
};

/** 4、移除选项卡 */
const removeTab = (fullPath: any) => {
  // 最后一个选项卡不被允许关闭
  const ObjectNumber = tabsStore.tabList.filter((item: any) => typeof item === "object").length;
  if (ObjectNumber == 1) {
    koiMsgWarning("到我的底线了，哼🌻");
    return;
  }
  tabsStore.removeTab(fullPath as string, fullPath == route.fullPath, "NULL");
};

/** 5、点击切换选项卡 */
const clickToggleTab = (tab: TabsPaneContext) => {
  const { props } = tab;
  // console.log(props.name); // 打印路由path
  // 将切换的选项卡进行添加路由操作
  router.push({ path: props.name as string });
};

/** 6、tabs 拖拽排序 */
const tabsDrop = () => {
  Sortable.create(document.querySelector(".el-tabs__nav") as HTMLElement, {
    draggable: ".el-tabs__item",
    animation: 300,
    // @ts-ignore
    onEnd({ newIndex, oldIndex }) {
      const tabsList = [...tabsStore.tabList];
      // 获取当前移动的索引的数据
      const currentRow = tabsList.splice(oldIndex as number, 1)[0];
      // 将 currentRow 元素插入到 tabsList 数组的指定位置 newIndex。0 是删除的元素数量，这里不需要删除任何元素
      tabsList.splice(newIndex as number, 0, currentRow);
      // 更新排序后的tabs仓库数据
      tabsStore.setTab(tabsList);
    }
  });
};

/** 7、右键菜单 */
const tabMenuRef = ref();
const handleTabsMenuParent = (value: any) => {
  if (tabMenuRef.value) {
    tabMenuRef.value.handleKoiMenuParent(value);
  } else {
    koiMsgWarning("右键获取属性失败，请刷新页面重试🌻");
  }
};

const handleTabsMenuChildren = (path: any, value: any) => {
  if (tabMenuRef.value) {
    tabMenuRef.value.handleKoiMenuChildren(path, value);
  } else {
    koiMsgWarning("右键获取属性失败，请刷新页面重试🌻");
  }
};
</script>

<style lang="scss" scoped>
/** tabs选项卡的css开始 */
.koi-tabs {
  border-bottom: 1px solid #d1d1da;
  @apply dark:border-#313233;
  // 色弱模式
  background-color: var(--el-bg-color);
}
:deep(.el-tabs__item:first-child) {
  margin-left: 8px;
}
:deep(.el-tabs__item:nth-child(n)) {
  // 选中/未选中边框
  border: 1px solid #d1d1da !important;
}
:deep(.el-tabs__item:not(:active)) {
  // 设置未选中的边框
  border: 1px solid #d1d1da;
}
:deep(.el-tabs__item) {
  height: 28px;
  margin-left: 6px;
  margin-top: 1px;
  font-size: 14px;
  font-weight: 500;
  color: #1F1F1F;
  @apply dark:text-#E0E0E0;
  border: 1px solid #d1d1da;
  border-radius: 4px;
  .is-top {
    border-bottom: none !important;
  }

  // 设置鼠标悬停时的样式
  &:hover {
    color: var(--el-color-primary);
    // 边框选中颜色
    border: 1px solid var(--el-color-primary) !important;
    // background: var(--el-color-primary-light-9);
  }

  // 设置鼠标选中的样式[可用来定制不同配色的主题]
  &.is-active {
    color: var(--el-color-primary);
    background: var(--el-color-primary-light-8);

    // 边框选中颜色
    border: 1px solid var(--el-color-primary) !important;
  }
}
:deep(.el-tabs__header) {
  border-top: 1px solid var(--el-color-info-light-7); // 选项卡边框实线
  border-bottom: 1px solid var(--el-color-info-light-7); // 选项卡边框实线
  margin: 0px;
}
:deep(.el-tabs__nav.is-top) {
  height: 32px; // 高度越高，可以调整tab卡距离底部的高度
  border: none; //  去除左侧tabs边框
  border-radius: 4px;
}

// 覆盖多余边框
:deep(.el-tabs__nav) {
  border: none !important;
}
:deep(.el-tabs__nav-prev) {
  // 标签页多了左侧图标
  line-height: 35px;
}
:deep(.el-tabs__nav-next) {
  // 标签页多了右侧图标
  line-height: 35px;
}
/** tabs选项卡的css结束 */
</style>
