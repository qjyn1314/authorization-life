import request from '../utils/request'
import {AUTH_SERVER} from './severApi'
// import Qs from "qs";

//  获取获取一个励志的句子
export function inspirational() {
    return request({
        url: `/${AUTH_SERVER}/oauth/sentence`,
        method: 'POST',
    })
}

//  获取图片验证码
export function pictureCode(uuid) {
    return request({
        url: `/${AUTH_SERVER}/oauth/picture-code?uuid=${uuid}`,
        method: 'GET',
    })
}

//  获取client信息
export function getClient(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/client`,
        method: 'POST',
        data: data
    })
}

//  账号登录
export function oauthLogin(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth/login`,
        method: 'POST',
        data: data,
        //设置请求头-Content-Type
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    })
}

// 通过code获取access token
export function getOauth2TokenByCode(data) {
    return request({
        url: `/${AUTH_SERVER}/oauth2/token`,
        method: 'POST',
        data: data,
        //设置请求头-Content-Type
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    })
}

// 获取当前登录用户的信息
export function getCurrentUser() {
    return request({
        url: `/${AUTH_SERVER}/oauth/self-user`,
        method: 'GET',
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
