// 定义权限小仓库[选择式Api写法]
import { defineStore } from "pinia";
import { currentUserInfo, getOauth2TokenByCode, oauthLogin } from "@/api/system/user";
import useClientStore from "@/stores/modules/client.ts";
import { AUTH_SERVER } from "@/utils/global.u.ts";
import useUserStore from "@/stores/modules/user.ts";

// TS OR JS 中不能直接导入 import { useRouter } from "vue-router";
import router from "@/routers/index";

import { staticRouter } from "@/routers/modules/staticRouter";
import authMenu from "@/assets/json/authMenu.json";
import authUser from "@/assets/json/authUser.json";
import { generateFlattenRoutes, generateRoutes } from "@/utils/filterRoute.ts";
import { getAllBreadcrumbList, getShowStaticAndDynamicMenuList } from "@/utils/index.ts";
import { HOME_URL } from "@/config";

// 权限数据，不进行持久化。否则刷新浏览器无法获取新的数据。
const authStore = defineStore("auth", {
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
    // 请求认证服务的登录接口
    ssoLogin(loginParams: any) {
      oauthLogin(loginParams).then((res) => {
        if (!res) {
          return;
        } else {
          this.oauth2Authorize();
        }
      });
    },
    // 在security中验证成功之后, 将请求授权码模式中的请求获取临时code接口, 并跳转到临时授权页面
    oauth2Authorize() {
      //将携带此次认证信息跳转至临时授权页面
      const clientStore = useClientStore();
      let host_target = import.meta.env.VITE_SERVER;
      let base_api = import.meta.env.VITE_WEB_BASE_API;
      // 登录成功将直接请求授权码模式接口,将跳转至临时授权页面
      // 使用windows.href直接请求接口
      // console.log("授权码模式...")
      // console.log("请求授权确认...", `${process.env.VUE_APP_PROXY_TARGET}/dev-api/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${state.clientInfo.clientId}&scope=${state.clientInfo.scopes}&state=${state.clientInfo.grantTypes}&redirect_uri=${state.clientInfo.redirectUri}`)
      window.location.href = `${host_target}/${base_api}/${AUTH_SERVER}/oauth2/authorize?response_type=code&client_id=${clientStore.clientId}&scope=${clientStore.scopes}&state=${clientStore.grantTypes}&redirect_uri=${clientStore.redirectUri}`
    },
    // 在临时授权页面中, 通过临时code获取accessToken, 并存储至cookie中
    genAccessToken(tempParams: any) {
      const clientStore = useClientStore();
      //请求的参数
      let params = {
        grant_type: "authorization_code",
        redirect_uri: clientStore.redirectUri,
        client_id: clientStore.clientId,
        client_secret: clientStore.clientSecret,
        code: tempParams.code,
        state: tempParams.state
      };
      //请求oauth/token接口
      getOauth2TokenByCode(params).then((res) => {
        const userStore = useUserStore();
        //获得到的结果示例
        // {
        //   "access_token": "eyJraWQiOiIyOGNkYWQwYi0yYzlkLTQwMGItYWIyYi1jNGExNzE5ZmViODkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhdXRoLXNlcnZlciIsImF1ZCI6InBhc3Nwb3J0X2RldiIsIm5iZiI6MTczMTE2NjkyNSwic2NvcGUiOlsiVEVOQU5UIl0sImlzcyI6Imh0dHBzOi8vYXV0aG9yaXphdGlvbi5saWZlIiwiZXhwIjoxNzMxMTc3NzI1LCJpYXQiOjE3MzExNjY5MjUsImp0aSI6Ijc3MDMyZGNjLTMyMmQtNDM2YS1hYzdjLWM3MmY1NzRjMDNmYSIsInRva2VuIjoiYjY1YjVmZmFkNDc4NDhkZGI2MGUzZjAyZTVjYTI4NjYifQ.Nowkir4LaW5qqThNUkSFeVn_x_Rd5HoCbv_9YfBNL0rVONxXRE1F4mjX1xxjoS4hcox71hS93zod_ITB4JEsrdHsr5cYUQXjWDlHoKq-QsUTrzX1_aH9CCYFYHlqpIPEK50RR4VwegxdsYpObn7eua7r04M9H_f2DOPxQqJGXpzrQmQv2J2rFulWnFb3Saow86w_DxylCvSTShqfvIYPhauObf-Uepiexy7jxaYXVYJgPDz9gI54HkyRlU9d99cNF5wOHJTrheV_zy-Armjfm3P8sOXm_xDZw94zwV83VLqO9qdRUd_3zh4sX0dsTXbGlaRSxotVFs7AvecIF8WtRw",
        //   "refresh_token": "hWuGAqGURtercRtiyhZgU5GfF5Pp-UvAjK4xX6O_UAFhoJuVDxkreScUQzuecqX_P-EhodW7jTRX7eNV2G1g7B0fr4j-NAAIiicNWdXmcuq7GfDQ1eohS537ClfV4jj9",
        //   "scope": "TENANT",
        //   "token_type": "Bearer",
        //   "expires_in": 10800
        // }
        //将AccessToken设置到本地cookie和localstorage中.
        userStore.setToken(res);
        const clientStore = useClientStore();
        //在登录成功后清除掉client信息
        clientStore.clearClient();
        // 根据accessToken当前登录用户的菜单和按钮和角色.
        this.genUserInfo();
      });
    },
    //登录成功后获取当前用户信息
    async genUserInfo() {
      await currentUserInfo().then(async res => {
        await this.setUserInfo(res.data);
      });
      //登录成功并获取到token后将跳转至登录页面
      router.replace(HOME_URL);
    },
    //每次刷新页面后进行获取用户信息不再进行跳转到首页
    async realTimeUserInfo() {
      await currentUserInfo().then((res) => {
        this.setUserInfo(res.data);
      });
    },
    async setUserInfo(user: object) {
      //设置当前登录用户信息
      this.loginUser = user;
      //设置角色
      this.roleList = [];
      //设置按钮
      this.buttonList = authUser.data.buttons;
      //生成用户的菜单
      this.listRouters(authMenu.data);
    },
    // 获取后端菜单数据
    async listRouters(authMenu: any[]) {
      // res.data是后端接口原始数据，进行扁平化路由数据。
      this.menuList = generateFlattenRoutes(authMenu);
      // 持久化递归菜单数据，左侧菜单栏渲染，这里的菜单将后端数据进行递归，需要将动态路由 isHide == 0 的隐藏菜单剔除，将静态路由 isHide == 0 的隐藏菜单剔除
      this.recursiveMenuList = getShowStaticAndDynamicMenuList(staticRouter).concat(
        generateRoutes(getShowStaticAndDynamicMenuList(authMenu), 0)
      );
      // 面包屑需要静态和动态所有的数据，无论是否隐藏
      this.breadcrumbList = staticRouter.concat(generateRoutes(authMenu, 0));
      //生成路由信息
      this.menuList.forEach((item: any) => {
        // if (item.component && typeof item.component == "string") {
        //   // 扁平化路由也需要构造component路由函数
        //   item.component = modules["/src/views/" + item.component + ".vue"];
        // }
        if (item.isFull == "0") {
          // 如果是全屏的话，直接为整个页面
          router.addRoute(item);
        } else {
          router.addRoute("layout", item);
        }
      });
    }
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
