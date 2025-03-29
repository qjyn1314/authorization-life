import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import routes from './routes'

const router = createRouter({
    // 具有井号的路径模式
    // history: createWebHashHistory(process.env.BASE_URL),
    // 没有井号的路径模式
    // history: createWebHistory(process.env.BASE_URL),
    history: createWebHistory(process.env.BASE_URL),
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition;
        } else {
            return {x: 0, y: 0};
        }
    },
    routes
})

router.beforeEach((to, from, next) => {
    if (to.matched.length === 0) {
        // 不存在的路由地址
        next('/404')
    } else {
        window.document.title = (to.query.title ? to.query.title : to.meta.title);
        next()
    }

})

router.afterEach((to, from) => {

})

export default router
