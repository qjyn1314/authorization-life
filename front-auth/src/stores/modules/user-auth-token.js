// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
import {access_token_key, setToken} from "@/utils/storage-util"
// 对外暴露方法
export const userTokenStore = defineStore("userTokenStore", {
    // 开启数据持久化
    persist: {
        enabled: true, // true 表示开启持久化保存
        storage: localStorage
    },
    // 存储数据state
    state: () => {
        return {
            accessToken: "",
        };
    },
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {
        userOauth2Token() {
            setToken(access_token_key, this.accessToken);
        },
    },
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getAccessToken: state => state.accessToken
    }
});