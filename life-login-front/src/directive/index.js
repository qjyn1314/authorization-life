import Vue from 'vue'
/* eslint-disable */
// 针对 el-select 下拉框定义的一个分页指令：滚动到底部执行加载下一页
Vue.directive('selectLoadMore', {
    bind(el, binding) {
        // 获取element-ui定义好的scroll盒子
        const SELECTWRAP_DOM = el.querySelector('.el-select-dropdown .el-select-dropdown__wrap')
        SELECTWRAP_DOM.addEventListener('scroll', function () {
            const CONDITION = this.scrollHeight - this.scrollTop === this.clientHeight
            if (CONDITION) {
                binding.value()
            }
        })
    }
}) 