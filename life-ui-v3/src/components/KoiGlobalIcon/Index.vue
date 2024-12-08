<template>
  <el-icon :size="props.size">
    <component v-if="!props.name.startsWith(SVG_PREFIX)" :is="props.name"></component>
    <component v-if="props.name.startsWith(SVG_PREFIX)" is="KoiSvgIcon" :name="props.name" :width="props.size" :height="props.size"></component>
  </el-icon>
</template>

<script setup lang="ts">
import { SVG_PREFIX } from '@/config/index.ts';

// 定义参数的类型
interface IGlobalIconProps {
  name?: string;
  size?: number | string;
}

// 子组件接收父组件的值
// withDefaults：设置默认值  defineProps：接收父组件的参数
const props = withDefaults(defineProps<IGlobalIconProps>(), {
  name: "Sunny",
  size: "18"
});
</script>

<style lang="scss" scoped>

</style>
