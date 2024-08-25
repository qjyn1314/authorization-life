import request from '../utils/formAxios'
import {AUTH_SERVER} from './severApi'
import Qs from "qs";

//  账号登录
export function getAuthLogin(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/login`,
        method: 'POST',
        data: data
    })
}

//  短信登录
export function getLoginSms(data) {
    return request({
        url: `/${AUTH_SERVER}/login/sms`,
        method: 'POST',
        data: data
    })
}

// 通过code获取access token
export function getOauth2TokenByCode(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth2/token`,
        method: 'POST',
        data: data
    })
}

// 通过code获取access token
export function oauth2Revoke(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth2/revoke`,
        method: 'POST',
        data: data
    })
}

// 通过code获取access token
export function oauthLogout(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/logout`,
        method: 'GET',
        data: data
    })
}
