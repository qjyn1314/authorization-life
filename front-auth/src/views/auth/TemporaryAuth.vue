<template>
  <h2>我是临时授权页面, 将通过临时code, state获取accessToken等信息</h2>
  <h2>临时code:{{ this.$route.query.code }}</h2>
  <h2>state:{{ this.$route.query.state }}</h2>
</template>
<script>
import {oauth2TemporaryCodeStore} from "@/stores/modules/user-auth-code";
import {userAccessTokenStore} from "@/stores/modules/user-auth-token"
import {userInfoStore} from "@/stores/modules/user-auth-info";
import {genOauth2TokenByCode, getCurrentUser} from "@/api/login-api"

export default {
  name: "TemporaryAuth",
  created() {
    const oauth2CodeClient = oauth2TemporaryCodeStore();
    console.log("进入到了临时授权页面,client的信息是:", oauth2CodeClient)
    let queryParams = this.$route.query;
    console.log("进入到了临时授权页面,路径中的临时CODE参数是:", queryParams)

    if (!queryParams.code || !queryParams.state) {
      return;
    }
    let accessTokenByCode = {
      grant_type: 'authorization_code',
      redirect_uri: oauth2CodeClient.redirectUri,
      client_id: oauth2CodeClient.clientId,
      client_secret: oauth2CodeClient.clientSecret,
      code: queryParams.code,
      state: queryParams.state
    };
    console.log("进入到了临时授权页面,请求获取AccessToken的参数:", accessTokenByCode)
    debugger
    // 获取accessToken
    const useOauth2TokenStore = userAccessTokenStore();
    const useUserInfoStore = userInfoStore();
    genOauth2TokenByCode(accessTokenByCode).then(res => {
      console.log('accessTokenByCodeRes', res)
      useOauth2TokenStore.setAccessToken(res);
      // 在设置登录的Token成功之后, 直接请求当前登录用户的信息,并存储到 userInfoStore
      getCurrentUser().then(res => {
        console.log("getCurrentUser", res)
        useUserInfoStore.setUserInfo(res.data);
        debugger
        // 此处成功设置值后, 将跳转至首页
        window.location.href = "/index";
      })
    })

  }
}
</script>

<style>

</style>