import request from '../utils/request'
import {AUTH_SERVER} from './severApi'
import Qs from 'qs' // 表单提交 data序列化 
// 值集-行值集查询-公用接口*******************************
export function getlovoptions (tenantId, data) {
  return request({
    url: `/${AUTH_SERVER}/v1/${tenantId}/lov-value/info`,
    method: 'GET',
    params: data
  })
} 
// 查询当前登录用户的信息
export function getUserSelf () {
  return request({
    url: `/${AUTH_SERVER}/v1/user/self`,
    method: 'GET'
  })
}
// 注册
export function getRegister (data) {
  return request({
    url: `/${AUTH_SERVER}/v1/company/register`,
    method: 'POST', 
    data: data
  })
}
//  注册 - 发送验证码
export function getSendCode (data) {
  return request({
    url: `/${AUTH_SERVER}/v1/company/register/send-code`,
    method: 'GET',
    params: data
  })
}
//  账号登录 -在登录失败10次之后，获取验证码图片接口
export function getAuthCaptcha (data) {
  return request({
    url: `/${AUTH_SERVER}/auth/captcha`,
    method: 'GET',
    params: data
  })
}
//   短信登录 - 发送验证码
export function getAuthCaptchaCode (data) {
  return request({
    url: `/${AUTH_SERVER}/auth/captcha-code`,
    method: 'GET',
    params: data
  })
}
// 获取token
export function getOauth2Token (data) {
  return request({
    url: `/${AUTH_SERVER}/oauth2/token?${Qs.stringify(data)}`,
    method: 'POST'
  })
} 