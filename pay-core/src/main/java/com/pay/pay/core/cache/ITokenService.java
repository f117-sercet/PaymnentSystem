package com.pay.pay.core.cache;

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
    }

}
