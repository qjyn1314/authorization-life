import {axiosService} from '@/utils/server-util'
import {AUTH_SERVER} from '@/utils/global-util'

export function clientPage(param) {
    return axiosService.post(`/${AUTH_SERVER}/client/page`, param)
}


export function getSelectVal(param) {
    return axiosService.get(`/${AUTH_SERVER}/client/getSelectVal`, param)
}


export function genAuthorizationUrl(param) {
    return axiosService.get(`/${AUTH_SERVER}/client/genAuthorizationUrl`, param)
}

export function genGrantTypeUrl(param) {
    return axiosService.get(`/${AUTH_SERVER}/client/genGrantTypeUrl`, param)
}


export function saveClient(param) {
    return axiosService.post(`/${AUTH_SERVER}/client/saveClient`, param)
}


export function getClient(param) {
    return axiosService.get(`/${AUTH_SERVER}/client/getClient`, param)
}


export function delClient(param) {
    return axiosService.get(`/${AUTH_SERVER}/client/delClient`, param)
}




