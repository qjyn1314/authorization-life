import Vue from 'vue'
import Vuex from 'vuex'
import {getClient, oauthLogin} from '@/api/login'
import {AUTH_SERVER} from '@/api/severApi'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        clientInfo: {
            domainName: "",
            clientId: "",
            clientSecret: "",
            grantTypes: "",
            scopes: "",
            redirectUri: "",
            accessTokenTimeout: 86400,
            refreshTokenTimeout: 108000,
            additionalInformation: "",
            tenantId: "0"
        }
    },
    getters: {},
    actions: {
        securityLogin(context, value) {
            //请求并获取client信息
            getClient({domain: new URL(window.location.href).hostname})
                .then((res) => {
                    if (!res) {
                        return;
                    }
                    context.state.clientInfo = res.data;
                    context.commit('SSO_LOGIN', value)
                })
        }
    },
    mutations: {
        SSO_LOGIN(state, value) {
            console.log('mutations->state', state)
            console.log('mutations->value', value)
            //请求登录接口
            oauthLogin(value).then((res) => {
                if (!res) {
                    return
                }
                // 登录成功将直接请求
                // 使用windows.href直接请求接口
                console.log("授权码模式...")
                console.log("请求授权确认...", `${process.env.VUE_APP_PROXY_TARGET}/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`)
                window.location.href = `${process.env.VUE_APP_PROXY_TARGET}/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`
                // oauth2AuthorizeCode().then((result) => {
                //     console.log(result)
                // })
            })
        },
    },
    modules: {}
})

export default store
