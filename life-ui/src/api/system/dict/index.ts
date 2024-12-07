// 导入二次封装axios
import service from "@/utils/axios.ts";
import { SYSTEM_SERVER } from "@/utils/global.u.ts";

// 多条件分页查询数据
export const listPage = (params: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/page`, params);
};

// 添加
export const add = (data: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/save-lov`, data);
};

// 根据ID进行查询
export const getById = (params: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/lov-one`, params);
};

// 根据ID进行修改
export const update = (data: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/update-lov`, data);
};

// 批量删除
export const batchDelete = (ids: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/deleteByIds`, ids); // 第一种传参方式
};

// 值集主表不分页集合
export const allLovData = (data: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/list`, data); // 第一种传参方式
};

// 值集明细分页列表
export const lovValuePage = (data: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/value/lovValuePage`, data); // 第一种传参方式
};

// 添加值明细
export const addLovValue = (data: any) => {
    return service.post(`${SYSTEM_SERVER}/lsys/lov/value/save-lov-value`, data);
};

// 获取明细值
export const getLovValueById = (data: any) => {
    return service.post(`${SYSTEM_SERVER}/lsys/lov/value/lov-value-one`, data);
};

// 更新明细值
export const updateLovValue = (data: any) => {
   return service.post(`${SYSTEM_SERVER}/lsys/lov/value/update-lov-value`, data);
}

// 批量删除
export const batchDeleteLovValue = (ids: any) => {
  return service.post(`${SYSTEM_SERVER}/lsys/lov/value/batchDeleteLovValue`, ids);
};

// 获取值集代码对应的值代码和值内容
export const lovValueByLovCode = (data: any) => {
  data = data.concat({"tenantId":"0"});
  return service.post(`${SYSTEM_SERVER}/lsys/lov/lovvalue-by-cache`, data);
};

