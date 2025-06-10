<template>
  <h2>我是临时授权页面用于获取临时code 和 state </h2>
</template>
<script>
import {oauth2TemporaryCodeStore} from "@/stores/modules/user-auth-code";
import {userAccessTokenStore} from "@/stores/modules/user-auth-token"
import {userInfoStore} from "@/stores/modules/user-auth-token";
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
    const oauth2TokenStore = userAccessTokenStore();
    genOauth2TokenByCode(accessTokenByCode).then(res => {
      console.log('accessTokenByCodeRes', res)
      oauth2TokenStore.setAccessToken(res);
      // 在设置登录的Token成功之后, 直接请求当前登录用户的信息,并存储到 userInfoStore
      getCurrentUser().then(res => {
        console.log("getCurrentUser", res)
        debugger
        userInfoStore.setUserInfo(res.data);
        let userInfo = userInfoStore.getUserInfo();
        debugger
        console.log("userInfo", userInfo)
      })
    })

  }
}
</script>

<style>

</style>