// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
import Cookies from "js-cookie";

// 权限数据，不进行持久化。否则刷新浏览器无法获取新的数据。
const authStore = defineStore("auth", {
    // 开启数据持久化
    persist: {
        enabled: true, // true 表示开启持久化保存
        key: "auth_keepAlive", // 默认会以 store 的 id 作为 key
        storage: Cookies
    },
    // 存储数据state
    state: (any) => {
        return {
            // 用户信息
            loginUser: {
                userId: "",
                loginName: "",
                sex: "",
                avatar: ""
            }
        };
    },
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {},
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getLoginUser: state => state.loginUser
    }
});

// 对外暴露方法
export default authStore;