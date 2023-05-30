/**
 * 获取支付方式工具类
 *
 */

import config from '@/config'


const getToPageRouteName = function () {

  const payWay = getPayWay();
  return payWay?payWay.routeName:null
}

const getPayWay = function (){
  const userAgent = navigator.userAgent

  if (userAgent.indexOf("MicroMessager")>=0){
     return config.payWay.WXPAY;
  }
  if (userAgent.indexOf("AlipayClient") > 0){
    return config.payWay.ALIPAY
  }
  return  null
}

export default {
  getToPageRouteName: getToPageRouteName,
  getPayWay: getPayWay
}
