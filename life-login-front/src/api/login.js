import request from '../utils/formAxios'
import { AUTH_SERVER } from './severApi'
import Qs from "qs";

//  账号登录
export function getAuthLogin (data) {
  return request({
    url: `/${AUTH_SERVER}/oauth2/login`,
    method: 'POST',
    data: data
  })
}
//  短信登录
export function getLoginSms (data) {
  return request({
    url: `/${AUTH_SERVER}/login/sms`,
    method: 'POST',
    data: data
  })
}

// 获取token
export function getOauth2Token (data) {
  return request({
    url: `/${AUTH_SERVER}/oauth2/token?${Qs.stringify(data)}`,
    method: 'POST'
  })
}
// 通过code获取access token
export function getOauth2TokenByCode (data) {
  return request({
    url: `/${AUTH_SERVER}/oauth2/token`,
    method: 'POST',
    data: data
  })
}
