import {createApp} from 'vue'
import App from './App.vue'
// 引入路由
import router from "./routers/index";
// 引入仓库pinia
import pinia from "./stores";
// 引入animate
import "animate.css";
// 引入ElementPlus
import ElementPlus from "element-plus";
// 引入ElementPlus的css
import "element-plus/dist/index.css";
// 引入ElementPlus所有图标
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";

// 导入微信icon
import '@/assets/icons/iconfont.css'

import "vue3-json-viewer/dist/index.css"

const app = createApp(App);
// 注册ElementPlus
app.use(ElementPlus, {
    locale: zhCn
});
// 注册ElementPlus所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}
// 注册路由
app.use(router);
// 注册pinia
app.use(pinia);
// 挂载
app.mount('#app')
