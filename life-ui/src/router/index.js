import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/login',
        name: 'index',
        component: () => import('../views/dashboard/DashboardView.vue')
    },
    {
        path: '/',
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
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

//白名单路径
// let weightPath = ['/login', '/']
//
// router.beforeEach((to, from, next) => {
//
// })


export default router
