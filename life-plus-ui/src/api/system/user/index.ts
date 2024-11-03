// 导入二次封装axios
import server from "@/utils/axios.ts";
import { AUTH_SERVER } from "@/utils/global.ts";

// 随机获取一个励志的句子
export const inspirational = () => {
  return server.post(`/${AUTH_SERVER}/oauth/sentence`);
};

// 获取图片验证码
export const pictureCode = (uuid: string) => {
  return server.get(`/${AUTH_SERVER}/oauth/picture-code?uuid=${uuid}`);
};

// 获取client信息
export const getClient = (data: any) => {
  return server.post(`/${AUTH_SERVER}/oauth/client`, data);
};

// 登录接口
export const oauthLogin = (data: any) => {
  return server.postForm(`/${AUTH_SERVER}/oauth/login`, data,);
};

// 通过code获取access token
export const getOauth2TokenByCode = (data: any) => {
  return server.postForm(`/${AUTH_SERVER}/oauth2/token`, data);
};

// 查询当前登录用户信息, 包含角色, 菜单, 按钮, 以及基本信息
export const currentUserInfo = () => {
  return server.get(`/${AUTH_SERVER}/oauth/self-user`);
};

// SpringSecurity的退出登录处理器
export const oauthLogout = (data: any) => {
  return server.get(`/${AUTH_SERVER}/oauth/logout`, data);
};

// 发送邮箱注册验证码
export const sendEmailCode = (data: any) => {
  return server.post(`/${AUTH_SERVER}/oauth/send-email-code`, data);
};

// 邮箱注册
export const emailRegister = (data: any) => {
  return server.post(`/${AUTH_SERVER}/oauth/email-register`, data);
};

// 发送邮箱重置密码时的验证码
export const sendEmailCodeResetPwd = (data: any) => {
  return server.post(`/${AUTH_SERVER}/oauth/send-email-code-reset-pwd`, data);
};

// 邮箱注册
export const resetPassword = (data: any) => {
  return server.post(`/${AUTH_SERVER}/oauth/reset-password`, data);
};
