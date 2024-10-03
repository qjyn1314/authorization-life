import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import App from './App.vue'
import router from './router'
import store from './store'

Vue.use(ElementUI);

Vue.config.productionTip = false

const vm = new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')

console.log('process.env-->', process.env)

export default vm