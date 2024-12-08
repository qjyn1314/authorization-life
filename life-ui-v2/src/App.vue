<template>
  <div id="app">
    <DashboardView>

    </DashboardView>
  </div>
</template>

<style>
#app {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
<script>
import {defineComponent} from "vue";
import DashboardView from "@/views/dashboard/DashboardView.vue";

export default defineComponent({
  components: {DashboardView}
})
</script>