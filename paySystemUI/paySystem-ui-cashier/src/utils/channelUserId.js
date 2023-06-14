/**
 * 获取渠道用户，工具类
 */

const getChannelUserId = function () {

  return localStorage.getItem("channelUserId")
}

const setChannelUserId = function (channelUserId) {

  localStorage.setItem("channelUserId",channelUserId)

}

export default {
  getChannelUserId:getChannelUserId,
  setChannelUserId:setChannelUserId
}
