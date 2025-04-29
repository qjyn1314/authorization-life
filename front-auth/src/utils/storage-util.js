import Cookies from 'js-cookie'

export function setValue(key, value) {
    return setValueExpires(key, value, null)
}

export function setValueExpires(key, value, expires) {
    if (expires) {
        //单位秒
        expires = new Date(new Date() * 1 + expires * 1000);
    } else {
        //如果没有过期时间则是当前会话session为过期时间
        expires = null
    }
    return Cookies.set(key, value, {expires: expires, domain: process.env.VUE_APP_COOKIE_HOST});
}

export function getValue(key) {
    return Cookies.get(key);
}

export function removeKey(key) {
    return Cookies.remove(key)
}

export const access_token_key = 'access_token'

export function getToken() {
    return getValue(access_token_key)
}

/**
 * 设置accessToken
 * @param token token值
 * @param expires 秒数
 * @returns {*}
 */
export function setToken(token, expires) {
    return setValueExpires(access_token_key, token, expires)
}

export function removeToken() {
    return removeKey(access_token_key)
}
