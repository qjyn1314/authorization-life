import {axiosService} from '@/utils/service-util'
import {AUTH_SERVER} from '@/utils/global-util'


//  获取获取一个励志的句子
export function inspirational() {
    return axiosService.post(`/${AUTH_SERVER}/oauth/sentence`)
}

//  获取图片验证码
export function pictureCode(uuid) {
    return axiosService.get(`/${AUTH_SERVER}/oauth/picture-code?uuid=${uuid}`)
}

//  获取client信息
export function getClient(data) {
    return axiosService.post(`/${AUTH_SERVER}/oauth/client`, data)
}

//  账号登录
export function oauthLogin(data) {
    return axiosService.postForm(`/${AUTH_SERVER}/oauth/login`, data)
}

// 通过code获取access token
export function getOauth2TokenByCode(data) {
    return axiosService.postForm(`/${AUTH_SERVER}/oauth2/token`, data);
}

// 获取当前登录用户的信息
export function getCurrentUser() {
    return axiosService.get(`/${AUTH_SERVER}/oauth/self-user`)
}

// SpringSecurity的退出登录处理器
export function oauthLogout(data) {
    return axiosService.get(`/${AUTH_SERVER}/oauth/logout`)
}

// 发送邮箱注册验证码
export function sendEmailCode(data) {
    return axiosService.post(`/${AUTH_SERVER}/oauth/send-email-code`, data)
}

// 邮箱注册
export function emailRegister(data) {
    return axiosService.post(`/${AUTH_SERVER}/oauth/email-register`, data)
}

// 发送邮箱注册验证码
export function sendEmailCodeResetPwd(data) {
    return axiosService.post(`/${AUTH_SERVER}/oauth/send-email-code-reset-pwd`, data)
}

// 邮箱注册
export function resetPassword(data) {
    return axiosService.post(`/${AUTH_SERVER}/oauth/reset-password`, data)
}






