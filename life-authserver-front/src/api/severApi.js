/* eslint-disable camelcase */
let LOGINTOKEN = {
    client_id: 'passport',
    client_secret: '3MMoCFo4nTNjRtGZ',
    redirect_uri: `${process.env.VUE_APP_API_HOST}/system/datasource`, // 默认登录跳转的链接
    // response_type: 'token',
    response_type: 'authorization_code',
    grant_type: 'authorization_code',
    loginRedirectUrl: `${process.env.VUE_APP_API_HOST}` // 登录重定向域名
}

let AUTH_SERVER = `auth-life`
let SYSTEM_SERVER = `system-life`

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
}

export {
    AUTH_SERVER,
    SYSTEM_SERVER,
    LOGINTOKEN,
    getTenantId
}
