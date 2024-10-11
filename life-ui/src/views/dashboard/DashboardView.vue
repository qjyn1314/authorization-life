<template>
  <div>
    <h2>登录成功后的首页</h2>
    <Temporary>我是临时授权组件, 用于将临时code转换为accessToken</Temporary>
  </div>
</template>

<script>
import Temporary from '@/components/Temporary.vue'

export default {
  name: 'DashboardView',
  components: {
    Temporary
  },
  created() {
  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>
<style scoped>

</style>
