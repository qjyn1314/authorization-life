<template>
  <div>
    <h2>用户的授权页面</h2>
    <h2>恭喜你，已成功实现第一步，获取到了临时授权码code。</h2>
    <h3>路径中的参数是：{{ this.$route.query }}</h3>
  </div>
</template>

<script>
import store from "@/store";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Temporary',
  components: {},
  created() {
    console.log("需要拿到当前路径中的临时code参数.", this.$route.query)
    store.dispatch("ssoAuth/oauth2AccessTokenByCode", this.$route.query)
  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>
<style scoped>

</style>
