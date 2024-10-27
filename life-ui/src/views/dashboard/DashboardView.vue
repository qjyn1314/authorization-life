<template>
  <div>
    <h2>登录成功后的首页</h2>
  </div>
</template>

<script>
import {setValueExpires,setValue} from '@/utils/cookie-util'

export default {
  name: 'DashboardView',
  components: {},
  created() {
    //VUE_APP_COOKIE_HOST
    // setValue("test", "test");
  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>
<style scoped>

</style>
