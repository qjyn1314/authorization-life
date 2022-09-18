// import router from './index'
// import { setCookie, getCookie } from '../utils/index'
// import { getuserself, getUrlParams, find } from './promission'
// import NProgress from 'nprogress' // progress bar
// import 'nprogress/nprogress.css' // progress bar style
// NProgress.configure({ showSpinner: false }) // NProgress Configuration
//
// router.beforeEach(async (to, from, next) => {
//   NProgress.start()
//   if (to.matched.length !== 0) {
//   // 获取token 登录成功重定向url
//     let accessToken = await getUrlParams('access_token') || ''
//     let tokenType = await getUrlParams('token_type') || ''
//     // eslint-disable-next-line eqeqeq
//     let error = window.location.href.indexOf('error') != -1
//     if (accessToken && tokenType && !error) {
//       setCookie('accessToken', accessToken)
//       setCookie('tokenType', tokenType)
//       let hrefUrl = window.document.location.href
//       window.location.href = String(hrefUrl.substring(0, find(hrefUrl, '#', 0)))
//     } else {
//       if (error) {
//         window.location.href = `${window.location.origin}/login`
//         return
//       }
//
//       // eslint-disable-next-line eqeqeq
//       if (!to.redirectedFrom && to.name && (to.name != 'login' && to.name != 'register' && to.name != 'mailboxVerification')) {
//         await getuserself()
//         if (!localStorage.getItem('tenantId') && getCookie('accessToken')) {
//           window.location.href = `${window.location.origin}/index`
//         }
//       }
//       next()
//     }
//   } else {
//     next({ path: '/login' })
//   }
// })
//
// router.afterEach(() => {
//   NProgress.done() // finish progress bar
// })
//
// router.onError((error) => {
//   NProgress.done() // finish progress bar
//   const pattern = /Loading chunk (\d)+ failed/g
//   const isChunkLoadFailed = error.message.match(pattern)
//   const targetPath = router.history.pending.fullPath
//
//   if (isChunkLoadFailed) {
//     router.replace(targetPath)
//   }
// })
