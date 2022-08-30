/* eslint-disable quote-props */
import Vue from 'vue'
import {getCookie, isDevelop, setCookie} from './index'

Vue.prototype.goBack = () => {
    window.history.back(-1)
}

Vue.prototype.getCookie = getCookie
Vue.prototype.setCookie = setCookie
Vue.prototype.isDevelop = function () {
    return isDevelop()
}
 