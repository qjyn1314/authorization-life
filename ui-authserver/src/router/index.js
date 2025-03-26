import Vue from 'vue'
import VueRouter from 'vue-router'
import {getToken} from '@/utils/cookie-util'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: () => import('../views/home/Home.vue')
    },
    {
        path: '/dashboard',
        name: 'dashboard',
        component: () => import('../views/dashboard/DashboardView.vue')
    },
    {
        path: '/auth-redirect',
        name: 'auth-redirect',
        component: () => import('../components/Temporary.vue')
    },
    {
        path: '/ssoLogin',
        name: 'login',
        component: () => import('../views/login/LoginView.vue')
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/reg/SignIn.vue')
    },
    {
        path: '/reset-pwd',
        name: 'resetPwd',
        component: () => import('../views/password/ResetPassword.vue')
    },
    {
        path: '/temp',
        name: 'temp',
        component: () => import('../views/temp/TempPage.vue')
    },
    {
        path: '/lov',
        name: 'lov',
        component: () => import('../views/lov/LovPage.vue')
    },
    {
        path: '/lovValue',
        name: 'lovValue',
        component: () => import('../views/lov/LovValuePage.vue')
    },
    {
        path: '/test',
        name: 'testView',
        component: () => import('../views/test/TestView.vue')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/error/404.vue')
    },
    // 404 page must be placed at the end !!!
    {path: '*', redirect: '/404', hidden: true}
]

const router = new VueRouter({
    //此处必须使用 history 模式. 因为有回调函数
    mode: 'history',
    routes
})

//白名单路径
const whiteList = ['/ssoLogin', '/register', '/reset-pwd', '/auth-redirect', '/404', '/test'] // no redirect whitelist

router.beforeEach((to, from, next) => {
    let token = getToken();
    if (token) {
        if (whiteList.indexOf(to.path) !== -1 || to.path === '/') {
            //如果已经登录还是有白名单的请求路径则跳转至 dashboard
            next(`/dashboard`)
        } else {
            next()
        }
    } else {
        //没有登录信息且跳转的路径中包含了白名单路径则跳转, 否则跳转至登录页
        if (whiteList.indexOf(to.path) !== -1) {
            next()
        } else {
            // next(`/login?redirect=${to.path}`)
            next(`/ssoLogin`)
        }
    }
})

export default router
