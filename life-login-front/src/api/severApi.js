/* eslint-disable camelcase */
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
    return Cookies.getTenantId();
}

export {
    AUTH_SERVER,
    SYSTEM_SERVER,
    getTenantId
}
