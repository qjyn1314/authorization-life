import {getClient, getCurrentUser, getOauth2TokenByCode, oauthLogin} from '@/api/login'
import {AUTH_SERVER} from '@/api/severApi'
import {setToken, setValue} from '@/utils/cookie-util'

export default {
    name: 'ssoAuth',
    namespaced: true,
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
        },
        tokenByCode: {
            grant_type: '',
            code: '',
            state: '',
            redirect_uri: '',
            client_id: '',
            client_secret: ''
        },
        accessToken: {
            access_token: '',
            token_type: '',
            refresh_token: '',
        },
        userInfo: {}
    },
    getters: {},
    actions: {
        //点击登录按钮调用
        securityLogin(context, value) {
            //请求并获取client信息为登录使用
            getClient({domain: new URL(window.location.href).hostname}).then((res) => {
                if (!res) {
                    return;
                }
                context.state.clientInfo = res.data;
                //在获取到client的认证信息后请求登录接口
                context.commit('SSO_LOGIN', value)
            })
        },
        //在临时授权页面请求接口用于获取当前登录用户的信息并存储至cookie中和store.state中用于使用getters进行直接获取
        oauth2AccessTokenByCode(context, value) {
            //请求并获取client信息为获取accessToken使用
            getClient({domain: new URL(window.location.href).hostname}).then((res) => {
                if (!res) {
                    return;
                }
                context.state.clientInfo = res.data;
                //默认的client信息
                context.state.tokenByCode.grant_type = 'authorization_code'
                context.state.tokenByCode.redirect_uri = context.state.clientInfo.redirectUri
                context.state.tokenByCode.client_id = context.state.clientInfo.clientId
                context.state.tokenByCode.client_secret = context.state.clientInfo.clientSecret
                //路径中的临时code
                context.state.tokenByCode.code = value.code
                context.state.tokenByCode.state = value.state
                console.log("context.state.tokenByCode", context.state.tokenByCode)
                debugger
                getOauth2TokenByCode(context.state.tokenByCode).then((res) => {
                    if (!res) {
                        return
                    }
                    context.state.accessToken = res.data;
                    context.commit('SELF_USER', res.data)
                })
            })
        }
    },
    mutations: {
        SSO_LOGIN(state, value) {
            //请求登录接口
            oauthLogin(value).then((res) => {
                if (!res) {
                    return
                }

                // 登录成功将直接请求授权码模式接口,将跳转至临时授权页面
                // 使用windows.href直接请求接口
                // console.log("授权码模式...")
                // console.log("请求授权确认...", `${process.env.VUE_APP_PROXY_TARGET}/dev-api/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`)
                window.location.href = `${process.env.VUE_APP_PROXY_TARGET}/dev-api/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`
            })
        },
        SELF_USER(state, value) {
            console.log("value-->{}", value)
            //将accessToken存放到cookie中
            setToken(state.accessToken.access_token);
            //此处将获取当前登录用户信息并重定向到首页保存用户信息至cookie中
            getCurrentUser().then((res) => {
                console.log("res", res)
                setValue("userInfo", res.data);
            })
        }
    },
}