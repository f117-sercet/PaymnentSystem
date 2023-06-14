import request from '/src/http/request'
import { Base64 } from 'js-base64'

// 登录确认接口
export function login({username,password,vercode,vercodeToken}) {

  const data = {
    ia: Base64.encode(username), // 账号
    ip: Base64.encode(password), // 密码
    vc: Base64.encode(vercode), // 验证码值
    vt: Base64.encode(vercodeToken) // 验证码token
  }
  return request.request({
    url: '/api/anon/auth/validate',
    method: 'post',
    data
  }, true, false, false)
}
  //获取图形验证码信息接口
  export function vercode(){

}
