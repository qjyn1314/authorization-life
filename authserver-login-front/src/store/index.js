import Vue from 'vue'
import Vuex from 'vuex'
import cacheView from './modules/cacheView'
import app from './modules/app'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    cacheView,
    app
  },
  getters
})

export default store
