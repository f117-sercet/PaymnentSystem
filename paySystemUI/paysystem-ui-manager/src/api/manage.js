/*
 *  全系列 restful api格式, 定义通用req对象
 *
 *  @author terrfly
 *  @site https://www.jeepay.vip
 *  @date 2021/5/8 07:18
 */

import request from "../http/request";

export const req = {

  // 通用列表查询接口

  // 通用列表查询接口
  list: (url, params) => {
    return request.request({url: url, method: 'GET', params: params}, true, true, false)
  },

  // 通用新增接口
  add: (url, data) => {
    return request.request({url: url, method: 'POST', data: data}, true, true, false)
  },
  // 通用查询单条数据接口
  getById: (url, bizId) => {
    return request.request({url: url + '/' + bizId, method: 'get'}, true, true, false)
  },
  // 通用修改接口
  updateById: (url, bizId, data) => {
    return request.request({url: url + '/' + bizId, method: 'PUT', data: data}, true, true, false)
  },

  // 通用删除接口
  delById: (url, bizId) => {
    return request.request({url: url + '/' + bizId, method: 'DELETED'}, true, true, false)
  }
}
// 全系列 restful api格式 (全局loading方式)
export const reqLoad = {

  // 通用列表查询接口
  list: (url, params) => {
    return request.request({url: url, method: 'GET', params: params}, true, true, true)
  },

  // 通用新增接口
  add: (url, data) => {
    return request.request({url: url, method: 'POST', data: data}, true, true, true)
  },

  // 通用查询单条数据接口
  getById: (url, bizId) => {
    return request.request({url: url + '/' + bizId, method: 'GET'}, true, true, true)
  },

  // 通用修改接口
  updateById: (url, bizId, data) => {
    return request.request({url: url + '/' + bizId, method: 'PUT', data: data}, true, true, true)
  },

  // 通用删除接口
  delById: (url, bizId) => {
    return request.request({url: url + '/' + bizId, method: 'DELETE'}, true, true, true)
  }
}

/***角色管理页面**/
export const API_URL_ENT_LIST = '/api/sysEnts'
export const API_URL_ROLE_LIST = '/api/sysRoles'
export const API_URL_ROLE_ENT_RELA_LIST = '/api/sysRoleEntRelas'
export const API_URL_SYS_USER_LIST = '/api/sysUsers'
export const API_URL_USER_ROLE_RELA_LIST = '/api/sysUserRoleRelas'



/** 服务商、商户管理 **/
export const API_URL_ISV_LIST = '/api/isvInfo'
export const API_URL_MCH_LIST = '/api/mchInfo'
/** 商户App管理 **/
export const API_URL_MCH_APP = '/api/mchApps'
/** 支付订单管理 **/
export const API_URL_PAY_ORDER_LIST = '/api/payOrder'
/** 退款订单管理 **/
export const API_URL_REFUND_ORDER_LIST = '/api/refundOrder'
/** 商户通知管理 **/
export const API_URL_MCH_NOTIFY_LIST = '/api/mchNotify'
/** 系统日志 **/
export const API_URL_SYS_LOG = 'api/sysLog'
/** 系统配置 **/
export const API_URL_SYS_CONFIG = 'api/sysConfigs'
/** 首页统计 **/
export const API_URL_MAIN_STATISTIC = 'api/mainChart'

/** 支付接口定义页面 **/
export const API_URL_IFDEFINES_LIST = '/api/payIfDefines'
export const API_URL_PAYWAYS_LIST = '/api/payWays'
/** 服务商、商户支付参数配置 **/
export const API_URL_ISV_PAYCONFIGS_LIST = '/api/isv/payConfigs'
export const API_URL_MCH_PAYCONFIGS_LIST = '/api/mch/payConfigs'
/** 商户支付通道配置 **/
export const API_URL_MCH_PAYPASSAGE_LIST = '/api/mch/payPassages'
/** 转账订单管理 **/
export const API_URL_TRANSFER_ORDER_LIST = '/api/transferOrders'


/**上传图片/文件地址*/
export const upload = {
  avatar:request.baseUrl+'/api/ossFiles/avatar',
  ifBG:request.baseUrl + '/api/ossFiles/ifBG',
  cert: request.baseUrl + '/api/ossFiles/cert'
}
const api = {
  user: '/user',
  role_list: '/role',
  service: '/service',
  permission: '/permission',
  permissionNoPager: '/permission/no-pager',
  orgTree: '/org/tree'
}

export  default api

/** 获取权限树状结构图 **/
export function getEntTree (sysType) {
  return request.request({ url: '/api/sysEnts/showTree?sysType=' + sysType, method: 'GET' })
}

/****退款接口****/

export function payOrderRefund(payOrderId,refundAmount,refundReason){

  return request.request({
    url:'/api/payOrder/refunds/'+payOrderId,
    method:'POST',
    data:{refundAmount,refundReason}
  })

  /***更新用户角色信息**/
  export function uSysUserRoleRela(sysUserId,roleIdList){
    return request.request({
      url:'/api/sysUserRoleRelas/relas/'+sysUserId,
      method:'POST',
      data:{roleIdListStr:JSON.stringify(roleIdList)}
    })
  }
}
