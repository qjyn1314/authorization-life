// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
import Cookies from "js-cookie";
// 手动添加cookie
const cookiesStorage = {
    setItem(key, state) {
        // 设置有效期 3 天，不设置默认同 sessionStorage 有效期一致
        return Cookies.set(key, state, {expires: 3})
    },
    getItem(key) {
        return JSON.stringify(Cookies.get(key))
    },
}
// 对外暴露方法
export const userAuthStore = defineStore("userAuthStore", {
    // 开启数据持久化
    persist: {
        enabled: true, // true 表示开启持久化保存
        storage: localStorage
    },
    // 存储数据state
    state: () => {
        return {
            // 用户信息
            loginUser: {
                userId: "",
                loginName: "",
                sex: "",
                avatar: ""
            },
            clientInfo:{
                clientId: "",
                clientSecret: "",
                redirectUrl: ""
            }
        };
    },
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {
        userOauth2Token(client_id, client_secret, redirect_uri) {
            console.log(redirect_uri);
            console.log(client_secret);
            console.log(redirect_uri);
            this.clientInfo.clientId = client_id;
            this.clientInfo.clientSecret = client_secret;
            this.clientInfo.redirectUrl = redirect_uri;
        },
    },
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getLoginUser: state => state.loginUser
    }
});