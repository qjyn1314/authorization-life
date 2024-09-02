<template>
  <content-layout :showSearch="true" v-loading="loading">
    <template slot="title">系统首页</template>
    <template slot="body">
      <h2>恭喜你，已成功实现第一步，获取到了授权码code。</h2>
      <h3>路径中的参数是：{{ this.$route.query }}</h3>
      <hr/>
      <hr/>
      <h2>接下来我们将实现第二步，通过这个临时授权码code获取accesstoken。</h2>
      <hr/>
      <hr/>
      <h2>请求路径是：https://www.authorization.life/auth-life/oauth2/token</h2>
      <hr/>
      <h2>获取的accessToken对象传参：{{ tokenByCode }}</h2>
      <hr/>
      <el-button type="primary" @click="getAccessToken">点击请求: /oauth2/token</el-button>
      <h3>请求成功后，获取的accessToken对象是：{{ accessToken }}</h3>
      <h2>恭喜你，已成功实现第二步，通过授权码code获取到了accesstoken的所有信息，登录成功了，可以将此accesstoken信息保存至
        cookie 、localStorage，为系统后续而做请求使用。</h2>
      <hr/>
      <hr/>
      <el-button type="success" @click="getUserInfo">点击通过AccessToken获取用户信息-自定义接口: /oauth/self-user
      </el-button>
      <hr/>
      <h3>{{ userInfo }}</h3>
      <hr/>
      <hr/>
      <el-button type="danger" @click="logout">点击按钮退出登录: /oauth/logout->配置了退出登录处理器</el-button>
      <hr/>
      退出登录时将redis缓存中的信息,会话中的信息,cookie中的信息删除, 并重定向到登录页.
      <hr/>
      <hr/>
      <hr/>
      <hr/>
      <hr/>
    </template>
  </content-layout>
</template>

<script>
import {CODE_ACCESS_TOKEN} from '@/api/severApi'
import {ContentLayout} from '@/components'
import {getOauth2TokenByCode, oauthLogout,refreshTokenByAccessToken} from '@/api/login'
import {getUserSelf} from '@/api/common'
import {getCookie, setCookie} from '@/utils'

export default {
  name: 'temporary',
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
      accessToken: {
        access_token: '',
        token_type: '',
        refresh_token: '',
      },
      userInfo: {}
    }
  },
  computed: {},
  mounted() {
    // 在此处需要获取地址栏中的相关参数：code，然后根据code获取accessToken
    this.tokenByCode.code = this.$route.query.code;
    this.tokenByCode.state = this.$route.query.state;
  },
  methods: {
    getAccessToken() {
      console.log("开始请求 oauth/token 接口，参数信息是：")
      console.log(this.tokenByCode)
      getOauth2TokenByCode(this.tokenByCode).then(result => {
        this.accessToken = result.data;
        console.log("获取的accessToken对象信息是：" + result);
        //将accessToken缓存到当前二级域名的cookie中，开始通过accessToken获取当前登录用户的信息
        setCookie('accessToken', this.accessToken.access_token)
        setCookie('tokenType', this.accessToken.token_type)
        setCookie('refreshToken', this.accessToken.refresh_token)
        //一旦将access_token 存储到cookie中之后将跳转到正确的路径中。
      })
    },
    getUserInfo() {
      getUserSelf().then(result => {
        this.userInfo = result.data;
      })
    },
    logout() {
      let accessToken = getCookie("accessToken");
      oauthLogout({"token":accessToken}).then(result => {
        console.log(result);
      });
    },
    refreshAccessToken(){
      let refreshToken = getCookie("refreshToken");
      // 请求过程中, 在转换
      let params = {
        "grant_type":"refresh_token",
        "refresh_token": refreshToken,
        "scope": "TENANT"
      };
      refreshTokenByAccessToken(params).then(res => {
        console.log(res);
      });
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
