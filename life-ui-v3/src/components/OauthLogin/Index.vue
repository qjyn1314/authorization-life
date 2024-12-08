<template>
  <div>
    <!--    <h2>用户的临时授权页面</h2>-->
    <!--    <h2>恭喜你，已成功实现第一步，获取到了临时授权码code。</h2>-->
    <!--    <h3>路径中的参数是：{{ this.$route.query }}</h3>-->
  </div>
</template>

<script>
import {useRouter} from 'vue-router'
import useOauthStore from "@/stores/modules/oauth2.ts";

export default {
  //临时授权码模式进行获取临时code页面
  name: "OauthLogin",
  setup(props, context) {
    const router = useRouter();
    const oauthStore = useOauthStore();
    //将请求获取accessToken接口
    oauthStore.genAccessToken(router.currentRoute.value.query)
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
