import request from '../utils/loginAxios'
import { AUTH_SERVER } from './severApi'

//  账号登录
export function getAuthLogin (data) {
  return request({
    url: `/${AUTH_SERVER}/login`,
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
