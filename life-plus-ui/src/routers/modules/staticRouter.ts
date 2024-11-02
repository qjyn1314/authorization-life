import { RouteRecordRaw } from "vue-router";
import { HOME_URL, LOGIN_URL } from "@/config";
import Layout from "@/layouts/index.vue";

/**
 * LayoutRouter[布局路由]
 */
export const layoutRouter: RouteRecordRaw[] = [
  // {
  //   path: "/",
  //   redirect: HOME_URL
  // },
  // {
  //   path: "/layout",
  //   name: "layout",
  //   component: Layout,
  //   redirect: HOME_URL,
  //   children: [
  //      {
  //       path: HOME_URL, // [唯一]
  //       component: () => import("@/views/home/index.vue"),
  //       meta: {
  //         title: "主控台", // 标题
  //         enName: "Master Station", // 英文名称
  //         icon: "HomeFilled", // 图标
  //         isHide: "0", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
  //         isLink: "", // 是否外链[有值则是外链]
  //         isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
  //         isFull: "1", // 是否缓存全屏[0是，1否]
  //         isAffix: "0" // 是否缓存固定路由[0是，1否]
  //       }
  //     }
  //   ]
  // },
  // 上方或者下方效果一样
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

// 若是想将主控台放置后端，上方注释，JSON数据在下方
// {
//   "menuId": 3,
//   "menuName": "主控台",
//   "enName": "Master Station",
//   "parentId": 0,
//   "menuType": "1",
//   "path": "/home/index",
//   "name": "homePage",
//   "component": "home/index",
//   "icon": "HomeFilled",
//   "isHide": "1",
//   "isLink": "",
//   "isKeepAlive": "0",
//   "isFull": "1",
//   "isAffix": "1",
//   "redirect": "/home/index"
// },
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
    path: "/system/static", // 路由访问路径[唯一]
    name: "staticPage", // 命名路由[唯一]
    component: Layout, // 一级路由，可以将子路由放置Main模块中
    meta: {
      title: "静态路由", // 标题
      enName: "Static Router", // 英文名称
      icon: "House", // 图标
      isHide: "0", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
      isLink: "", // 是否外链[有值则是外链]
      isKeepAlive: "1", // 是否缓存路由数据[0是，1否]
      isFull: "1", // 是否缓存全屏[0是，1否]
      isAffix: "1", // 是否缓存固定路由[0是，1否]
      activeMenu: HOME_URL // 默认选中哪个路由
    },
    children: [
      // 字典详情json
      // {
      //   "menuId": 15,
      //   "menuName": "字典详情",
      //   "parentId": 1,
      //   "menuType": "2",
      //   "path": "/system/dict/data/:dictType",
      //   "name": "dictDataPage",
      //   "component": "system/dict/data",
      //   "icon": "Flag",
      //   "isHide": "1",
      //   "isLink": "",
      //   "isKeepAlive": "0",
      //   "isFull": "1",
      //   "isAffix": "1",
      //   "redirect": ""
      // },
      {
        path: "/system/dict/data/:dictType", // 路由访问路径[唯一]
        name: "dictDataPage", // 命名路由[唯一]
        component: () => import("@/views/system/dict/data.vue"), // 一级路由，可以将子路由放置Main模块中
        meta: {
          title: "字典详情", // 标题
          enName: "DictData Manage", // 英文名称
          icon: "Flag", // 图标
          isHide: "0", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
          isLink: "", // 是否外链[有值则是外链]
          isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
          isFull: "1", // 是否缓存全屏[0是，1否]
          isAffix: "1", // 是否缓存固定路由[0是，1否]
          activeMenu: "/system/dict/type" // 默认选中哪个路由
        }
      }
    ]
  }
  /** 系统管理 */
  // {
  //   path: "/system", // 路由访问路径[唯一]
  //   name: "system", // 命名路由[唯一]
  //   component: Layout, // 一级路由，可以将子路由放置Main模块中
  //   redirect: "/system/user", // path路径，<router-link name="/404"> 也是使用path进行跳转
  //   meta: {
  //     title: "系统管理", // 标题
  //     icon: "Tools", // 图标
  //     isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
  //     isLink: "", // 是否外链[有值则是外链]
  //     isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
  //     isFull: "1", // 是否缓存全屏[0是，1否]
  //     isAffix: "1" // 是否缓存固定路由[0是，1否]
  //   },
  //   children: [
  //     {
  //       path: "/system/user", // [唯一]
  //       name: "userPage",
  //       component: () => import("@/views/system/user/index.vue"),
  //       meta: {
  //         title: "用户管理", // 标题
  //         icon: "UserFilled", // 图标
  //         isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
  //         isLink: "", // 是否外链[有值则是外链]
  //         isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
  //         isFull: "1", // 是否缓存全屏[0是，1否]
  //         isAffix: "1" // 是否缓存固定路由[0是，1否]
  //       }
  //     },
  //     {
  //       path: "/system/role", // [唯一]
  //       name: "rolePage",
  //       component: () => import("@/views/system/role/index.vue"),
  //       meta: {
  //         title: "角色管理", // 标题
  //         icon: "CameraFilled", // 图标
  //         isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
  //         isLink: "", // 是否外链[有值则是外链]
  //         isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
  //         isFull: "1", // 是否缓存全屏[0是，1否]
  //         isAffix: "1" // 是否缓存固定路由[0是，1否]
  //       }
  //     },
  //     {
  //       path: "/system/menu", // [唯一]
  //       name: "menu",
  //       component: () => import("@/views/system/menu/index.vue"),
  //       meta: {
  //         title: "菜单管理", // 标题
  //         icon: "Menu", // 图标
  //         isHide: "1", // 代表路由在菜单中是否隐藏，是否隐藏[0隐藏，1显示]
  //         isLink: "", // 是否外链[有值则是外链]
  //         isKeepAlive: "0", // 是否缓存路由数据[0是，1否]
  //         isFull: "1", // 是否缓存全屏[0是，1否]
  //         isAffix: "1" // 是否缓存固定路由[0是，1否]
  //       }
  //     }
  //   ]
  // }
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
