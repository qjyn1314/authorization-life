import Vue from 'vue'
import VueRouter from 'vue-router'
import {getToken} from '@/utils/cookie-util'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import('../views/dashboard/DashboardView.vue')
    },
    {
        path: '/auth-redirect',
        name: 'dashboard',
        component: () => import('../views/dashboard/DashboardView.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/login/LoginView.vue')
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
    // 404 page must be placed at the end !!!
    {path: '*', redirect: '/404', hidden: true}
]

const router = new VueRouter({
    mode: 'history',
    routes
})

//白名单路径
const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

router.beforeEach((to, from, next) => {
    console.log('to', to)
    let token = getToken();
    console.log('token-->', token)
    if (token) {
        // 有登录信息将跳转至首页
        next({path: '/'});
    } else {
        //没有登录信息且跳转的路径中包含了白名单路径则跳转, 否则跳转至登录页
        if (whiteList.indexOf(to.path) !== -1) {
            next()
        } else {
            next(`/login?redirect=${to.path}`)
        }
    }
    console.log("--->需要跳转的页面路由...", to)
})

export default router
