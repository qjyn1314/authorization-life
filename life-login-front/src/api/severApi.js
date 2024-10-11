/* eslint-disable camelcase */
//授权码模式的第一步，获取授权码code，传参示例。
let LOGINCODE = {
    grant_type: 'authorization_code',
    response_type: 'code',
    client_id: 'passport',
    scope: 'TENANT',
    state: 'authorization-life',
    redirect_uri: `${process.env.VUE_APP_API_HOST}/login/temporary`, // 默认登录跳转的链接
    loginRedirectUrl: `${process.env.VUE_APP_API_HOST}` // 登录重定向域名
}

//授权码模式的第二步，通过临时授权码code获取accessToken信息，传参示例。
let CODE_ACCESS_TOKEN = {
    grant_type: 'authorization_code',
    client_id: 'passport',
    client_secret: '3MMoCFo4nTNjRtGZ',
    redirect_uri: `${process.env.VUE_APP_API_HOST}/login/temporary`, // 默认登录跳转的链接
    loginRedirectUrl: `${process.env.VUE_APP_API_HOST}` // 登录重定向域名
}
import Cookies from 'js-cookie'
//认证服务和授权服务
let AUTH_SERVER = `auth-life`
//使用token的服务
let SYSTEM_SERVER = `system-life`
// 重新设置请求路径的前缀
window.changeRoute = function (code, route) {
    switch (code) {
        case AUTH_SERVER:
            AUTH_SERVER = route
            console.log(AUTH_SERVER)
            break;
        case SYSTEM_SERVER:
            SYSTEM_SERVER = route
            console.log(SYSTEM_SERVER)
            break;
    }
}

function getTenantId() {
    return localStorage.getItem('tenantId') || ''
    // return Cookies.getTenantId();
}

export {
    AUTH_SERVER,
    SYSTEM_SERVER,
    LOGINCODE,
    CODE_ACCESS_TOKEN,
    getTenantId
}

