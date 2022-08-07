import Vue from 'vue'
import VueI18n from 'vue-i18n'
import elementEnLocale from 'element-ui/lib/locale/lang/en' // element-ui lang
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN' // element-ui lang
import enLocale from './en'
import zhLocale from './zh'

Vue.use(VueI18n)
//  中文:zh_cn    英文:en_us
const messages = {
  en: {
    ...enLocale,
    ...elementEnLocale
  },
  zh: {
    ...zhLocale,
    ...elementZhLocale
  }
}
let lang = navigator.language || navigator.browserLanguage
const i18n = new VueI18n({
  locale: localStorage.getItem('language') || lang.substring(0, 2), // 设置默认语言文件
  messages // set locale messages
})
export {i18n}
