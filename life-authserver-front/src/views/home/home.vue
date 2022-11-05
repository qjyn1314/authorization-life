<template>
  <content-layout :showSearch="true" v-loading="loading">
    <template slot="title">系统首页</template>
    <template slot="body">
      <h2>恭喜你，已成功实现第一步，获取到了授权码code。</h2>
      <h3>路径中的参数是：{{ this.$route.query }}</h3>
      <h2>接下来我们将实现第二步，通过这个临时授权码code获取accesstoken。</h2>
      <h4>获取的accessToken对象传参：{{ tokenByCode }}</h4>
      <h2>请求路径是：https://www.authorization.life/auth-life/oauth2/token</h2>
      <h4>请求成功后，获取的accessToken对象是：{{ accessToken }}</h4>
      <h2>恭喜你，已成功实现第二步，通过授权码code获取到了accesstoken的所有信息，登录成功了，可以将此accesstoken信息保存至 cookie 、localStorage，为系统后续而做请求使用。</h2>
    </template>
  </content-layout>
</template>

<script>
import {CODE_ACCESS_TOKEN} from '@/api/severApi'
import {ContentLayout} from '@/components'
import {getOauth2TokenByCode} from '@/api/login'

export default {
  name: 'home',
  components: {
    ContentLayout,
  },
  data() {
    return {
      loading: false,
      tokenByCode: {
        grant_type: CODE_ACCESS_TOKEN.grant_type,
        code: '',
        state: '',
        redirect_uri: CODE_ACCESS_TOKEN.redirect_uri,
        client_id: CODE_ACCESS_TOKEN.client_id,
        client_secret: CODE_ACCESS_TOKEN.client_secret
      },
      accessToken: {}
    }
  },
  computed: {},
  mounted() {
    // 在此处需要获取地址栏中的相关参数：code，然后根据code获取accessToken
    this.tokenByCode.code = this.$route.query.code;
    this.tokenByCode.state = this.$route.query.state;
    console.log("开始请求 oauth/token 接口，参数信息是：")
    console.log(this.tokenByCode)
    getOauth2TokenByCode(this.tokenByCode).then(res => {
      this.accessToken = res.data;
      console.log("获取的accessToken对象信息是："+res);
      //开始通过accessToken获取当前登录用户的信息

    })
  },
  methods: {}
}
</script>
<style lang="scss" scoped>
</style>
