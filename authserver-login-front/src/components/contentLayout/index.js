import ContentLayout from './ContentLayout'

const install = function (Vue) {
  Vue.component('ContentLayout', ContentLayout)
}

if (window.Vue) {
  window.ContentLayout = ContentLayout
  Vue.use(install); // eslint-disable-line
}

ContentLayout.install = install
export default ContentLayout
