import {createRouter, createWebHashHistory, createWebHistory, NavigationGuardNext, RouteLocationNormalized} from "vue-router";
import {errorRouter, layoutRouter, staticRouter} from "@/routers/modules/staticRouter";
import nprogress from "@/utils/nprogress";
import useUserStore from "@/stores/modules/user.ts";
import useAuthStore from "@/stores/modules/auth.ts";
import {LOGIN_URL, ROUTER_WHITE_LIST} from "@/config/index.ts";
import useGlobalStore from "@/stores/modules/global.ts";

// .env配置文件读取
const mode = import.meta.env.VITE_ROUTER_MODE;

// 路由访问两种模式：带#号的哈希模式，正常路径的web模式。
const routerMode: any = {
  hash: () => createWebHashHistory(),
  history: () => createWebHistory()
};

// 创建路由器对象
const router = createRouter({
  // 路由模式hash或者默认不带#
  history: routerMode[mode](),
  routes: [...layoutRouter, ...staticRouter, ...errorRouter],
  strict: false,
  // 滚动行为
  scrollBehavior() {
    return {
      left: 0,
      top: 0
    };
  }
});

/**
 * @description 前置路由
 * */
router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
  const userStore = useUserStore();
  const authStore = useAuthStore();
  const globalStore = useGlobalStore();
  console.log("前置路由", to, from);
  // 1、NProgress 开始
  nprogress.start();

  // 2、标题切换，没有防止后置路由，是因为页面路径不存在，title会变成undefined
  if (globalStore.language === "en") {
    document.title = to.meta.enName || import.meta.env.VITE_WEB_EN_TITLE;
  } else {
    document.title = to.meta.title || import.meta.env.VITE_WEB_TITLE;
  }
  //从cookie中获取token信息
  let token = userStore.getToken();
  console.log("token--->", token);
  if (!token) {
    // 没有Token重置路由到登陆页。
    resetRouter();
    //没有登录且在白名单的范围内, 将直接跳转
    if (ROUTER_WHITE_LIST.includes(to.path)) {
      //包含在白名单里, 则直接跳转
      return next();
    } else {
      //否则将跳转到登录页面
      return next({path: LOGIN_URL, replace: true});
    }
  }
  if (token) {
    // 进入页面都将获取登录用户信息
    await authStore.realTimeUserInfo();
  }
  // 所有路由信息
  console.log(router.getRoutes());
  next();
});

/**
 * @description 重置路由
 */
export const resetRouter = () => {
  const authStore = useAuthStore();
  authStore.getMenuList.forEach((route: any) => {
    const {name} = route;
    if (name && router.hasRoute(name)) {
      router.removeRoute(name);
    }
  });
};

router.onError(error => {
  // 结束全屏动画
  nprogress.done();
  console.warn("路由错误", error.message);
});

router.afterEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized) => {
  console.log("后置守卫", to, from);
  // 所有路由信息
  console.log(router.getRoutes());
  // 结束全屏动画
  nprogress.done();
});

export default router;
