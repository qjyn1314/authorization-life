import {getValue, setValue} from '@/utils/storage-util'
//认证服务和授权服务
let AUTH_SERVER = getValue(`auth-life`) || `auth-life`
//使用token的服务
let SYSTEM_SERVER = getValue(`system-life`) || `system-life`
// 重新设置请求路径的前缀
window.changeRoute = function (code, value) {
    setValue(code, value)
    console.log('code', code, 'value', value)
}

export {
    AUTH_SERVER,
    SYSTEM_SERVER,
}
