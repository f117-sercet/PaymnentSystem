/**
 * 路由配置信息
 */

import Vue from 'vue'
import VueRouter from 'vue-router'
// hack router push callback
// [解决 vue-router跳转相同路径报错 ]
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {path: '/hub/:jeepayToken', name: 'Hub', component: () => import('../views/Hub.vue')}, //自动分发器
  {path: '/error', name: 'Error', component: () => import('../views/Error.vue')},
  {path: '/oauth2Callback/:paySystemToken', name: 'Oauth2Callback', component: () => import('../views/Oauth2Callback.vue')}, //oauth回调地址
  {path: '/cashier', name: 'Cashier', component: () => import('../views/Cashier.vue'), //收银台（该地址无意义）
    children: [
      { path: '/cashier/wxpay', name: 'CashierWxpay', component: () => import('../views/payway/Wxpay.vue') },
      { path: '/cashier/alipay', name: 'CashierAlipay', component: () => import('../views/payway/Alipay.vue') },
      { path: '/cashier/ysfpay', name: 'CashierYsfpay', component: () => import('../views/payway/Ysfpay.vue') }
    ]
  }
]
const router = new VueRouter({

  mode:'hash',// 需要nginx适配
  base:"",
  routes
})
export default router
