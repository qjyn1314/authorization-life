import {createRouter, createWebHashHistory} from 'vue-router'
import routes from './routes'

// // .env配置文件读取
// const mode = import.meta.env.VITE_ROUTER_MODE;
//
// // 路由访问两种模式：带#号的哈希模式，正常路径的web模式。
// const routerMode = {
//   hash: () => createWebHashHistory(process.env.BASE_URL),
//   history: () => createWebHistory(process.env.BASE_URL)
// };


const router = createRouter({
    // 具有井号的路径模式
    // history: createWebHashHistory(process.env.BASE_URL),
    history: createWebHashHistory(process.env.BASE_URL),
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
