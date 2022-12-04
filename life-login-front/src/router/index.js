import Vue from 'vue'
import Router from 'vue-router' 

Vue.use(Router) // 需求设置
// 获取原型对象上的push函数
const originalPush = Router.prototype.push
const originalReplace = Router.prototype.replace
// 修改原型对象中的push方法
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalReplace.call(this, location, onResolve, onReject)
  return originalReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history',
  base: '/login',
  routes: [ 
    {
      path: '/', // 登录
      name: 'login',
      component: () => import('@/views/login/login.vue'),
      meta: {
        title: 'login'
      }
    },
    {
      path: '/register', // 注册
      name: 'register',
      component: () => import('@/views/login/register.vue'),
      meta: {
        title: 'register'
      }
    }, 
    {
      path: '/mailboxVerification', // 邮箱验证成功失败页面
      name: 'mailboxVerification',
      component: () => import('@/views/verification/mailboxVerification.vue'),
      meta: {
        title: 'mailboxVerification'
      }
    },
    {
      path: '/404', component: () => import('@/views/system/404.vue'), meta: { title: '404' }
    },
    {
      path: '/home', // 首页
      name: 'home',
      component: () => import('@/views/home/home.vue'), meta: { title: 'home' }
    },
    // {
    //   path: '/',
    //   component: () => import('@/views/AdminLayout/index.vue'),
    //   meta: { title: '自述文件' },
    //   children: [
    //     {
    //       path: 'index',
    //       name: 'index',
    //       component: () => import('@/views/Index.vue'),
    //       meta: { title: '系统首页' }
    //     },
    //     // 基础设置部分
    //     ...basicSettings,
    //   ]
    // },
  ]
})
