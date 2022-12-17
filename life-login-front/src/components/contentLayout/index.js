import ContentLayout from './ContentLayout'
import Vue from "vue";

const install = function (Vue) {
  Vue.component('ContentLayout', ContentLayout)
}

if (window.Vue) {
  window.ContentLayout = ContentLayout
  Vue.use(install); // eslint-disable-line
}

ContentLayout.install = install
export default ContentLayout
