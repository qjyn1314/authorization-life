import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from 'element-ui'

Vue.use(ElementUI)

Vue.config.productionTip = false

const vm = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')

console.log('process.env-->', process.env)

export default vm