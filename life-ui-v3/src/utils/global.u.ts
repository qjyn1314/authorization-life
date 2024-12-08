import { koiLocalStorage } from "@/utils/storage.ts";

declare global {
  interface Window {
    changeRoute(code: string, value: string): void;
  }
}

//认证服务和授权服务
let AUTH_SERVER = koiLocalStorage.get(`auth-life`) || `auth-life`;
//使用token的服务
let SYSTEM_SERVER = koiLocalStorage.get(`system-life`) || `system-life`;
// 重新设置请求路径的前缀
window.changeRoute = function (code, route) {
  console.log("code", code, "route", route);
  koiLocalStorage.set(code, route);
};

export { AUTH_SERVER, SYSTEM_SERVER };
