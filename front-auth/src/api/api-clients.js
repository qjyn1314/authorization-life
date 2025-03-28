import {axiosService} from '@/utils/service-util'
import {AUTH_SERVER} from '@/utils/global-util'

export function clientPage(param) {
    return axiosService.post(`/${AUTH_SERVER}/client/page`, param)
}

export function genAuthorizationUrl(param) {
    return axiosService.get(`/${AUTH_SERVER}/client/genAuthorizationUrl`, param)
}




