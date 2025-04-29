import {getClient, getCurrentUser, getOauth2TokenByCode, oauthLogin} from '@/api/login'
import {AUTH_SERVER} from '@/api/severApi'
import {setToken, setValueExpires} from '@/utils/cookie-util'

export default {
    name: 'ssoAuth',
    namespaced: true,
    state: {
        clientInfo: {
            domainName: '',
            clientId: '',
            clientSecret: '',
            grantTypes: '',
            scopes: '',
            redirectUri: '',
            accessTokenTimeout: 0,
            refreshTokenTimeout: 0,
            additionalInformation: '',
            tenantId: "0"
        }
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
                context.state.clientInfo = res.data.data;
                console.log(value);
                //在获取到client的认证信息后请求登录接口
                context.commit('SSO_LOGIN', value)
            })
        },
        //在临时授权页面请求接口用于获取当前登录用户的信息并存储至cookie中和store.state中用于使用getters进行直接获取
        oauth2AccessTokenByCode(context, value) {
            //请求并获取client信息为获取accessToken使用
            getClient({domain: new URL(window.location.href).hostname}).then((res) => {
                if (!res || !res.data || !res.data.clientId) {
                    return;
                }
                context.state.clientInfo = res.data.data;
                let clientInfo = res.data.data;
                let accessTokenByCode = {
                    grant_type: 'authorization_code',
                    redirect_uri: clientInfo.redirectUri,
                    client_id: clientInfo.clientId,
                    client_secret: clientInfo.clientSecret,
                    code: value.code,
                    state: value.state
                };
                getOauth2TokenByCode(accessTokenByCode).then((res) => {
                    if (!res) {
                        return;
                    }
                    //将返回的accesstoken信息存放至cookie中, 并访问当前登录用户信息接口
                    context.commit('SELF_USER', res.data)
                })
            })
        }
    },
    mutations: {
        SSO_LOGIN(state, value) {
            //请求登录接口
            oauthLogin(value).then((res) => {
                console.log(res)
                // if (!res) {
                //     return
                // }
                // 登录成功将直接请求授权码模式接口,将跳转至临时授权页面
                // 使用windows.href直接请求接口
                // console.log("授权码模式...")
                // console.log("请求授权确认...", `${process.env.VUE_APP_PROXY_TARGET}/dev-api/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`)
                window.location.href = `${process.env.VUE_APP_PROXY_TARGET}/dev-api/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`
            })
        },
        SELF_USER(state, value) {
            //将accessToken存放到cookie中并设置过期时间
            setToken("access_token_info", JSON.stringify(value), value.expires_in);
            setToken(value.access_token, value.expires_in);
            //此处将获取当前登录用户信息并重定向到首页保存用户信息至cookie中
            getCurrentUser().then((res) => {
                setValueExpires("userInfo", JSON.stringify(res.data), value.expires_in);
                window.location.href = `${process.env.VUE_APP_PROXY_TARGET}/dashboard`
            })
        }
    },
}