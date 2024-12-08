<template>
  <div>
    <template v-for="item in tagOptions">
      <el-tag
        v-if="value != null && value.toString() === item.valueCode.toString()"
        :disable-transitions="true"
        :key="item.valueCode"
        :index="item.valueCode + item.valueContent.toString()"
        :effect="item.valueCode.length === 0 ? effect : 'dark'"
      >
        {{ item.valueContent }}
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
