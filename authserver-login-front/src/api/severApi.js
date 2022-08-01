 /* eslint-disable camelcase */ 
let LOGINTOKEN = {
  client_id: 'passport',
  client_secret: '3MMoCFo4nTNjRtGZ',
  redirect_uri: `${process.env.VUE_APP_API_HOST}/emd/businessList`, // 默认登录跳转的链接
  // redirect_uri: ``, // 默认登录跳转的链接
  response_type: 'token',
  grant_type: 'authorization_code',
  loginRedirectUrl: `${process.env.VUE_APP_API_HOST}` // 登录重定向域名
}

let ZTNT = `ztnt`
let ZIAM = `ziam`
let ZEMD = `zemd`
let ZFIL = `zfil`
let ZRFQ = 'zrfq'
let ZRFP = 'zrfp'
let ZMSG = `zmsg`

window.changeRoute = function (code, route) {
  switch (code) {
    case 'ZTNT':
      ZTNT = route
      console.log(ZTNT)
      break;
    case 'ZIAM':
      ZIAM = route
      console.log(ZIAM)
      break;
    case 'ZEMD':
      ZTNT = route
      console.log(ZTNT)
      break;
    case 'ZFIL':
      ZFIL = route
      console.log(ZFIL)
      break;
    case 'ZRFQ':
      ZRFQ = route
      console.log(ZRFQ)
      break;
    case 'ZRFP':
      ZRFQ = route
      console.log(ZRFQ)
      break;
    case 'ZMSG':
      ZMSG = route
      console.log(ZMSG)
      break;
  }
}

function getTenantId () {
  return localStorage.getItem('tenantId') || ''
}
export {
  ZTNT,
  ZIAM,
  ZEMD,
  ZFIL,
  ZRFQ,
  ZRFP,
  ZMSG,
  LOGINTOKEN,
  getTenantId
}
