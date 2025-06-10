// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
import {access_token_key, setToken} from "@/utils/storage-util"
// 根据临时code请求获取accessToken
export const userAccessTokenStore = defineStore("userAccessTokenStore", {
    // 开启数据持久化
    persist: {
        enabled: true, // true 表示开启持久化保存
        strategies: [
            {
                key: 'latest-data',
                storage: localStorage,
                // 每次状态变化时自动保存
            }
        ]
    },
    // 存储数据state
    state: () => {
        return {
            access_token: "",
            refresh_token:"",
            scope:"",
            token_type:"",
            expires_in:""
        };
    },
    // 该函数没有上下文数据，所以获取state中的变量需要使用this
    actions: {
        setAccessToken(accessTokenInfo) {
            console.log("setAccessToken", accessTokenInfo);
            this.access_token = accessTokenInfo.access_token;
            this.refresh_token = accessTokenInfo.refresh_token;
            this.scope = accessTokenInfo.scope;
            this.token_type = accessTokenInfo.token_type;
            this.expires_in = accessTokenInfo.expires_in;
            setToken(this.access_token, this.expires_in);
        },
    },
    // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
    getters: {
        getAccessToken: state => state.access_token
    }
});