/* eslint-disable camelcase */
export const clearLoginCache = function () { // 清除登录信息
    localStorage.setItem('userinfo', '')
    localStorage.setItem('tenantId', '')
    setCookie('accessToken', '')
    setCookie('tokenType', '')
}
// 是否为开发环境true为开发，false为生产
export const isDevelop = function () {
    return process.env.NODE_ENV === 'development'
}

export const getCookie = (cname) => {
    let name = cname + '='
    let ca = document.cookie.split(';')
    for (const element of ca) {
        let c = element
        while (c.charAt(0) === ' ') c = c.substring(1)
        if (c.indexOf(name) !== -1) return c.substring(name.length, c.length)
    }
    return ''
}

export const setCookie = (c_name, c_value) => {
    let exp = new Date()
    exp.setTime(exp.getTime() + 4 * 24 * 60 * 60 * 1000)
    document.cookie = `${c_name}=${c_value}; path=/;expires=${exp.toGMTString()}`
}
