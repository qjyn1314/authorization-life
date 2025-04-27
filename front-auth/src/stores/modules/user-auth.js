// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
// 对外暴露方法
export const useOauth2Store = defineStore("useOauth2Store", {
    // 存储数据state
    state: () => {
        return {
            clientId: "",
            clientSecret: "",
            redirectUrl: "",
            redirectUri: ""
        };
    },
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {
        userOauth2Token(loginForm, redirectUrl) {
            this.clientId = loginForm.client_id;
            this.clientSecret = loginForm.client_secret;
            this.redirectUri = loginForm.redirect_uri;
            this.redirectUrl = redirectUrl;
            // 在此处直接进行请求 this.redirectUri
            window.location.href = this.redirectUri;
        },
    },
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getClientId: state => state.clientId,
        getClientSecret: state => state.clientSecret,
        getRedirectUri: state => state.redirectUri,
        getRedirectUrl: state => state.redirectUrl,
    }
});