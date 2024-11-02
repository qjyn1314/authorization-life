<template>
  <el-tooltip placement="left" :content="$t('header.componentSize')">
    <div class="hover:bg-[rgba(0,0,0,0.06)] koi-icon w-32px h-100% flex flex-justify-center flex-items-center">
      <el-dropdown @command="handleDimension">
        <el-icon class="koi-icon p-b-2px" :size="22"><ElementPlus /></el-icon>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item
              v-for="item in dimensionList"
              :key="item.value"
              :command="item.value"
              :disabled="dimension === item.value"
            >
              {{ item.label }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-tooltip>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from "vue";
import useGlobalStore from "@/stores/modules/global.ts";
import { koiMsgSuccess } from "@/utils/koi.ts";

const globalStore = useGlobalStore();
const dimension = computed(() => globalStore.dimension);

const dimensionList = ref<any>([]);
onMounted(() => {
  handleSwitchLanguage();
});

const handleSwitchLanguage = () => {
  // 当 language 变化时，手动触发 dimensionList 的更新
  if (globalStore.language === "en") {
    dimensionList.value = [
      { label: "default", value: "default" },
      { label: "large", value: "large" },
      { label: "small", value: "small" }
    ];
  } else {
    dimensionList.value = [
      { label: "默认", value: "default" },
      { label: "大型", value: "large" },
      { label: "小型", value: "small" }
    ];
  }
};

// 监听 globalStore.language 的变化
watch(
  () => globalStore.language,
  () => {
    // 当 language 变化时，手动触发 dimensionList 的更新
    handleSwitchLanguage();
  }
);

const handleDimension = (item: string) => {
  if (dimension.value === item) return;
  globalStore.setDimension(item);
  koiMsgSuccess("配置成功🌻");
};
</script>

<style lang="scss" scoped></style>
