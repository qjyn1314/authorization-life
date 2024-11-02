<template>
  <div class="koi-logo flex flex-items-center p-l-5px" v-show="isShow">
    <div class="rounded-full" :style="{ width: logoSize, height: logoSize }" v-if="props.layout !== 'classic' && props.layout !== 'horizontal'">
      <el-image
        :src="logoUrl"
        fit="cover"
        class="w-100% h-100% rounded-full"
      >
        <template #error>
          <el-icon class="w-100% h-100% rounded-full c-[--el-color-primary]" :size="34">
            <CircleCloseFilled />
          </el-icon>
        </template>
      </el-image>
    </div>
    <div class="m-l--14px rounded-full" :style="{ width: logoSize, height: logoSize }" v-if="props.layout === 'classic'">
      <el-image
        :src="logoUrl"
        fit="cover"
        class="w-100% h-100% rounded-full"
      >
        <template #error>
          <el-icon class="w-100% h-100% rounded-full c-[--el-color-primary]" :size="34">
            <CircleCloseFilled />
          </el-icon>
        </template>
      </el-image>
    </div>
    <div class="m-l-6px rounded-full" :style="{ width: logoSize, height: logoSize }" v-if="props.layout === 'horizontal'">
      <el-image
        :src="logoUrl"
        fit="cover"
        class="w-100% h-100% rounded-full"
      >
        <template #error>
          <el-icon class="w-100% h-100% rounded-full c-[--el-color-primary]" :size="34">
            <CircleCloseFilled />
          </el-icon>
        </template>
      </el-image>
    </div>
    <div class="chroma-text w-160px m-l-10px font-bold truncate tracking-1px" :style="{ 'font-size': titleSize }" :class="titleAnimate" v-text="title" v-show="!props.isCollapse"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import settings from "@/settings";
import { getAssets } from "@/utils/index.ts";
import { getLanguage } from "@/utils/index.ts";
import useGlobalStore from "@/stores/modules/global.ts";

const globalStore = useGlobalStore();

// 接收父组件传递的参数
const props = defineProps({
  isCollapse: {
    require: false, // true显示，false隐藏
    type: Boolean
  },
  layout: {
    require: "vertical", // 布局模式 (纵向：vertical | 分栏：columns | 经典：classic | 上左：optimum | 横向：horizontal )
    type: String
  }
});

const title = ref(settings.logoTitle);
const titleSize = ref(`${settings.loginTitleSize}px`);
const logoUrl = ref(`${getAssets(settings.logoUrl)}`);
const isShow = ref(settings.logoShow);
const logoSize = ref(settings.logoSize);
const titleAnimate = ref(settings.logoTitleAnimate);

// 标题语言切换
title.value = computed(() => {
  return getLanguage(globalStore.language, settings.logoTitle, settings.logoEnTitle);
});
</script>

<style lang="scss" scoped>
.koi-logo {
  height: $aside-header-height;
  line-height: $aside-header-height;
}
</style>
