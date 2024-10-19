import Cookies from 'js-cookie'

export function setValue(key, value) {
    return Cookies.set(key, value)
}

export function getValue(key) {
    let value = Cookies.get(key);
    console.log('从cookie中获取值..', key, value);
    return value;
}

export function removeKey(key) {
    return Cookies.remove(key)
}

const TokenKey = 'access_token'

export function getToken() {
    return getValue(TokenKey)
}

export function setToken(token) {
    return setValue(TokenKey, token)
}

export function removeToken() {
    return removeKey(TokenKey)
}

