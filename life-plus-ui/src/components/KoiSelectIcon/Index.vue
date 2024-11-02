<template>
  <el-popover
    placement="bottom-start"
    v-model:visible="visible"
    :disabled="disabled"
    :width="widthPopover"
    trigger="click"
  >
    <template #reference>
      <el-input
        :style="{ width: width ? width + 'px' : '100%' }"
        v-model="inputValue"
        placeholder="请选择图标"
        :disabled="disabled"
        :autofocus="false"
        clearable
      >
        <template #append>
          <KoiGlobalIcon :name="modelValue" v-if="modelValue" />
          <span v-else></span>
        </template>
      </el-input>
    </template>
    <template #default>
      <el-tabs>
        <el-tab-pane :label="item.name" v-for="(item, index) in iconTabs" :key="index" class="h-70">
          <el-scrollbar>
            <div class="flex flex-wrap">
              <div v-for="(iconItem, iconIndex) in item.icons" :key="iconIndex" class="m-1">
                <el-button @click="handleIconSelect(iconItem)">
                  <KoiGlobalIcon :name="iconItem" size="18" />
                </el-button>
              </div>
            </div>
          </el-scrollbar>
        </el-tab-pane>
      </el-tabs>
    </template>
  </el-popover>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue';
// 引入ElementPlus所有图标
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

interface ISelectIconProps {
  modelValue?: string;
  width?: string;
  widthPopover?: string;
  disabled?: boolean;
}
const props = withDefaults(defineProps<ISelectIconProps>(), {
  widthPopover: '380',
  modelValue: '',
  disabled: false
});

// ElementPlus图标库
const elementPlusIconList = [] as string[];

// 获取ElementPlus所有图标
const getElementPlusIcon = () => {
  for (const name in ElementPlusIconsVue) {
    elementPlusIconList.push(name);
  }
  return elementPlusIconList;
};

const localIconList = [] as string[];
const modules = import.meta.glob("../../assets/icons/*.svg");


// 获取本地所有图标
const getLocalIcon = () => {
  // 获取本地所有图标
  for (const path in modules) {
    const icon = path.split("assets/icons/")[1].split(".svg")[0];
    localIconList.push(icon);
  }
  return localIconList;
};

const emit = defineEmits(['update:modelValue']);
const visible = ref(false);
const inputValue = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    !value && emit('update:modelValue', value);
  }
});

const iconTabs = [
  {
    name: "ele图标",
    icons: getElementPlusIcon()
  },
  {
    name: '本地图标',
    icons: getLocalIcon()
  }
];
const handleIconSelect = (iconItem: string) => {
  visible.value = false;
  emit('update:modelValue', iconItem);
};
</script>

<style lang="scss" scoped></style>
