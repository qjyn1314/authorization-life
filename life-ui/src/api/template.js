import request from '../utils/request'
import {SYSTEM_SERVER} from './severApi'

export function pageTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/page-temp`,
        method: 'POST',
        data: data,
    })
}

export function oneTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/temp-one`,
        method: 'POST',
        data: data
    })
}

export function saveTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/save-temp`,
        method: 'POST',
        data: data
    })
}

export function updateTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/update-temp`,
        method: 'POST',
        data: data
    })
}

export function deleteTemp(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/temp/delete-temp`,
        method: 'POST',
        data: data
    })
}