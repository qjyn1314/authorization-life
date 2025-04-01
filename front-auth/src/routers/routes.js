/**
 * 在主框架内显示
 */
const frameIn = [
    {
        path: '/',
        // 配置访问根路径时默认重定向显示的页面
        // redirect: {name: 'index'},
        redirect: {name: 'clients',},
        component: () => import('@/layout/index.vue'),
        children: [
            // 首页
            {
                path: '/index',
                name: 'index',
                meta: {
                    auth: true,
                    title: '首页',
                },
                component: () => import('@/views/home/index.vue'),
            },
        ]
    }
]

/**
 * 在主框架之外显示
 */
const frameOut = [
    {
        path: '/clients',
        name: 'clients',
        component: () => import('@/views/clients/ClientsPage.vue'),
        meta: {
            title: '管理OAuth2Client',
        },
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/login/LoginView.vue'),
        meta: {
            title: 'Oauth2SSOLogin',
        },
    },
    {
        path: '/reg',
        name: 'reg',
        component: () => import('@/views/login/RegView.vue'),
        meta: {
            title: 'Oauth2SSOReg',
        },
    },
    {
        path: '/resetPwd',
        name: 'resetPwd',
        component: () => import('@/views/login/ResetPwdView.vue'),
        meta: {
            title: 'Oauth2SSOResetPwd',
        },
    },
    {
        path: '/auth-redirect',
        name: 'resetPwd',
        component: () => import('@/views/auth/TemporaryAuth.vue'),
        meta: {
            title: 'Oauth2SSOTemporaryAuth',
        },
    }
]

/**
 * 错误页面
 */
const errorPage = [
    {
        path: '/:pathMatch(.*)*',
        name: '404',
        component: () => import('@/views/404/index.vue'),
        meta: {
            title: '404',
        },
    }
]

// 导出需要显示菜单的
export const frameInRoutes = frameIn

// 重新组织后导出
export default [
    ...frameIn,
    ...frameOut,
    ...errorPage
]
