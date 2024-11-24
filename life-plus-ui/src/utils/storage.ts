import {ACCESS_TOKEN, PINIA_PREFIX} from "@/config";
// @ts-ignore
import cookies from "js-cookie";

/**
 * 封装获取用户信息的方法
 */
export const getToken = () => {
  return cookie.get(ACCESS_TOKEN)
};

/**
 * 各个小仓库之间进行数据交互使用 OR 页面获取storge值使用
 * window.sessionStorage
 * @method set 设置会话缓存
 * @method get 获取会话缓存
 * @method remove 移除会话缓存
 * @method clear 移除全部会话缓存
 */
export const SessionStorage = {
  put(key: string, value: any) {
    window.sessionStorage.setItem(PINIA_PREFIX + key, value);
  },
  set(key: string, value: any) {
    window.sessionStorage.setItem(PINIA_PREFIX + key, value);
  },
  get(key: string) {
    const value: any = window.sessionStorage.getItem(PINIA_PREFIX + key);
    return value;
  },
  remove(key: string) {
    window.sessionStorage.removeItem(PINIA_PREFIX + key);
  },
  clear() {
    window.sessionStorage.clear();
  },
  putJSON(key: string, jsonValue: any) {
    window.sessionStorage.put(PINIA_PREFIX + key, JSON.stringify(jsonValue));
  },
  setJSON(key: string, jsonValue: any) {
    window.sessionStorage.put(PINIA_PREFIX + key, JSON.stringify(jsonValue));
  },
  getJSON(key: string) {
    const jsonValue: any = window.sessionStorage.get(PINIA_PREFIX + key);
    return JSON.parse(jsonValue);
  }
};

/**
 * window.localStorage
 * @method set 设置
 * @method get 获取
 * @method remove 移除
 * @method clear 移除全部
 */
export const LocalStorage = {
  put(key: string, value: any) {
    window.localStorage.setItem(PINIA_PREFIX + key, value);
  },
  set(key: string, value: any) {
    window.localStorage.setItem(PINIA_PREFIX + key, value);
  },
  get(key: string) {
    const value: any = window.localStorage.getItem(PINIA_PREFIX + key);
    return value;
  },
  remove(key: string) {
    window.localStorage.removeItem(PINIA_PREFIX + key);
  },
  clear() {
    window.localStorage.clear();
  },
  putJSON(key: string, jsonValue: any) {
    window.localStorage.put(PINIA_PREFIX + key, JSON.stringify(jsonValue));
  },
  setJSON(key: string, jsonValue: any) {
    window.localStorage.put(PINIA_PREFIX + key, JSON.stringify(jsonValue));
  },
  getJSON(key: string) {
    const jsonValue: any = window.localStorage.get(PINIA_PREFIX + key);
    return JSON.parse(jsonValue);
  }
};

/**
 * cookies
 * @method set 设置
 * @method get 获取
 * @method remove 移除
 */
export const cookie = {
  put(key: string, value: any) {
    cookies.set(key, value);
  },
  set(key: string, value: any) {
    cookies.set(key, value);
  },
  get(key: string) {
    return cookies.get(key);
  },
  remove(key: string) {
    cookies.remove(key);
  },
  removeToken() {
    cookies.remove(ACCESS_TOKEN);
  },
  setEx(key: string, value: any, expiresSecond: number) {
    let cookieExpires = null;
    if (expiresSecond) {
      //单位秒
      cookieExpires = new Date(new Date().getTime() + expiresSecond * 1000);
    } else {
      //如果没有过期时间则是当前会话session为过期时间
      cookieExpires = null;
    }
    if (import.meta.env.PROD) {
      return cookies.set(key, value, {
        expires: cookieExpires,
        domain: import.meta.env.VITE_APP_COOKIE_HOST
      });
    }
    return cookies.set(key, value, {
      expires: cookieExpires
    });
  }
};
