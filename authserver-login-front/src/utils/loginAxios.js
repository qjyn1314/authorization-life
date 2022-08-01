import vue from 'vue'
import axios from 'axios'
import Qs from 'qs' // 表单提交 data序列化 
import { Message } from 'element-ui'
import { clearLoginCache } from '../utils/index'

let outCount = 0
// create an axios instance
const service = axios.create({
  timeout: 60 * 1000, // request timeout
  baseURL: process.env.VUE_APP_URL
}) 
service.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/x-www-form-urlencoded'
  if (config.data) {
    config.data = Qs.stringify(config.data)
  }
  return config
}, err => {
  Promise.reject(err)
})
// 添加响应拦截
service.interceptors.response.use(response => {
 // eslint-disable-next-line eqeqeq
 // isMessage 是否错误提示 默认提示可以不传  不提示时config.isMessage：'ok'
 // code 0 成功 -1 吐报错Message  -2是表单行内报错
 if (response.data && response.data.code == '-1' && !response.config.isMessage) { 
  Message({
    showClose: true, 
    message: `${response.data.desc}`,
    type: 'error',
    duration: 5000
  })
} 
response.body = response.data 
return response
}, err => {
  //   400: 报错
  //   401: 重定向到登录页
  //   403: 报错，没有权限
  let xtr = err.response.status
  // eslint-disable-next-line eqeqeq
  if (xtr == '401') {
    if (outCount === 0) {
      outCount++
      Message({
        showClose: true,
        message: ` ${err.response.data.desc || '登录超时，请重新登录'}`,
        type: 'error',
        duration: 5000
      })
      clearLoginCache()
      setTimeout(function () {
        location.href = '/login'
        outCount = 0
      }, 3000)
    }
  } else if (xtr == '404') {
    Message({
      showClose: true,
      message: `${err.response.data.desc || '资源不存在'}`,
      type: 'error',
      duration: 5000
    })
  }  else {
    Message({
      showClose: true,
      message: ` ${err.response.data.desc || '用户无权访问'}`,
      type: 'error',
      duration: 5000
    })
  }
  return Promise.reject(err)
})  
export default service
