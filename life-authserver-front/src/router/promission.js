
import { getUserSelf } from '@/api/common'

export async function getuserself () {
  return getUserSelf().then((res) => {
    if (Number(res.data.code) === 0) {
      let data = res.data.data
      localStorage.setItem('userinfo', JSON.stringify(data))
      localStorage.setItem('tenantId', data.tenantId || '')
    }
  }) 
}

/* eslint-disable */
export async function getUrlParams (name) { // 不传name返回所有值，否则返回对应值
 
  var url = window.location.hash 
  /* eslint-disable */
  if (url.indexOf('?') == 1) { return false }
  url = url.substr(url.indexOf("access_token"))
  url = url.split('&')
  var name = name || ''
  var nameres
  // 获取全部参数及其值
  for (var i = 0; i < url.length; i++) {
    var info = url[i].split('=')
    var obj = {}
    obj[info[0]] = decodeURI(info[1])
    url[i] = obj
  }
  // 如果传入一个参数名称，就匹配其值
  if (name) {
    for (var i = 0; i < url.length; i++) {
      for (const key in url[i]) {
        if (key == name) {
          nameres = url[i][key]
        }
      }
    }
  } else {
    nameres = url
  }
  // 返回结果
  return nameres
} 

export function find (str, cha, num) { // 获取第二个o出现的位置
  var x = str.indexOf(cha);
  for (var i = 0; i < num; i++) {
    x = str.indexOf(cha, x + 1);
  }
  return x;
}