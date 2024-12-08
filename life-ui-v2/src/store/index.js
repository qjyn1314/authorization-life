import Vue from 'vue'
import Vuex from 'vuex'
import ssoAuth from "../store/modules/ssoAuth";

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {},
    getters: {},
    actions: {},
    mutations: {},
    modules: {ssoAuth}
})

export default store
