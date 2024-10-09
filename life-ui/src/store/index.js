import Vue from 'vue'
import Vuex from 'vuex'
import {getClient, oauth2AuthorizeCode, oauthLogin} from '@/api/login'

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
                //登录成功将直接请求
                // 使用windows.href直接请求接口
                oauth2AuthorizeCode().then((result) => {
                    console.log(result)
                })
            })
        },
    },
    modules: {}
})

export default store
