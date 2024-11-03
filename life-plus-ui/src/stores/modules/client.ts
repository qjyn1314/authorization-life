// 定义是否折叠小仓库[选择式Api写法]
import {defineStore} from "pinia";
import {PINIA_PREFIX} from "@/config";
// defineStore方法执行会返回一个函数，函数的作用就是让组件可以获取到仓库数据
const clientStore = defineStore("client", {
  // 开启数据持久化
  persist: {
    // enabled: true, // true 表示开启持久化保存
    key: PINIA_PREFIX + "client", // 默认会以 store 的 id 作为 key
    storage: localStorage
  },
  // 存储数据state
  state: (): any => {
    return {
      domainName: "",
      clientId: "",
      clientSecret: "",
      grantTypes: "",
      scopes: "",
      redirectUri: "",
      accessTokenTimeout: "",
      refreshTokenTimeout: "",
      additionalInformation: "",
      tenantId: "",
    };
  },
  // 该函数没有上下文数据，所以获取state中的变量需要使用this
  actions: {
    // Set client
    async setClientInfo(clientInfo:any) {
      this.domainName = clientInfo.domainName;
      this.clientId = clientInfo.clientId;
      this.clientSecret = clientInfo.clientSecret;
      this.grantTypes = clientInfo.grantTypes;
      this.scopes = clientInfo.scopes;
      this.redirectUri = clientInfo.redirectUri;
      this.accessTokenTimeout = clientInfo.accessTokenTimeout;
      this.refreshTokenTimeout = clientInfo.refreshTokenTimeout;
      this.additionalInformation = clientInfo.additionalInformation;
      this.tenantId = clientInfo.tenantId;
    }
  },
  // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
  getters: {}
});

// 对外暴露方法
export default clientStore;
