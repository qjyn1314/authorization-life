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

const TokenKey = 'accessToken'

export function getToken() {
    return getValue(TokenKey)
}

export function setToken(token, expires) {
    return setValueExpires(TokenKey, token, expires)
}

export function removeToken() {
    return removeKey(TokenKey)
}
