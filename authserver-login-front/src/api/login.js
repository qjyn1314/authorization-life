import request from '../utils/loginAxios'
import { ZIAM } from './severApi'

//  账号登录
export function getAuthLogin (data) {
  return request({
    url: `/${ZIAM}/auth/login`,
    method: 'POST',
    data: data
  })
}
//  短信登录
export function getLoginSms (data) {
  return request({
    url: `/${ZIAM}/login/sms`,
    method: 'POST', 
    data: data
  })
}
