// 定义权限小仓库[选择式Api写法]
import {defineStore} from "pinia";
import {currentUserInfo, getOauth2TokenByCode, oauthLogin} from "@/api/system/user";
import useClientStore from "@/stores/modules/client.ts";
import {AUTH_SERVER} from "@/utils/global.ts";
import useUserStore from "@/stores/modules/user.ts";

import router from "@/routers/index.ts";

import { staticRouter } from "@/routers/modules/staticRouter";
import authMenu from "@/assets/json/authMenu.json";
import { generateRoutes, generateFlattenRoutes } from "@/utils/filterRoute.ts";
import { getShowStaticAndDynamicMenuList, getAllBreadcrumbList } from "@/utils/index.ts";


// 权限数据，不进行持久化。否则刷新浏览器无法获取新的数据。
const authStore = defineStore("auth", {
  // 存储数据state
  state: (): any => {
    return {
      // 扁平化路由数据
      menuList: [],
      // 递归之后的菜单数据
      recursiveMenuList: [],
      // 面包屑数据
      breadcrumbList: [],
      // 用户角色
      roleList: [],
      // 按钮权限列表
      buttonList: [],
      // 用户信息
      loginUser: {
        userId: "",
        username: "",
        userPhone: "",
        userEmail: "",
        userGroups: [],
        sex: "",
        avatar: ""
      }
    };
  },
  // 该函数没有上下文数据，所以获取state中的变量需要使用this
  actions: {
    // 请求登录接口
    async ssoLogin(loginParams: any) {
      console.log(loginParams)
      oauthLogin(loginParams).then((res) => {
        console.log(res);
        if (!res) {
          return
        } else {
          this.oauth2Authorize()
        }
      })
    },
    oauth2Authorize() {
      const clientStore = useClientStore();
      let host_target = import.meta.env.VITE_SERVER;
      let base_api = import.meta.env.VITE_WEB_BASE_API;
      // 登录成功将直接请求授权码模式接口,将跳转至临时授权页面
      // 使用windows.href直接请求接口
      // console.log("授权码模式...")
      // console.log("请求授权确认...", `${process.env.VUE_APP_PROXY_TARGET}/dev-api/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`)
      window.location.href = `${host_target}/${base_api}/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${clientStore.clientId}&scope=${clientStore.scopes}&state=${clientStore.grantTypes}&redirect_uri=${clientStore.redirectUri}`
    },
    async genAccessToken(tempParams: any) {
      console.log("接受到参数是", tempParams);
      const clientStore = useClientStore();
      console.log(clientStore);
      let params = {
        grant_type: 'authorization_code',
        redirect_uri: clientStore.redirectUri,
        client_id: clientStore.clientId,
        client_secret: clientStore.clientSecret,
        code: tempParams.code,
        state: tempParams.state
      };
      //请求oauth/token接口
      getOauth2TokenByCode(params).then((res) => {
        const userStore = useUserStore();
        // {
        //   "access_token": "eyJraWQiOiIyOGNkYWQwYi0yYzlkLTQwMGItYWIyYi1jNGExNzE5ZmViODkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhdXRoLXNlcnZlciIsImF1ZCI6InBhc3Nwb3J0X2RldiIsIm5iZiI6MTczMTE2NjkyNSwic2NvcGUiOlsiVEVOQU5UIl0sImlzcyI6Imh0dHBzOi8vYXV0aG9yaXphdGlvbi5saWZlIiwiZXhwIjoxNzMxMTc3NzI1LCJpYXQiOjE3MzExNjY5MjUsImp0aSI6Ijc3MDMyZGNjLTMyMmQtNDM2YS1hYzdjLWM3MmY1NzRjMDNmYSIsInRva2VuIjoiYjY1YjVmZmFkNDc4NDhkZGI2MGUzZjAyZTVjYTI4NjYifQ.Nowkir4LaW5qqThNUkSFeVn_x_Rd5HoCbv_9YfBNL0rVONxXRE1F4mjX1xxjoS4hcox71hS93zod_ITB4JEsrdHsr5cYUQXjWDlHoKq-QsUTrzX1_aH9CCYFYHlqpIPEK50RR4VwegxdsYpObn7eua7r04M9H_f2DOPxQqJGXpzrQmQv2J2rFulWnFb3Saow86w_DxylCvSTShqfvIYPhauObf-Uepiexy7jxaYXVYJgPDz9gI54HkyRlU9d99cNF5wOHJTrheV_zy-Armjfm3P8sOXm_xDZw94zwV83VLqO9qdRUd_3zh4sX0dsTXbGlaRSxotVFs7AvecIF8WtRw",
        //   "refresh_token": "hWuGAqGURtercRtiyhZgU5GfF5Pp-UvAjK4xX6O_UAFhoJuVDxkreScUQzuecqX_P-EhodW7jTRX7eNV2G1g7B0fr4j-NAAIiicNWdXmcuq7GfDQ1eohS537ClfV4jj9",
        //   "scope": "TENANT",
        //   "token_type": "Bearer",
        //   "expires_in": 10800
        // }
        userStore.setAccessToken(res)
        // 获取当前登录用户的信息以及菜单和角色
        currentUserInfo().then((res) => {
          console.log("currentUserInfo", res);
          this.setUserInfo(res.data);
        })
      });
    },
    async setUserInfo(user: any) {
      console.log("setUserInfo", user);
      this.loginUser = user;
      this.listRouters();
      router.replace("/");
    },
    // 获取后端菜单数据
    async listRouters() {
      // res.data是后端接口原始数据，进行扁平化路由数据。
      this.menuList = generateFlattenRoutes(authMenu.data);
      // 持久化递归菜单数据，左侧菜单栏渲染，这里的菜单将后端数据进行递归，需要将动态路由 isHide == 0 的隐藏菜单剔除，将静态路由 isHide == 0 的隐藏菜单剔除
      this.recursiveMenuList = getShowStaticAndDynamicMenuList(staticRouter).concat(
        generateRoutes(getShowStaticAndDynamicMenuList(authMenu.data), 0)
      );
      // 面包屑需要静态和动态所有的数据，无论是否隐藏
      this.breadcrumbList = staticRouter.concat(generateRoutes(authMenu.data, 0));
    },
  },
  // 计算属性，和vuex是使用一样，getters里面不是方法，是计算返回的结果值
  getters: {
    // 按钮权限列表
    getButtonList: state => state.buttonList,
    // 菜单权限列表 ==> 原始后端接口菜单数据，扁平化之后的一维数组菜单，主要用来添加动态路由，隐藏和不隐藏的菜单路由都需要
    getMenuList: state => state.menuList,
    // 菜单权限列表 ==> 左侧菜单栏渲染，这里的菜单将后端数据进行递归，需要将动态路由 isHide == 0 的隐藏菜单剔除, 将静态路由 isHide == 0 的隐藏菜单剔除
    showMenuList: state => state.recursiveMenuList,
    // 递归处理后的所有面包屑导航列表
    getBreadcrumbList: state => getAllBreadcrumbList(state.breadcrumbList)
  }
});

// 对外暴露方法
export default authStore;
