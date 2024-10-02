import request from '../utils/request'
import {SYSTEM_SERVER} from './severApi'
import Qs from "qs";

//  获取模板列表
export function pageTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/page-temp`,
        method: 'POST',
        data: data
    })
}