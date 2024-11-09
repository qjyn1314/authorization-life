<template>
  <div>
    <h2>用户的授权页面</h2>
    <h2>恭喜你，已成功实现第一步，获取到了临时授权码code。</h2>
    <h3>路径中的参数是：{{ this.$route.query }}</h3>
  </div>
</template>

<script>
import {useRouter} from 'vue-router'
import useAuthStore from "@/stores/modules/auth.ts";

export default {
  setup(props, context) {
    console.log(props)
    console.log(context)
    const router = useRouter();
    // 打印
    console.log('router:', router.currentRoute.value.query)
    console.log("已进入临时授权页面..")
    const authStore = useAuthStore();
    //将请求获取accessToken接口
    authStore.genAccessToken(router.currentRoute.value.query)
    return {}
  },
  created() {
  },
  mounted() {
  },
  computed: {},
  watch: {}
}
</script>
<style scoped>

</style>
