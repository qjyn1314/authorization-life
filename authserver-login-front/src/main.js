import Vue from 'vue'
import Element from 'element-ui' // 引入elementui主库
import App from './App'

/* css库 */
import 'normalize.css'
import 'element-ui/lib/theme-chalk/display.css' // 引入屏幕适配 
import './styles/icomoon/iconfont.css' // 引入字体图标
import './styles/theme/index.css'
import './styles/index.scss' // 自定义样式   

/* js库 */
import router from './router'
import './router/routerInterceptor' // 引入路由控制 
import store from './store' // 引入状态管理  

import {i18n} from './lang' // 引入语言文件
Vue.use(Element, {
  size: 'medium',
  i18n: (key, value) => i18n.t(key, value)
})    

Vue.config.productionTip = false 
const vue = new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
}).$mount("#app") 
export default vue