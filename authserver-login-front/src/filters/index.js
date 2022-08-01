import vue from '../main.js'
/* eslint-disable */
function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

export function parseTime(time, cFormat) {
  if (!time) return ''
  if (arguments.length === 0) {
    return null
  }

  if ((time + '').length === 10) {
    time = +time * 1000
  }

  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    date = new Date(parseInt(time))
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const timeStr = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    if (key === 'a') {
      return ['一', '二', '三', '四', '五', '六', '日'][value - 1]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return timeStr
}

export function formatTime(time, option) {
  time = +time * 1000
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}
// 格式化业务类型
export function busType(val){
    if(!val) return '--'
    let vals= val.toString()
    switch(vals){
        case '1': return '普通采购'
        case '2': return '直运采购'
        case '3': return '固定资产'
        case '4': return '委外加工'
        default: return '--'
    }
}
/* 数字 格式化 */
export function nFormatter(num, digits) {
  const si = [
    { value: 1e18, symbol: 'E' },
    { value: 1e15, symbol: 'P' },
    { value: 1e12, symbol: 'T' },
    { value: 1e9, symbol: 'G' },
    { value: 1e6, symbol: 'M' },
    { value: 1e3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (
        (num / si[i].value + 0.1)
          .toFixed(digits)
          .replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
      )
    }
  }
  return num.toString()
}

export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

export function toThousandslsFilter(num) {
  return (+num || 0)
    .toString()
    .replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}
export function toThousandslsAndDigFilter(num,dig) {
    return (+num || 0).toFixed(dig)
      .toString()
      .replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
    //   return nums.toFixed(dig)
  }

export function currencyFormat(num) {
  if (num == null) {
    return 0.0
  }
  // eslint-disable-next-line
  num = num.toString().replace(/\$|\,/g, '')
  if (isNaN(num)) {
    num = '0'
  }
  // eslint-disable-next-line
  var sign = num == (num = Math.abs(num))
  num = Math.floor(num * 1000 + 0.50000000001)
  var cents = num % 1000
  num = Math.floor(num / 1000).toString()
  if (cents < 10) {
    cents = '00' + cents
  } else if (cents < 100) {
    cents = '0' + cents
  }
  for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
    num =
      num.substring(0, num.length - (4 * i + 3)) +
      ',' +
      num.substring(num.length - (4 * i + 3))
  }
  return (sign ? '' : '-') + num + '.' + cents
}

export function formatMoney(value, precision) {
  // 格式金额 123,456.02
  if (!value || value == 'null' || value == null) {
    return '0.00'
  }
  var n = precision //两位小数
  n = n > 0 && n <= 20 ? n : 2
  value = parseFloat((value + '').replace(/[^\d\.-]/g, '')).toFixed(n) + ''
  var l = value
      .split('.')[0]
      .split('')
      .reverse(),
    r = value.split('.')[1]
  var t = ''
  for (let i = 0; i < l.length; i++) {
    t += l[i] + ((i + 1) % 3 == 0 && i + 1 != l.length ? ',' : '')
  }
  return (
    t
      .split('')
      .reverse()
      .join('') +
    '.' +
    r
  )
}
//地址格式化
export function AddressStr(address) { 
  if (!address) return ''
  if (address.nodispose) {
    return (
      (address.name ? address.name : '') +
      ' ' +
      (address.phone ? address.phone : '') +
      ' ' +
      (address.address ? address.address : '')
    )
  } else {
    return (
      (address.name ? address.name : '') +
      ' ' +
      (address.phone ? address.phone : '') +
      ' ' +
      (address.provinceName ? address.provinceName : '') +
      (address.cityName ? address.cityName : '') +
      (address.areaName ? address.areaName : '') +
      (address.street ? address.street : '')
    )
  }
}

export function AddressNamePhoneStr(address) {
  if (!address) return ''
  if (address.nodispose) {
    return (
      (address.name ? address.name : '') +
      ' ' +
      (address.phone ? address.phone : '')
    )
  } else {
    return (
      (address.name ? address.name : '') +
      ' ' +
      (address.phone ? address.phone : '')
    )
  }
}
export function AddressStreetStr(address) {
  if (!address) return ''
  if (address.nodispose) {
    return address.address ? address.address : ''
  } else {
    return (
      (address.provinceName ? address.provinceName : '') +
      (address.cityName ? address.cityName : '') +
      (address.areaName ? address.areaName : '') +
      (address.street ? address.street : '')
    )
  }
}

export function goodsOrService(type) {
  switch (type) {
    case '1':
      return `${vue.$t('page.detail.Goods')}`
    case '2':
      return `${vue.$t('page.detail.Service')}`
  }
}
export  function filterType (value) {
  switch (value) {
    case 0: return `${vue.$t('page.detail.DirectPayment')}`
    case 1: return `${vue.$t('page.detail.POpayment')}`
    case 2: return `${vue.$t('page.detail.Prepayment')}`
  }
}
export  function payMethodType (value) {
    let vals = Number(value)
  switch (vals) {
    case 0: return `${vue.$t('page.bi.Onlinebanktransfer')}`
    case 2: return `${vue.$t('page.bi.Check')}`
    case 3: return `${vue.$t('page.bi.offsetLoan')}`
    case 6: return `${vue.$t('page.bi.ForeignExchange')}`
  }
}
export function assertOrCost(type) {
  switch (type) {
    case '1':
      return `${vue.$t('page.detail.Capex')}`
    case '2':
      return `${vue.$t('page.detail.Expense')}`
  }
}

export function purpost(type) {
  type=type.toString()
  switch (type) {
    case '1':
      return `${vue.$t('page.detail.Work')}`
    case '2':
      return `${vue.$t('page.detail.EmployeeIncentive')}`
    case '3':
      return `${vue.$t('page.detail.CustomerIncentive')}`
  }
}
//商品二级分类
export function goodsCategory(value, list) {
  var returnStr = '　'
  try {
    var parentItem = list.find(item => item.id === value[0]) 
    var childItem = parentItem.classificationList.find(
      item => item.id === value[1]
    ) 
    returnStr = parentItem.procurementName + '>' + childItem.procurementName
  } catch (e) { 
    returnStr = '　'
  }
  return returnStr
}
//商品一级分类
export function goodsOneCategory(value, list) {
  var returnStr = '　'
  try {
    var parentItem = list.find(item => item.id === value[0]) 
    var childItem = parentItem.classificationList.find(
      item => item.id === value[1]
    ) 
    returnStr = parentItem.procurementName
  } catch (e) { 
    returnStr = '　'
  }
  return returnStr
}
//商品二级分类
export function goodsTwoCategory(value, list) {
  var returnStr = '　'
  try {
    var parentItem = list.find(item => item.id === value[0]) 
    var childItem = parentItem.classificationList.find(
      item => item.id === value[1]
    ) 
    returnStr =  childItem.procurementName
  } catch (e) { 
    returnStr = '　'
  }
  return returnStr
}
// 合同类型
export function contractType(value, list) {
  var returnStr = '　'
  try {
    var parentItem = list.find(item => item.contract_type_code === value[0]) 
    var childItem = parentItem.contract_item.find(
      item => item.item_code === value[1]
    ) 
    returnStr = parentItem.contract_type_name + '>' + childItem.item_name
  } catch (e) { 
    returnStr = '　'
  }
  return returnStr
}

//预算状态
export function budgetStatusStr(status) {
  switch (status) {
    case 1:
      return  `${vue.$t('page.detail.passed')}`
    case 2:
      return  `${vue.$t('page.detail.overBudget')}`
    default:
      return  `${vue.$t('page.detail.NoBudget')}`
  }
}
// 申请的状态
export function ApplyStatusStr(status) {
  switch (status) {
    case 1:
      return  `${vue.$t('page.bi.Draft')}`
    case 2:
      return  `${vue.$t('page.bi.Approving')}`
    case 3:
      return  `${vue.$t('page.bi.wait_confirm')}`
    case 4:
      return `${vue.$t('page.bi.Cancelled')}`
  }
}
// 订单的状态
export function OrderStatusStr(status) {
  switch (status) {
    case 1:
      return   `${vue.$t('page.bi.wait_confirm')}`
    case 2:
      return   `${vue.$t('page.bi.wait_receive')}`
    case 3:
      return  `收货中`
    case 4:
      return `收货完成`
    case 5:
      return  `取消中`
    case 9:
      return   `已取消`


  }
}
// 订单的状态
export function PaymentStatusStr(status) {
  switch (status) {
    case 1:
      return  `${vue.$t('page.bi.Draft')}`
    case 2:
      return  `${vue.$t('page.bi.Approving')}`
    case 3:
      return   `${vue.$t('page.bi.already_pay')}`
    case 4:
      return   `${vue.$t('page.bi.Paid')}`
    case 5:
      return   `${vue.$t('page.bi.Draft')}`  //被退回,实际是待提交
    case 6:
      return  `${vue.$t('page.bi.Cancelled')}`
  }
}
export function  payableType (value) {
  switch (value) {
    case 0: return `${vue.$t('page.detail.DirectPayment')}`
    case 1: return `${vue.$t('page.detail.POpayment')}`
    case 2: return `${vue.$t('page.detail.Prepayment')}`
  }
}

//预算对象-成本中心-父
export function budgetObjectStr(value) {
  switch (value) {
    case '2':
      return `${vue.$t('page.detail.productCode')}`
    case '3':
      return `${vue.$t('page.detail.projectCode')}`
    default:
      return `${vue.$t('page.detail.CostCenter')}`
  }
}

// 历史操作
export function  operationType (val) {
      switch (val) {
        case 1: return `${vue.$t('page.detail.Approve')}`
        case 2: return `${vue.$t('page.detail.Reject')}`
        case 3: return `${vue.$t('page.detail.Recall')}`
        case 4: return `${vue.$t('page.detail.Submits')}`
        case 5: return `${vue.$t('page.detail.create')}`
        case 6: return `${vue.$t('page.detail.Confirm')}`
        case 7: return `${vue.$t('page.detail.send')}`
        case 8: return `${vue.$t('page.detail.Invoicing')}`
        case 9: return `${vue.$t('page.detail.transmit')}`
        case 10: return `合同失效`
        case 11: return `${vue.$t('page.detail.create')}`
        case 12: return `作废`
        case 13: return `${vue.$t('page.detail.Receive')}`
        case 14: return `${vue.$t('page.detail.generate')}`
        case 15: return `${vue.$t('page.detail.PaymentRequest')}`
        case 16: return `${vue.$t('page.detail.Reject')}`
        case 17: return `${vue.$t('page.detail.Edit')}`
        case 18: return `合同生效`
        case 19: return `修改`
        case 20: return `失效`
        case 21: return `${vue.$t('page.detail.release')}`
        case 22: return `变更`
        case 23: return `${vue.$t('page.detail.POSplit')}`
        case 24: return `${vue.$t('page.detail.Canel')}`
        case 25: return `${vue.$t('page.detail.Completed')}`
        case 59: return `${vue.$t('page.detail.copyc')}`
        case 501: return `BPM调用异常`
        case 502: return `创建合同失败`
        case 503: return `${vue.$t('page.detail.ConfirmPayment')}`
        case 504: return `${vue.$t('page.detail.AddComments')}`
        case 505: return `${vue.$t('page.detail.EditVoucher')}`
        case 33: return '修改总价-二次审批'
        case 35: return `${vue.$t('page.detail.ReceiveConfirm')}`
        case 36:  return '批准并上传附件'
        case 37:  return '拒绝'
        case 38:  return '拒绝取消'
        case 39:  return '接受取消'
        case 40:  return '申请取消'
        case 41:  return '上传发票'
        case 42:  return '发票通过'
        case 43:  return '发票不通过'
        case 44:  return '上传签约文件'
        case 45:  return '删除签约文件'
        case 46:  return '关闭收货'
        case 47:  return '确认退货'
        case 48:  return '驳回'
        case 49:  return '收到退货'
        case 50:  return '补发货'
        case 51:  return '关联发票'
        case 52:  return '付款'
        case 53:  return '采购变更'
        case 54:  return '追加订单'
        default:
          return ``
      }
    }
    export function  applicationType (val) {
      switch (val) {
        case 0: return `${vue.$t('page.detail.Procurement')}`
        case 1: return `${vue.$t('page.detail.Noncatalogprocurement')}`
        case 2: return `${vue.$t('page.detail.Indirectprocurement')}`
        case 3: return `${vue.$t('page.detail.Supplier')}`
        case 4: return `${vue.$t('page.detail.PaymentDirectpayment')}`
        case 5: return `${vue.$t('page.detail.collaborationinviatation')}`
        case 6: return `${vue.$t('page.detail.PO')}`
        case 7: return `${vue.$t('page.detail.POpayment')}`
        case 8: return `${vue.$t('page.detail.Prepayment')}`
        case 10: return `${vue.$t('page.detail.CatalogRequestbySupplier')}`
        case 11: return `${vue.$t('page.detail.Suppliercatalog')}`
        case 12: return `${vue.$t('page.detail.Colloborationrespond')}`
        case 13: return `${vue.$t('page.detail.SendReceiveReturnHistory')}`
        case 14: return `${vue.$t('page.detail.Directprocurement')}`
        case 15: return `${vue.$t('page.detail.Ecommerceprocurement')}`
        case 17: return `合同申请`
        case 33: return `合同类型`
        case 40: return `退货`
        case 41: return `对账单`

        default:
          return ``
      }
    }
//预算对象-成本中心
export function costCenterStr(value, list = []) {
  var item = list.find(item => item.id === value)
  return item ? item.costCenterName : ''
}
//预算对象-费用科目
export function accountOFFnanceStr(value, list = []) {
  var item = list.find(item => item.id === value)
  return item ? item.name : ''
}
//预算对象-预算周期
export function chargeCycleStr(value, list = []) {
  var item = list.find(item => item.value === value)
  return item ? item.key : ''
}
// 预算信息
export function getBudgetObjectText(val= '1'){
    let value = val.toString()
    switch (value) {
        case '2':
          return `${vue.$t('page.detail.productCode')}`
        case '3':
          return `${vue.$t('page.detail.projectCode')}`
        default:
          return `${vue.$t('page.detail.CostCenter')}`
      }
}