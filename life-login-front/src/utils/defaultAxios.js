import axios from 'axios'
import {Message} from 'element-ui'
import {clearLoginCache, getCookie} from '../utils/index'

let outCount = 0

// create an axios instance
const service = axios.create({
    timeout: 60 * 1000, // request timeout
    baseURL: process.env.VUE_APP_URL
})
console.log(process.env)

service.interceptors.request.use(config => {
    let accessToken = getCookie('accessToken') || ''
    let tokenType = getCookie('tokenType') || ''
    config.headers['Authorization'] = `${tokenType} ${accessToken}`
    // 如果没有特别声明，application/json是Axios默认的Content-Type, 所以在此处不需要显示的声明 Content-Type 为 application/json
    // 参考:
    // https://www.cnblogs.com/jdWu-d/p/12036528.html
    // https://www.cnblogs.com/dreamcc/p/10752604.html
    // config.headers['Content-Type'] = 'application/json'
    return config
}, err => {
    Promise.reject(err)
})

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
    }, error => {
        //   400: 报错
        //   401: 重定向到登录页
        //   403: 报错，没有权限
        // eslint-disable-next-line eqeqeq
        if (error.response.status === '401') {
            if (outCount === 0) {
                outCount++
                Message({
                    showClose: true,
                    message: ` ${error.response.data.desc || '登录超时，请重新登录'}`,
                    type: 'error',
                    duration: 5000
                })

                clearLoginCache()
                setTimeout(function () {
                    location.href = `/login?redirect_uri=${encodeURIComponent(window.location.href)}`
                    outCount = 0
                }, 3000)
            }
        } else if (error.response.status === '404') {
            Message({
                showClose: true,
                message: `资源不存在`,
                type: 'error',
                duration: 5000
            })
        } else {
            Message({
                showClose: true,
                message: `用户无权访问`,
                type: 'error',
                duration: 5000
            })
        }
        return Promise.reject(error)
    }
)
export default service
