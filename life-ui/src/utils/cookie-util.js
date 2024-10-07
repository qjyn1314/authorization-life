import Cookies from 'js-cookie'

const TokenKey = 'access_token'

export function getToken() {
    return Cookies.get(TokenKey)
}

export function setToken(token) {
    return Cookies.set(TokenKey, token)
}

export function removeToken() {
    return Cookies.remove(TokenKey)
}

export function setValue(key, value) {
    return Cookies.set(key, value)
}

export function getValue(key) {
    let value = Cookies.get(key);
    // console.log('从cookie中获取值..', key, value);
    return value;
}

export function removeKey(key) {
    return Cookies.remove(key)
}

