import request from '../utils/request'
import {SYSTEM_SERVER} from './severApi'

export function pageLov(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/page`,
        method: 'POST',
        data: data,
    })
}

export function oneLov(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/lov-one`,
        method: 'POST',
        data: data
    })
}

export function saveLov(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/save-lov`,
        method: 'POST',
        data: data
    })
}

export function updateLov(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/update-lov`,
        method: 'POST',
        data: data
    })
}

export function deleteLov(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/delete-lov`,
        method: 'POST',
        data: data
    })
}



export function pageLovValue(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/value/page`,
        method: 'POST',
        data: data,
    })
}

export function oneLovValue(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/value/lov-value-one`,
        method: 'POST',
        data: data
    })
}

export function saveLovValue(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/value/save-lov-value`,
        method: 'POST',
        data: data
    })
}

export function updateLovValue(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/value/update-lov-value`,
        method: 'POST',
        data: data
    })
}

export function deleteLovValue(data) {
    return request({
        url: `/${SYSTEM_SERVER}/lsys/lov/value/delete-lov-value`,
        method: 'POST',
        data: data
    })
}