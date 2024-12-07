<template>
  <div>
    <template v-for="item in tagOptions">
      <el-tag
        v-if="value != null && value.toString() === item.dictValue.toString()"
        :disable-transitions="true"
        :key="item.dictLabel"
        :type="item.dictTag"
        :index="item.dictLabel + item.dictValue.toString()"
        :effect="item.dictColor.length === 0 ? effect : 'dark'"
        :size="size"
        :style="{
            'border-color': item.dictColor,
            background: item.dictColor
          }"
      >
        {{ item.dictLabel }}
      </el-tag>
    </template>
  </div>
</template>

<script setup lang="ts">
// 定义参数的类型
interface ITagProps {
  tagOptions?: any;
  value?: any;
  size?: any;
  effect?: any;
}

// 子组件接收父组件的值
// withDefaults：设置默认值  defineProps：接收父组件的参数
withDefaults(defineProps<ITagProps>(), {
  tagOptions: [],
  value: "",
  size: "default",
  effect: "light"
});
</script>

<style lang="scss" scoped>
.el-tag {
  padding: 0 10px;
}
</style>
