import request from '@/http/request'
import wayCode from "@/utils/wayCode";
import channelUserIdUtil from "@/utils/channelUserId";
import config from '@/config'


// 获取 url

export function getRedirectUrl () {
  return request.request({
    url: '/api/cashier/redirectUrl',
    method: 'POST',
    data: { wayCode: wayCode.getPayWay().wayCode, token: config.cacheToken }
  })
}
