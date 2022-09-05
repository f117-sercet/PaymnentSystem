package com.pay.pay.core.cache;

import com.pay.pay.core.constants.CS;
import com.pay.pay.core.model.security.JeeUserDetails;

/**
 * Description： token service
 *
 * @author: 段世超
 * @aate: Created in 2022/10/14 18:12
 */
public class ITokenService {

    /***
     * 处理Token信息
     * @param userDetail
     * @param cacheKey
     */
    public static void processTokenCache(JeeUserDetails userDetail, String cacheKey){

        //设置cacheKey
        userDetail.setCacheKey(cacheKey);

        //保存token
        //保存token
        RedisUtil.set(cacheKey, userDetail, CS.TOKEN_TIME);  //缓存 时间2小时, 保存具体信息而只是uid, 因为很多场景需要得到信息， 例如验证接口权限， 每次请求都需要获取。 将信息封装在一起减少磁盘请求次数， 如果放置多个key会增加非顺序读取。
    }
    /** 退出时，清除token信息 */
    public static void removeIToken(String iToken, Long currentUID){

        //1. 清除token的信息
        RedisUtil.del(iToken);
    }

    /**
     * 刷新数据
     * **/
    public static void refData(JeeUserDetails currentUserInfo){

        //保存token 和 tokenList信息
        RedisUtil.set(currentUserInfo.getCacheKey(), currentUserInfo, CS.TOKEN_TIME);  //缓存时间2小时, 保存具体信息而只是uid, 因为很多场景需要得到信息， 例如验证接口权限， 每次请求都需要获取。 将信息封装在一起减少磁盘请求次数， 如果放置多个key会增加非顺序读取。

    }

}
