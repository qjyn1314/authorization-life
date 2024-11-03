// 定义是否折叠小仓库[选择式Api写法]
import { defineStore } from "pinia";
import { cookie } from "@/utils/storage.ts";
import { ACCESS_TOKEN } from "@/config";
// defineStore方法执行会返回一个函数，函数的作用就是让组件可以获取到仓库数据
const userStore = defineStore("user", {
  // 存储数据state
  state: (): any => {
    return {
      token: cookie.get(ACCESS_TOKEN)
    };
  },
  // 该函数没有上下文数据，所以获取state中的变量需要使用this
  actions: {
    // Set Token
    setToken(token: string) {
      // 并指定过期秒
      cookie.setEx(ACCESS_TOKEN, token, 6000);
    }
  },
  // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
  getters: {
  }
});

// 对外暴露方法
export default userStore;
