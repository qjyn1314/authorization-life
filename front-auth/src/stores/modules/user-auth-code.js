import {defineStore} from "pinia";
import {AUTH_SERVER} from "@/utils/global-util";
// 登录用户在登录成功后, 携带client信息, 发起获取临时code的请求: /oauth2/authorize
export const oauth2TemporaryCodeStore = defineStore("oauth2TemporaryCodeStore", {
    // 开启数据持久化
    persist: {
        enabled: true, // true 表示开启持久化保存
        strategies: [
            {
                key: 'latest-data',
                storage: localStorage,
                // 每次状态变化时自动保存
            }
        ]
    },
    // 存储数据state
    state: () => ({
        domainName: "",
        clientId: "",
        clientSecret: "",
        grantTypes: "authorization_code",
        scopes: "",
        redirectUri: "",
        accessTokenTimeout: "",
        refreshTokenTimeout: "",
        additionalInformation: "",
        tenantId: "",
        params: "",
        authenticated: false,
        state: "",
    }),
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {
        setClient(clientInfo) {
            this.domainName = clientInfo.domainName;
            this.clientId = clientInfo.clientId;
            this.clientSecret = clientInfo.clientSecret;
            this.grantTypes = clientInfo.grantTypes;
            this.scopes = clientInfo.scopes;
            this.redirectUri = clientInfo.redirectUri;
            this.accessTokenTimeout = clientInfo.accessTokenTimeout;
            this.refreshTokenTimeout = clientInfo.refreshTokenTimeout;
            this.additionalInformation = clientInfo.additionalInformation;
            this.tenantId = clientInfo.tenantId;
            this.params = clientInfo.params;
            this.authenticated = null;
            this.state = "";
        },
        oauth2Authorize(authorizeInfo) {
            this.authenticated = authorizeInfo.authenticated;
            this.state = authorizeInfo.state;
            window.location.href = `${process.env.VUE_APP_PROXY_TARGET}${process.env.VUE_APP_BASE_API}${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${this.clientId}&scope=${this.scopes}&state=${authorizeInfo.state}&redirect_uri=${this.redirectUri}`
        },
    },
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getClientId: state => state.clientId,
        getClientSecret: state => state.clientSecret,
        getTenantId: state => state.tenantId,
    }
});