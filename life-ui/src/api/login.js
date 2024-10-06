import request from '../utils/request'
import {AUTH_SERVER} from './severApi'
// import Qs from "qs";

//  获取图片验证码
export function pictureCode(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/picture-code`,
        method: 'GET',
        data: data
    })
}

//  获取client信息
export function getClient(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/client`,
        method: 'GET',
        data: data
    })
}

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

// SpringSecurity的退出登录处理器
export function oauthLogout(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/logout`,
        method: 'GET',
        data: data
    })
}

// 通过refreshToken刷新accessToken
export function refreshTokenByAccessToken(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth2/token`,
        method: 'POST',
        data: data
    })
}


