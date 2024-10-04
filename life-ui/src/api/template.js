import request from '../utils/request'
import {SYSTEM_SERVER} from './severApi'
// import Qs from "qs";

//  获取模板列表
export function pageTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/page-temp`,
        method: 'POST',
        data: data,
    })
}

//  获取-根据参数
export function oneTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/temp-one`,
        method: 'POST',
        data: data
    })
}

//  保存模板
export function saveTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/save-temp`,
        method: 'POST',
        data: data
    })
}

//  更新模板
export function updateTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/update-temp`,
        method: 'POST',
        data: data
    })
}

//  保存模板
export function deleteTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/delete-temp`,
        method: 'POST',
        data: data
    })
}