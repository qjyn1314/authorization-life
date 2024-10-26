import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import store from '@/store'
import {getToken} from '@/utils/cookie-util'
import pubsub from 'pubsub-js';

// create an axios instance
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
    withCredentials: true, // send cookies when cross-domain requests
    timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
    config => {
        // do something before request is sent
        if (store.getters.token) {
            // let each request carry token
            // ['X-Token'] is a custom headers key
            // please modify it according to the actual situation
            config.headers['Authorization'] = 'Bearer ' + getToken()
        }
        return config
    },
    error => {
        // do something with request error
        Message.error('系统异常,请稍后重试.');
        console.log('request-error', error) // for debug
        return null;
    }
)

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     * Here is just an example
     * You can also judge the status by HTTP Status Code
     */
    response => {
        const res = response.data
        // if the custom code is not 20000, it is judged as an error.
        if (res.code === '0') {
            return res;
        }
        if (res.code === '-1') {
            // 默认接口错误, 需要将错误信息弹出
            Message.error(res.msg);
        } else if (res.code === '-2') {
            //此处是登录接口报错信息
            console.log('rescode=-2', res)
            if(res.data.showCaptchaCode){
                //发送消息给登录组件并显示验证码框
                pubsub.publish('showCaptcha', {showCaptcha: true});
            }
            Message.error(res.msg);
        } else if (res.code === '50008' || res.code === '50012' || res.code === '50014') {
            // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
            // to re-login
            MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
                confirmButtonText: 'Re-Login',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(() => {
                store.dispatch('user/resetToken').then(() => {
                    location.reload()
                })
            })
        }
        return null;
    },
    error => {
        console.error('response-error', error.message) // for debug
        Message.error('系统错误,请稍后重试.');
        return null;
    }
)

export default service
