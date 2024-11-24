import { RouteRecordRaw } from "vue-router";
import { HOME_URL, LOGIN_URL } from "@/config";
import Layout from "@/layouts/index.vue";

/**
 * LayoutRouter[布局路由]
 */
export const layoutRouter: RouteRecordRaw[] = [
  {
    // 登录成功以后展示数据的路由[一级路由，可以将子路由放置Main模块中(核心)]
    path: "/", // 路由访问路径[唯一]
    name: "layout", // 命名路由[唯一]
    component: Layout, // 登录进入这个页面，这个页面是整个布局
    redirect: HOME_URL, // path路径，<router-link name="/404"> 也是使用path进行跳转
    children: [
      {
        path: HOME_URL, // [唯一]
        component: () => import("@/views/home/index.vue"),
        meta: {
          title: "主控台", // 标题
          enName: "Master Station", // 英文名称
          icon: "HomeFilled", // 图标
          isHide: "0", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
          isLink: "", // 是否外链[有值则是外链]
          isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
          isFull: "1", // 是否缓存全屏[0是，1否]
          isAffix: "0" // 是否缓存固定路由[0是，1否]
        }
      }
    ]
  },
  {
    path: LOGIN_URL,
    name: "login",
    component: () => import("@/views/login/index.vue"),
    meta: {
      title: "登录"
    }
  }
];
/**
 * staticRouter[静态路由]
 */
export const staticRouter: RouteRecordRaw[] = [
  /** 主控台 */
  {
    path: HOME_URL, // [唯一]
    component: () => import("@/views/home/index.vue"),
    meta: {
      title: "主控台", // 标题
      enName: "Master Station", // 英文名称
      icon: "HomeFilled", // 图标 HomeFilled
      isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
      isLink: "", // 是否外链[有值则是外链]
      isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
      isFull: "1", // 是否缓存全屏[0是，1否]
      isAffix: "0" // 是否缓存固定路由[0是，1否]
    }
  },
  {
    path: "/auth-redirect",
    name: "authRedirect",
    component: () => import("@/views/login/components/Temporary.vue"),
    meta: {
      title: "临时授权"
    }
  }
];

/**
 * errorRouter (错误页面路由)
 */
export const errorRouter = [
  {
    path: "/403",
    name: "403",
    component: () => import("@/views/error/403.vue"),
    meta: {
      title: "403页面",
      enName: "403 Page", // 英文名称
      icon: "QuestionFilled", // 菜单图标
      isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
      isLink: "1", // 是否外链[有值则是外链]
      isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
      isFull: "1", // 是否缓存全屏[0是，1否]
      isAffix: "1" // 是否缓存固定路由[0是，1否]
    }
  },
  {
    path: "/404",
    name: "404",
    component: () => import("@/views/error/404.vue"),
    meta: {
      title: "404页面",
      enName: "404 Page", // 英文名称
      icon: "CircleCloseFilled", // 菜单图标
      isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
      isLink: "1", // 是否外链[有值则是外链]
      isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
      isFull: "1", // 是否缓存全屏[0是，1否]
      isAffix: "1" // 是否缓存固定路由[0是，1否]
    }
  },
  {
    path: "/500",
    name: "500",
    component: () => import("@/views/error/500.vue"),
    meta: {
      title: "500页面",
      enName: "500 Page", // 英文名称
      icon: "WarningFilled", // 图标
      isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
      isLink: "1", // 是否外链[有值则是外链]
      isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
      isFull: "1", // 是否缓存全屏[0是，1否]
      isAffix: "1" // 是否缓存固定路由[0是，1否]
    }
  },
  // 找不到path将跳转404页面
  {
    path: "/:pathMatch(.*)*",
    component: () => import("@/views/error/404.vue")
  }
];
