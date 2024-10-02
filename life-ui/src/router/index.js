import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'index',
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
        component: () => import('../views/temp')
    }
]

const router = new VueRouter({
    routes
})

//白名单路径
// let weightPath = ['/login', '/']
//
// router.beforeEach((to, from, next) => {
//
// })


export default router
