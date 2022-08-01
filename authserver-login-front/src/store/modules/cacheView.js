const cacheView = {
  state: {
    cachedViews: []
  },
  mutations: {
    ADD_CACHE_VIEWS: (state, view) => {
      if (view.meta.cache && !state.cachedViews.some((item) => item === view.name)) {
        state.cachedViews.push(view.name)
      }
    },
    DEL_CACHE_VIEWS: (state, view) => {
      for (const i of state.cachedViews) {
        if (i === view.name) {
          const index = state.cachedViews.indexOf(i)
          state.cachedViews.splice(index, 1)
          break
        }
      }
    },
    DEL_OTHERS_VIEWS: (state, view) => {
      for (const i of state.cachedViews) {
        if (i === view.name) {
          const index = state.cachedViews.indexOf(i)
          state.cachedViews = state.cachedViews.slice(index, i + 1)
          break
        }
      }
    },
    DEL_ALL_VIEWS: (state) => {
      state.cachedViews = []
    }
  },
  actions: {
    addCacheViews ({ commit }, view) {
      commit('ADD_CACHE_VIEWS', view)
    },
    delCacheViews ({ commit, state }, view) {
      return new Promise((resolve) => {
        commit('DEL_CACHE_VIEWS', view)
        resolve([...state.cachedViews])
      })
    },
    delOthersViews ({ commit, state }, view) {
      return new Promise((resolve) => {
        commit('DEL_OTHERS_VIEWS', view)
        resolve([...state.cachedViews])
      })
    },
    delAllViews ({ commit, state }) {
      return new Promise((resolve) => {
        commit('DEL_ALL_VIEWS')
        resolve([...state.cachedViews])
      })
    }
  }
}

export default cacheView
