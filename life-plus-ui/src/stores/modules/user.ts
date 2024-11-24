// 定义是否折叠小仓库[选择式Api写法]
import {defineStore} from "pinia";
import {cookie} from "@/utils/storage.ts";
import {ACCESS_TOKEN, PINIA_PREFIX} from "@/config";
// defineStore方法执行会返回一个函数，函数的作用就是让组件可以获取到仓库数据
const userStore = defineStore("user", {
  // 开启数据持久化
  persist: {
    enabled: true, // true 表示开启持久化保存
    key: PINIA_PREFIX + "user", // 默认会以 store 的 id 作为 key
    storage: localStorage
  },
  // 存储数据state
  state: (): any => {
    return {
      accessToken: ""
    };
  },
  // 该函数没有上下文数据，所以获取state中的变量需要使用this
  actions: {
    // 获取cookie中的token
    getToken() {
      return cookie.get(ACCESS_TOKEN) || this.accessToken;
    },
    //清除token
    clearToken() {
      this.accessToken = "";
      cookie.remove(ACCESS_TOKEN);
    },
    setToken(token: any) {
      // 并指定过期秒
      cookie.setEx(ACCESS_TOKEN, token.access_token, token.expires_in);
      this.accessToken = token.access_token;
    }
  },
  // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
  getters: {}
});

// 对外暴露方法
export default userStore;
