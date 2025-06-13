import {createRouter, createWebHistory} from 'vue-router'
import routes from './routes'
// 用于获取当前登录用户的信息
import {getToken} from "@/utils/storage-util"

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

const noRequiredPath = ['/clients','/login','/register','/reset-pwd','/auth-redirect'];


router.beforeEach((to, from, next) => {
    if (to.matched.length === 0) {
        // 不存在的路由地址
        next('/404')
    } else {
        window.document.title = (to.query.title ? to.query.title : to.meta.title);
        let accessToken = getToken();
        let noAuthFlag  = noRequiredPath.includes(to.path);
        // 没有token, 需要认证的则跳转到client管理
        if(!accessToken && !noAuthFlag){
         return next("/clients");
        }
        // 不需要登录的路径直接放行
        if(noAuthFlag) {
            return next()
        }
        // 有登录用户信息直接放行
        if (accessToken && to.path === '/login') {
            console.log('getToken()', getToken());
           return next("/index")
        }
        // 有登录用户信息直接放行
        if (accessToken) {
            console.log('getToken()', getToken());
           return next()
        }
    }

})

router.afterEach((to, from) => {

})

export default router
