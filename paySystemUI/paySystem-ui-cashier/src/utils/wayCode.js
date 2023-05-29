/**
 * 获取支付方式工具类
 *
 */

import config from '@/config'


const getToPageRouteName = function () {

  const payWay = getPayWay();
  return payWay?payWay.routeName:null

}
